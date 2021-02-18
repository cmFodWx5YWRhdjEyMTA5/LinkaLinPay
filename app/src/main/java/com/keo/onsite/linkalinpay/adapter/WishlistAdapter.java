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
import com.keo.onsite.linkalinpay.activity.EditProductActivity;
import com.keo.onsite.linkalinpay.activity.ProductDetailsActivity;
import com.keo.onsite.linkalinpay.activity.model.Productlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Wishlistmodelclass;
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

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyViewHolder> {
    private Context context;
    private List<Wishlistmodelclass> cartList;
    private ProgressDialog progressDialog;
    String product_id,sellerid;
    UserShared psh;
    Translator t;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView productname, price,offerprice,addtocart;
        public ImageView wishlistimage,delete;


        public MyViewHolder(View view) {
            super(view);
            productname = view.findViewById(R.id.productname);
            price = view.findViewById(R.id.productprice);
            wishlistimage = view.findViewById(R.id.imageview);
            offerprice=view.findViewById(R.id.productofferprice);
            delete=(ImageView)view.findViewById(R.id.delete);
            addtocart=(TextView)view.findViewById(R.id.addtocart);

        }
    }


    public WishlistAdapter(Context context, List<Wishlistmodelclass> cartList) {
        this.context = context;
        this.cartList = cartList;
        t =  new Translator();
        t.getTranslateService(context);
    }

    @Override
    public WishlistAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wishlist_item, parent, false);

        return new WishlistAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final WishlistAdapter.MyViewHolder holder, final int position) {
        psh=new UserShared(context);
        sellerid=cartList.get(position).seller_id;

        holder.productname.setText(t.translate(context,cartList.get(position).product_name));
        holder.price.setText("₹" + cartList.get(position).product_price);
        holder. price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.offerprice.setText("₹"+cartList.get(position).product_offerprice);
        String IMg = cartList.get(position).product_image;
        if (!cartList.equals("")) {
            try {
                //String apiLink = context.getResources().getString(R.string.banner)+IMg;
                String apiLink = IMg;
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
                            .into(holder.wishlistimage); // Into: ImageView into which the final image has to be passed
                    //  .resize(130, 130)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

  holder.addtocart.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        addtocartapi(cartList.get(position).product_id);
    }
});

    holder.delete.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            removeapi(cartList.get(position).product_id);
            remove(holder.getAdapterPosition());
        }
    });
  }

    private void remove(int position) {
        int newPosition =position ;
        cartList.remove(position);
        notifyItemRemoved(newPosition);
        notifyItemRangeChanged(newPosition, cartList.size());
    }

    private void removeapi(final String id) {
        if (VolleySingleton.getInstance(context).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Removelistapi ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg=jsonObject.getString("msg");

                                if (status.equals("success")){
                                    Toast.makeText(context,t.translate(context,msg),Toast.LENGTH_LONG).show();
                                    stopProgress();


                                    //notifyDataSetChanged();

                                }

                                else {
                                    Toast.makeText(context,t.translate(context,msg),Toast.LENGTH_LONG).show();
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

                    params.put("seller_id", sellerid);
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id", id);
                   //params.put("OrganizationId", "");
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


    private void addtocartapi(final  String id) {
        if (VolleySingleton.getInstance(context).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.AddtoCartapiapi ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg=jsonObject.getString("msg");

                                if (status.equals("success")){
                                    Toast.makeText(context,t.translate(context,msg),Toast.LENGTH_LONG).show();
                                    stopProgress();


                                    //notifyDataSetChanged();

                                }

                                else {
                                    Toast.makeText(context,t.translate(context,msg),Toast.LENGTH_LONG).show();
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

                    params.put("seller_id", sellerid);
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id", id);
                    params.put("qty", "1");
                    //params.put("OrganizationId", "");
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
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();

    }

    private void stopProgress() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }



    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(context.getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }


















}
