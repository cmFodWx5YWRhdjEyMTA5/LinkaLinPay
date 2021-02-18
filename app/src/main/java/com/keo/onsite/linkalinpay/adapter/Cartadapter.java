package com.keo.onsite.linkalinpay.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.CartlistActivity;
import com.keo.onsite.linkalinpay.activity.OnItemDeletedListener;
import com.keo.onsite.linkalinpay.activity.ProductDetails_customer;
import com.keo.onsite.linkalinpay.activity.Productcategory;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.model.cartlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class Cartadapter extends RecyclerView.Adapter<Cartadapter.ViewHolder> {

    private Context context;
    ArrayList<cartlistmodelclass> itemm;
    private String img = "";
    //int minteger = 1;
    public static double Price = 0.0;
    ConnectionDetector connection;
    private ProgressDialog progressDialog;
    private MultipartEntity reqEntity;
    UserShared psh;
    private TextView subtotal_tv,total_tv;
    String total_value;
     double pricess=0.0;
    // private Double total;
    private static double fullprice, totalfull;
    String productid,customerid,sellerid;
    ItemDeleteListener itemDeleteListener;
    private OnItemDeletedListener onItemDeletedListener;

   Translator t ;


    public Cartadapter(Context context, ArrayList<cartlistmodelclass> items,TextView subtotal_tv,TextView total_tv,String total_value) {
        this.context = context;
        this.itemm = items;
        this.subtotal_tv = subtotal_tv;
        this.total_tv=total_tv;
        this.total_value=total_value;

        t =  new Translator();
        t.getTranslateService(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item_row, null);
        final ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        psh = new UserShared(context);
        connection = new ConnectionDetector(context);
        productid=itemm.get(position).cart_pid;
        sellerid=itemm.get(position).cart_sellerid;
        customerid=itemm.get(position).cart_cust;
        viewHolder.title_tv.setText(t.translate(context,itemm.get(position).product_name));
        viewHolder.price_tv.setText(t.translate(context,itemm.get(position).product_currency+" " + itemm.get(position).product_price));
        viewHolder. price_tv.setPaintFlags(viewHolder.price_tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        viewHolder.offer_price.setText(t.translate(context,itemm.get(position).product_currency+" "+itemm.get(position).product_offerprice));
        viewHolder.qty_edt.setText(t.translate(context,itemm.get(position).cart_qty));
        //total_tv.setText(total_value);
        //subtotal_tv.setText(total_value);



        String IMG=itemm.get(position).product_image;


        if (!IMG.equals("")) {
            try {
                //String apiLink = context.getResources().getString(R.string.banner)+IMg;
                String apiLink = IMG;
                //Log.d("User Profile Image Parsing", "apiLink:"+apiLink);
                String encodedurl = "";
                encodedurl = apiLink.substring(0, apiLink.lastIndexOf('/')) + "/" + Uri.encode(apiLink.substring(
                        apiLink.lastIndexOf('/') + 1));
                Log.d("User", "encodedurl:" + encodedurl);
                if (!apiLink.equals("") && apiLink != null) {
                    Picasso.with(context)
                            .load(encodedurl) // load: This path may be a remote URL,
                            .placeholder(R.drawable.defaultpic)
                            //.resize(130, 130)
                            .error(R.drawable.defaultpic)
                            .into(viewHolder.img); // Into: ImageView into which the final image has to be passed
                    //  .resize(130, 130)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        viewHolder.remove_linearayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeapi(Integer.parseInt(itemm.get(position).cart_pid));
                 //total_tv.setText("");

                Double  pricess = Double.parseDouble(itemm.get(position).product_offerprice) * Double.parseDouble(itemm.get(position).cart_qty);
                double  pretotal= Double.parseDouble(total_value)-pricess;
                subtotal_tv.setText(t.translate(context,String.valueOf(pretotal)));
                total_tv.setText(t.translate(context,String.valueOf(pretotal)));

                 remove(viewHolder.getAdapterPosition());


            }
        });


    }


      private void remove(final int position) {
       int newPosition =position ;
        itemm.remove(position);
        notifyItemRemoved(newPosition);
        notifyItemRangeChanged(newPosition, itemm.size());

     }



    private static int newpos;


    private void removeapi(final int pos) {
        newpos=pos;
         if (VolleySingleton.getInstance(context).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.productremovecart,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                                    notifyDataSetChanged();
                                    stopProgress();

                                } else {

                                    stopProgress();

                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            //parseVolleyError(error);


                            if (error instanceof NetworkError) {

                                //Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                                showSnackBar("No internet connection");
                            } else if (error instanceof ParseError) {

                                showSnackBar("No internet connection");
                            } else if (error instanceof NoConnectionError) {

                                showSnackBar("No internet connection");
                            } else {

                                parseVolleyError(error);

                                stopProgress();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("seller_id",sellerid );
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id", String.valueOf(pos));

                    // params.put("radius", "5");


                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    //  headers.put("X-Auth-Token", "4532794240e799529b08f39bc54a354f");
                    return headers;
                }

            };
            strRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String errors = data.getString("error");
            Log.e("VolleyError", errors);
            showSnackBar(errors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void showSnackBar(String msg) {
    Toast.makeText(context,Toast.LENGTH_LONG,Toast.LENGTH_LONG).show();

     }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(context.getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }



    private void showToastLong(String string) {
    Toast.makeText(context, string, Toast.LENGTH_LONG).show();


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView title_tv, subtitle_tv, price_tv,offer_price;
        TextView qty_edt, increment_edt, decrement_edt;
        int minteger = 1;
        ImageView remove_linearayout;
        ImageView img;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            title_tv = (TextView) itemLayoutView.findViewById(R.id.product_name);
            price_tv = (TextView) itemLayoutView.findViewById(R.id.product_price);
            offer_price=(TextView)itemLayoutView.findViewById(R.id.product_offerprice);
            qty_edt = (TextView) itemLayoutView.findViewById(R.id.quantity);
            //increment_edt = (EditText) itemLayoutView.findViewById(R.id.increment);
            //decrement_edt = (EditText) itemLayoutView.findViewById(R.id.decrement);
            remove_linearayout = (ImageView) itemLayoutView.findViewById(R.id.remove);
            img=(ImageView)itemLayoutView.findViewById(R.id.imageView);

        }
    }

    @Override
    public int getItemCount() {
        return itemm.size();
    }
    public static interface ItemDeleteListener {
        public void onItemDelete(double price);
    }
    public void setOnItemDeletedListener(Object object) {
        onItemDeletedListener = (OnItemDeletedListener) object;
    }
}
