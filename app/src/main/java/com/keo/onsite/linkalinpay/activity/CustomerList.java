package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.keo.onsite.linkalinpay.activity.model.Customermodel;
import com.keo.onsite.linkalinpay.activity.model.Invoicelist;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Invlistadapter;
import com.keo.onsite.linkalinpay.adapter.custlistadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CustomerList extends AppCompatActivity {

    //    TextView addinvoice;
    RecyclerView recyclerview;
    ArrayList<Customermodel> Customerlist;
    private ProgressDialog progressDialog;
    UserShared psh;
    ImageView b;
    String totrecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_list);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.customerlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        psh=new UserShared(this);
        xmlinit();
        xmlonclik();
        CustomerListapi();

    }


    private void CustomerListapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.customerlist,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                Customerlist = new ArrayList<Customermodel>();
                               // totrecord=jsonObject.getString("totalrecords");
                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("customer");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);


                                        Customermodel blmc = new Customermodel(
                                                jobj1.getString("cust_id"),
                                                jobj1.getString("cust_name"),
                                                jobj1.getString("cust_mobile"),
                                                jobj1.getString("cust_email"),
                                                jobj1.getString("cust_profile"));
                                        Customerlist.add(blmc);
                                    }
                                    setAdapter();
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
                    params.put("seller_id", psh.getSellerid());

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
            Toast.makeText(CustomerList.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(CustomerList.this, msg, Toast.LENGTH_LONG).show();
    }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setAdapter() {
        custlistadapter custadapter = new custlistadapter(CustomerList.this, Customerlist);
        recyclerview.setAdapter(custadapter);
    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(CustomerList.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void xmlonclik() {
//        addinvoice.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(CustomerList.this, AddCustomerList.class);
//                startActivity(i);
//
//            }
//        });

    }

    private void xmlinit() {
//        b=(ImageView)findViewById(R.id.b);

        // addinvoice = (TextView) findViewById(R.id.addinvoice);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        //  LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // recyclerview.setLayoutManager(layoutManager);
           /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerview.getContext(),
                   layoutManager.getOrientation());
           recyclerview.addItemDecoration(dividerItemDecoration);
*/
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerview.setLayoutManager(layoutManager);
        Recyclerviewmargin itemdecorator=new Recyclerviewmargin(20);
        recyclerview.addItemDecoration(itemdecorator);
    }
}