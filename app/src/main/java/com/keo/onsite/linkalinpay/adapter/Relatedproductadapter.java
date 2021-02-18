package com.keo.onsite.linkalinpay.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.keo.onsite.linkalinpay.activity.ProductDetails_customer;
import com.keo.onsite.linkalinpay.activity.model.Productlistmodelclass_cust;
import com.keo.onsite.linkalinpay.activity.model.RelatedProductmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relatedproductadapter extends  RecyclerView.Adapter<Relatedproductadapter.ViewHolder>{
     String sellerid,productid;
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<RelatedProductmodelclass> productList;
    //getting the context and product list with constructor
    private ProgressDialog progressDialog;
    String categoryid;
    public Button status_btn;
    String Category_status;
    String image;
    UserShared psh;


    public Relatedproductadapter(Context mCtx, List<RelatedProductmodelclass> productList,String sellerid,String productid) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.sellerid=sellerid;
        this.productid=productid;
    }

    @Override
    public Relatedproductadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_product_related, null);
        return new Relatedproductadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final Relatedproductadapter.ViewHolder holder, final int position) {
        //binding the data with the viewholder views
       psh=new UserShared(mCtx);

        holder.productname.setText(productList.get(position).product_name);
        holder.price.setText(productList.get(position).product_price);
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        image=productList.get(position).product_image;
        if (!image.equals("")) {
            try {
                //String apiLink = context.getResources().getString(R.string.banner)+IMg;
                String apiLink = image;
                //Log.d("User Profile Image Parsing", "apiLink:"+apiLink);
                String encodedurl = "";
                encodedurl = apiLink.substring(0, apiLink.lastIndexOf('/')) + "/" + Uri.encode(apiLink.substring(
                        apiLink.lastIndexOf('/') + 1));
                Log.d("User", "encodedurl:" + encodedurl);
                if (!apiLink.equals("") && apiLink != null) {
                    Picasso.with(mCtx)
                            .load(encodedurl) // load: This path may be a remote URL,
                            .placeholder(R.drawable.defaultpic)
                            //.resize(130, 130)
                            .error(R.drawable.defaultpic)
                            .into(holder.imageview); // Into: ImageView into which the final image has to be passed
                    //  .resize(130, 130)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       holder.addtocart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addtocartapi(productList.get(position).product_id);
           }
       });


    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    private void addtocartapi(final String pos) {

        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            //showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.AddtoCartapiapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(mCtx, msg, Toast.LENGTH_LONG).show();
                                    //stopProgress();

                                } else {

                                    //stopProgress();

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

                                //stopProgress();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("seller_id", sellerid);
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id", String.valueOf(pos));
                    params.put("qty", "1");
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
            VolleySingleton.getInstance(mCtx).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(mCtx, "No internet connection", Toast.LENGTH_SHORT).show();
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
    private void showSnackBar(String no_internet_connection) {
    Toast.makeText(mCtx,no_internet_connection,Toast.LENGTH_LONG).show();


    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname, price,addtocart;
        TextView offerprice;
        ImageView imageview;

        public RelativeLayout view_foreground,view_background;
        public ViewHolder(View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.price);
            offerprice = itemView.findViewById(R.id.offerprice);
            imageview=itemView.findViewById(R.id.imageview);
            addtocart=itemView.findViewById(R.id.addtocart);

        }
    }






















}
