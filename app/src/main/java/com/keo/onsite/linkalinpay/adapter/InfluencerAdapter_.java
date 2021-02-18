package com.keo.onsite.linkalinpay.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.keo.onsite.linkalinpay.activity.AddInvoiceActivity;
import com.keo.onsite.linkalinpay.activity.CouponDetailActivity;
import com.keo.onsite.linkalinpay.activity.model.CouponListmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Influencerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfluencerAdapter_ extends RecyclerView.Adapter<InfluencerAdapter_.ViewHolder>{
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private ArrayList<Influencerlistmodelclass> productList;
    //getting the context and product list with constructor
    String couponid;
    String status;
    private ProgressDialog progressDialog;
    Translator t ;

    public InfluencerAdapter_(Context mCtx, ArrayList<Influencerlistmodelclass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;

        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public InfluencerAdapter_.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.influencer_item, null);
        return new InfluencerAdapter_.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfluencerAdapter_.ViewHolder holder, final int position) {
        couponid=productList.get(position).id;
        //binding the data with the viewholder views
        holder.name.setText(productList.get(position).name);
        holder.email.setText(productList.get(position).email);
        holder.mobilenumber.setText(productList.get(position).mobile);

       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               removeitem();
               remove(position);
           }
       });


    }

    private void remove(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());


    }

    private void removeitem() {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Sellerinfluencerdelete,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(mCtx,t.translate(mCtx,msg), Toast.LENGTH_LONG).show();
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
                    params.put("id", couponid);

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













    class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, email,mobilenumber;
        ImageView view;
        Button status_btn,status_inactive;





        public ViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            email = itemView.findViewById(R.id.email);
            mobilenumber=itemView.findViewById(R.id.mobilenumber);
            view=itemView.findViewById(R.id.view);


        }
    }



















}
