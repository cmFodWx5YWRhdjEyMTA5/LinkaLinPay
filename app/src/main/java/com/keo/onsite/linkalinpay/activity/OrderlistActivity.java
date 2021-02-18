package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Orderlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Orderlistadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderlistActivity extends AppCompatActivity {
    RecyclerView orderlist;
    private ProgressDialog progressDialog;
    ArrayList<Orderlistmodelclass> orderlist_array;
    Orderlistadapter orderadapter;
    UserShared psh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderlist);
        Toolbar toolbar = findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.orderlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
           toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
           psh=new UserShared(this);
          xmlinit();
         orderlistapi();
    }

    private void orderlistapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Orderlistapi,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                orderlist_array = new ArrayList<Orderlistmodelclass>();

                                if (status.equals("success")) {
                                    stopProgress();

                                    JSONArray jarray = jsonObject.getJSONArray("orders");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Orderlistmodelclass flmc = new Orderlistmodelclass(
                                                jobj1.getString("oid"),
                                                jobj1.getString("product_id"),
                                                jobj1.getString("product_name"),
                                                jobj1.getString("product_price"),
                                                jobj1.getString("product_offerprice"),
                                                jobj1.getString("product_image"),
                                                jobj1.getString("sbtotal"),jobj1.getString("qty"),
                                                jobj1.getString("ftotal"),jobj1.getString("order_id"),
                                                jobj1.getString("total_amount"),jobj1.getString("orderstatus"),
                                                jobj1.getString("payment_type"),jobj1.getString("order_date")

                                        );



                                      orderlist_array.add(flmc);
                                    }
                                    setAdapter();



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
                    params.put("seller_id", psh.getSellerid());
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
            VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(OrderlistActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }




    private void setAdapter() {
    orderadapter=new Orderlistadapter(OrderlistActivity.this,orderlist_array);
    orderlist.setAdapter(orderadapter);



    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(OrderlistActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


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
    Toast.makeText(OrderlistActivity.this,msg,Toast.LENGTH_LONG).show();

    }

    private void xmlinit() {
    orderlist=(RecyclerView)findViewById(R.id.orderlist);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    orderlist.setLayoutManager(layoutManager);


    }
}