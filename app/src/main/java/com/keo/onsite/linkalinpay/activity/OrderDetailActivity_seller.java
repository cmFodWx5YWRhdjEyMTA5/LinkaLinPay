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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Sellerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.CustomSpinnerAdapter;
import com.keo.onsite.linkalinpay.adapter.OrderDetailAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderDetailActivity_seller extends AppCompatActivity {
     TextView date, orderid, status_tv,
             total_amnt,pyment_type,orderstatus ;
    ImageView imageview;
    private ProgressDialog progressDialog;
    Intent mIntent;
    ArrayList<Sellerlistmodelclass> orderdetaillist;
    OrderDetailAdapter orderdadapter;
    RecyclerView recyclerview;
    Spinner orderstatus_sp;
    String status_;
    String defaultTextForSpinner = "Update Status ";
     UserShared psh;

  final String []categories={"Select Status", "Pending","Rejected","Processed","Shipped","Delivered","Completed"};

    /*List<String> categories = new ArrayList<String>();
        categories.add("Pending");
        categories.add("Accept");
        categories.add("Reject");
        categories.add("Processing");
        categories.add("Shipped");
        categories.add("Delivered");
        categories.add("Complete");
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail_new);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
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
        calldetailapi();
    }

    private void calldetailapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Orderdetailapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                orderdetaillist = new ArrayList<Sellerlistmodelclass>();
                                if (status.equals("success")) {
                                    Toast.makeText(OrderDetailActivity_seller.this, msg, Toast.LENGTH_LONG).show();
                                    stopProgress();
                                    JSONArray jarray = jsonObject.getJSONArray("orderdetail");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj = jarray.getJSONObject(i);
                                        Sellerlistmodelclass slmc = new Sellerlistmodelclass(jobj.getString("product_id"),
                                                jobj.getString("product_name"), jobj.getString("product_price"),
                                                jobj.getString("product_offerprice"),
                                                jobj.getString("product_image"), jobj.getString("sbtotal"),
                                                jobj.getString("qty"), jobj.getString("ftotal"),
                                                jobj.getString("order_id"),
                                                jobj.getString("total_amount"), jobj.getString("orderstatus"),
                                                jobj.getString("payment_type"), jobj.getString("order_date"));
                                                orderdetaillist.add(slmc);
                                                date.setText(jobj.getString("order_date"));
                                                orderid.setText("#"+jobj.getString("order_id"));
                                                orderstatus.setText(jobj.getString("orderstatus"));
                                                total_amnt.setText(psh.getCurrencysymbol()+" "+jobj.getString("total_amount"));
                                                pyment_type.setText(jobj.getString("payment_type"));


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
                    params.put("order_id", mIntent.getStringExtra("orderid"));


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
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setadapter() {
        orderdadapter = new OrderDetailAdapter(OrderDetailActivity_seller.this, orderdetaillist);
        recyclerview.setAdapter(orderdadapter);
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
        Toast.makeText(OrderDetailActivity_seller.this, msg, Toast.LENGTH_LONG).show();

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(OrderDetailActivity_seller.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


       private void xmlinit() {
       orderstatus_sp=(Spinner)findViewById(R.id.status);
        orderstatus=(TextView)findViewById(R.id.orderstatus);

        // Spinner Drop down elements
      // Creating adapter for spinner
       // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.contact_spinner_row_nothing_selected, categories);
       // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       // orderstatus_sp.setAdapter(dataAdapter);
        orderstatus_sp.setAdapter(new CustomSpinnerAdapter(this, R.layout.contact_spinner_row_nothing_selected, categories ));

        orderstatus_sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  if(i>0){
                      status_=adapterView.getItemAtPosition(i).toString();
                      callstatusapi();

                  }else{


                  }



              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
          });




        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);
       recyclerview.setNestedScrollingEnabled(true);

        date=(TextView)findViewById(R.id.date);
        orderid=(TextView)findViewById(R.id.orderid);
                total_amnt=(TextView)findViewById(R.id.total);
              pyment_type=(TextView)findViewById(R.id.payment_type);



    }

    private void callstatusapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.sellerorderstatus , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            Toast.makeText(OrderDetailActivity_seller.this,msg,Toast.LENGTH_LONG).show();
                            stopProgress();
                            orderstatus.setText(status_);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("order_id", mIntent.getStringExtra("orderid"));
                    params.put("orderstatus", status_);


                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    // headers.put(Constants.authKEY, Constants.authValue);
                    // headers.put("header2", "header2");

                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(OrderDetailActivity_seller.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }






    }
}