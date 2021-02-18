package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Custdetailmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Pricing;
import com.keo.onsite.linkalinpay.activity.other.Choice;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.OrderDetailAdapter_cust;
import com.keo.onsite.linkalinpay.adapter.PricingAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PricingActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    ArrayList<Pricing> pricelist;
    PricingAdapter priceadapter;
    RecyclerView pricing_recycler;
    UserShared  psh;
   // JSONObject map2, map;
    JSONObject map2 = new JSONObject();

    JSONObject map = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pricing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.pricing);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pricing_recycler = findViewById(R.id.pricing_recycler);
        pricing_recycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        pricing_recycler.setLayoutManager(linearLayoutManager);

        psh=new UserShared(this);
        pricingapi();






    }

    private void setadapter() {
        priceadapter = new PricingAdapter(PricingActivity.this, pricelist);
        pricing_recycler.setAdapter(priceadapter);
        priceadapter.setViewItemInterface(new Choice() {
            @Override
            public void onItemClick(int adapterPosition, String choice) {


                updateChoice(adapterPosition,choice);



            }
        });


    }

    private void updateChoice(int adapterPosition, String choice) {
        //pricelist.clear();
        //pricingapi();

        try {
            map.put("name",pricelist.get(adapterPosition).getName());
            map.put("value",choice);
            map2.put("0",map);

        } catch (JSONException e) {
            e.printStackTrace();
        }



        //update choice on clicking
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.sellertransactionpayment,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response>>>",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                pricelist = new ArrayList<Pricing>();
                                if (status.equals("success")) {
//                                    JSONObject jobi=jsonObject.getJSONObject("charge");
                                    Toast.makeText(PricingActivity.this, msg, Toast.LENGTH_LONG).show();
                                    //  JSONArray jarray = jobi.getJSONArray("productdata");



                                    stopProgress();
                                    finish();
                                    startActivity(new Intent(PricingActivity.this,PricingActivity.class));
//                                    Log.d("Response>>>",pricelist.get(0).getName().toString());

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
                    params.put("sellerid", psh.getSellerid());
                    params.put("method", map2.toString());
                    Log.d("map2>>>>",map2.toString());



                    //params.put("OrganizationId", "");
                    // params.put("radius", "5");


                    return params;
                }
                /*mIntent.getStringExtra("orderid")*/
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
            VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }














    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(PricingActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

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
        Toast.makeText(PricingActivity.this,msg,Toast.LENGTH_LONG).show();

    }


    private void pricingapi() {

        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.sellertransactioncharge,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("response>>>",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                pricelist = new ArrayList<Pricing>();
                                if (status.equals("success")) {
                                    JSONObject jobi=jsonObject.getJSONObject("charge");
                                    //Toast.makeText(PricingActivity.this, msg, Toast.LENGTH_LONG).show();
                                  //  JSONArray jarray = jobi.getJSONArray("productdata");
                                    for (int i = 0; i < jobi.length(); i++) {
                                        JSONObject jobj1 = jobi.getJSONObject(""+i);

                                        pricelist.add(new Pricing(jobj1.getString("name"),
                                                jobj1.getString("charge"),
                                                jobj1.getString("image"),
                                                jobj1.getString("pay")

                                                ));

                                    }
                                    stopProgress();
                                    Log.d("Response>>>",pricelist.get(0).getName().toString());
                                    setadapter();

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
                    params.put("sellerid", psh.getSellerid());


                    //params.put("OrganizationId", "");
                    // params.put("radius", "5");


                    return params;
                }
                /*mIntent.getStringExtra("orderid")*/
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
            VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }



    }
}