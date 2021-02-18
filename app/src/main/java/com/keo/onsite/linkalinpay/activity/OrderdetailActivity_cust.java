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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Custdetailmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Sellerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.OrderDetailAdapter;
import com.keo.onsite.linkalinpay.adapter.OrderDetailAdapter_cust;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderdetailActivity_cust extends AppCompatActivity {
         Intent mIntent;
         TextView date, orderid, status_tv,
            total_amnt,pyment_type,paymentaddress ,Shippigaddress;
    ImageView imageview;
    private ProgressDialog progressDialog;
    ArrayList<Custdetailmodelclass> custdetaillist;
    OrderDetailAdapter_cust orderdadapter;
    RecyclerView recyclerview;
    Spinner orderstatus_sp;
    String status;
    UserShared  psh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderdetail_cust);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.orderdetail);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        mIntent = getIntent();
         psh=new UserShared(this);
        xmlinit();
        callmyorderdetailapi();


    }

    private void callmyorderdetailapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.myorder_custdetail,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                custdetaillist = new ArrayList<Custdetailmodelclass>();
                                if (status.equals("success")) {
                                  JSONObject jobi=jsonObject.getJSONObject("myorder");
                                 //Toast.makeText(OrderdetailActivity_cust.this, msg, Toast.LENGTH_LONG).show();
                                    date.setText(jobi.getString("order_date"));
                                    orderid.setText("#"+jobi.getString("order_id"));
                                    status_tv.setText(jobi.getString("orderstatus"));
                                    total_amnt.setText(jobi.getString("currency")+""+jobi.getString("total_amount"));
                                    pyment_type.setText(jobi.getString("payment_type"));
                                    paymentaddress.setText(jobi.getString("billingaddress"));
                                    Shippigaddress.setText(jobi.getString("shippingaddress"));

                                    JSONArray jarray = jobi.getJSONArray("productdata");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Custdetailmodelclass slmc = new Custdetailmodelclass(jobj1.getString("product_name"),
                                                jobj1.getString("product_price"),
                                                jobj1.getString("product_offerprice"), jobj1.getString("product_image"),
                                                jobj1.getString("qty"), jobj1.getString("sbtotal"),
                                                jobj1.getString("ftotal"));
                                        custdetaillist.add(slmc);

                                    }
                                    stopProgress();
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
                    params.put("order_id",mIntent.getStringExtra("orderid") );
                    params.put("cust_id", psh.getCustomerid());


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

    private void setadapter() {
        orderdadapter = new OrderDetailAdapter_cust(OrderdetailActivity_cust.this, custdetaillist);
        recyclerview.setAdapter(orderdadapter);


    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }


    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(OrderdetailActivity_cust.this, ProgressDialog.THEME_HOLO_LIGHT);
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
    Toast.makeText(OrderdetailActivity_cust.this,msg,Toast.LENGTH_LONG).show();

    }

    private void xmlinit() {
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setNestedScrollingEnabled(true);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(),
                layoutManager.getOrientation());
        recyclerview.addItemDecoration(dividerItemDecoration);



        paymentaddress=(TextView)findViewById(R.id.paymentaddress);
        Shippigaddress=(TextView)findViewById(R.id.Shippigaddress);

        date=(TextView)findViewById(R.id.date);
        orderid=(TextView)findViewById(R.id.orderid);
        total_amnt=(TextView)findViewById(R.id.total);
        status_tv=(TextView)findViewById(R.id.status);
        pyment_type=(TextView)findViewById(R.id.payment_type);


    }
}