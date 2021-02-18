package com.keo.onsite.linkalinpay.adapter;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.keo.onsite.linkalinpay.activity.CouponDetailActivity;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.CouponListmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Couponlistadapter extends RecyclerView.Adapter<Couponlistadapter.ViewHolder>{

    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private ArrayList<CouponListmodelclass> productList;
    //getting the context and product list with constructor
    String coupon_id;
    String status;
    private ProgressDialog progressDialog;

    Translator t ;

    public Couponlistadapter(Context mCtx, ArrayList<CouponListmodelclass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public Couponlistadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.coupon_list_item, null);
        return new Couponlistadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Couponlistadapter.ViewHolder holder, final int position) {

        //binding the data with the viewholder views
        holder.Couponname.setText(t.translate(mCtx,productList.get(position).name));
        holder.amount.setText(t.translate(mCtx,productList.get(position).amount));
        holder.date.setText(productList.get(position).expirydate);
        coupon_id=productList.get(position).id;

        holder.delete.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {

             AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mCtx);
             alertDialogBuilder.setMessage(t.translate(mCtx,"Are you sure want to delete"));

             alertDialogBuilder.setPositiveButton(mCtx.getResources().getText(R.string.yes),
                             new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface arg0, int arg1) {
                                     Toast.makeText(mCtx,t.translate(mCtx,"You clicked yes button"),Toast.LENGTH_LONG).show();
                                      deletecouponapi();
                                     remove(position);
                                 }
                             });

             alertDialogBuilder.setNegativeButton(mCtx.getResources().getText(R.string.no),new DialogInterface.OnClickListener() {
                 @Override
                 public void onClick(DialogInterface dialog, int which) {
                     dialog.dismiss();
                 }
             });

             AlertDialog alertDialog = alertDialogBuilder.create();
             alertDialog.show();










         }
     });




       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(mCtx, CouponDetailActivity.class);
              i.putExtra("couponname",productList.get(position).name);
              i.putExtra("couponcode",productList.get(position).code);
              i.putExtra("expirydate",productList.get(position).expirydate);
              i.putExtra("amount",productList.get(position).amount);
              i.putExtra("limit",productList.get(position).limit);
              i.putExtra("status",productList.get(position).status);
              i.putExtra("type",productList.get(position).type);

               mCtx.startActivity(i);

           }
       });



    }

    private void deletecouponapi() {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.coupondelete,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(mCtx, msg, Toast.LENGTH_LONG).show();
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
                    params.put("id", coupon_id);

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


    private void remove(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());


    }











    class ViewHolder extends RecyclerView.ViewHolder {

        TextView Couponname, amount,date;
        ImageView view,delete;
        Button status_btn,status_inactive;





        public ViewHolder(View itemView) {
            super(itemView);

            Couponname = itemView.findViewById(R.id.Couponname);
            amount = itemView.findViewById(R.id.amount);
            date=itemView.findViewById(R.id.date);
            view=itemView.findViewById(R.id.view);
            delete=itemView.findViewById(R.id.delete);


        }
    }










}
