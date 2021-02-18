package com.keo.onsite.linkalinpay.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.keo.onsite.linkalinpay.activity.model.Productcategorymodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productlistmodelclass_cust;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.fragement.ProductListFragment;
import com.keo.onsite.linkalinpay.fragement.WishListFragment;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductlistAdapter_cust extends RecyclerView.Adapter<ProductlistAdapter_cust.ViewHolder>{

    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Productlistmodelclass_cust> productList;
    //getting the context and product list with constructor
    private ProgressDialog progressDialog;
    String categoryid;
    public Button status_btn;
    String Category_status;
    String image,productid,sellerid;
    UserShared psh;
   String seller_id;
   Translator t;

    public ProductlistAdapter_cust(Context mCtx, List<Productlistmodelclass_cust> productList,String seller_id) {
        this.mCtx = mCtx;
        this.productList = productList;
        this.seller_id=seller_id;
        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public ProductlistAdapter_cust.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.item_product, null);
        return new ProductlistAdapter_cust.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProductlistAdapter_cust.ViewHolder holder, final int position) {
        //binding the data with the viewholder views
        psh=new UserShared(mCtx);

        productid=productList.get(position).product_id;
        sellerid=productList.get(position).seller_id;

        holder.productname.setText(t.translate(mCtx,productList.get(position).product_name));
        holder.price.setText(productList.get(position).product_currency+" "+productList.get(position).product_price);
        holder.price.setPaintFlags(holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.offerprice.setText(productList.get(position).product_currency+" "+productList.get(position).product_offerprice);
         holder.addtocart.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               addtocartapi(productList.get(position).product_id);
           }
       });

     holder.wishlist.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
          wishlistapi(productList.get(position).product_id);


      }
  });


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


    holder.imageview.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent i=new Intent(mCtx, ProductDetails_customer.class);
        i.putExtra("productid",productList.get(position).product_id);
        i.putExtra("sellerid",productList.get(position).seller_id);

        mCtx.startActivity(i);

    }
});

    }

    private void wishlistapi(final String id) {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Wishtlistapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(mCtx, t.translate(mCtx,msg), Toast.LENGTH_LONG).show();
                                    stopProgress();
                                    WishListFragment fragmentB=new WishListFragment();
                                    FragmentManager fragmentManager = ((AppCompatActivity)mCtx).getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, fragmentB);
                                    fragmentTransaction.commit();
                                    Bundle bundle=new Bundle();
                                    bundle.putString("seller_id",sellerid);



                                    fragmentB.setArguments(bundle);



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
                    params.put("seller_id", sellerid);
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id", id);



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

    private void addtocartapi(final String id) {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.AddtoCartapiapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(mCtx, t.translate(mCtx,msg), Toast.LENGTH_LONG).show();
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
                    params.put("seller_id", seller_id);
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id",String.valueOf(id));
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

    private void parseVolleyError(VolleyError error) {
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
    Toast.makeText(mCtx,msg,Toast.LENGTH_LONG).show();

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(mCtx, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(mCtx.getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView productname, price,addtocart;
        TextView offerprice;
        ImageView imageview,wishlist;

        public RelativeLayout view_foreground,view_background;
        public ViewHolder(View itemView) {
            super(itemView);

            productname = itemView.findViewById(R.id.productname);
            price = itemView.findViewById(R.id.price);
            offerprice = itemView.findViewById(R.id.offerprice);
            imageview=itemView.findViewById(R.id.imageview);
            addtocart=itemView.findViewById(R.id.addtocart);
            wishlist=itemView.findViewById(R.id.wishlist);
        }
    }












}
