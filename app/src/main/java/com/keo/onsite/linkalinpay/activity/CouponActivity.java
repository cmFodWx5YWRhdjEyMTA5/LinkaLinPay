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
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.CouponListmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.adapter.Bannerlistadapter;
import com.keo.onsite.linkalinpay.adapter.Couponlistadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CouponActivity extends AppCompatActivity {
    RecyclerView recycler_view;
    ArrayList<CouponListmodelclass> couponlist;
    private ProgressDialog progressDialog;
    Couponlistadapter cadapter;
    Toolbar toolbar;
    //FloatingActionButton fab;
    TextView addcoupon;
    ImageView back_arrow;
    // ArrayList<CouponListmodelclass>couponarraylist;
    private LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.couponlist);
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

        xmlinit();
        xmlonclik();
        callcouponlistapi();

    }

    private void callcouponlistapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Couponlist,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                couponlist = new ArrayList<CouponListmodelclass>();

                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("coupon");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        CouponListmodelclass blmc = new CouponListmodelclass(jobj1.getString("id"),
                                                jobj1.getString("seller_id"),
                                                jobj1.getString("name"),
                                                jobj1.getString("code"),
                                                jobj1.getString("type"),
                                                jobj1.getString("amount"),
                                                jobj1.getString("limit"),
                                                jobj1.getString("expirydate"), jobj1.getString("email"), jobj1.getString("status")
                                        );
                                        couponlist.add(blmc);
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
                    params.put("seller_id", "1");
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
            Toast.makeText(CouponActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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

    private void setAdapter() {
        Couponlistadapter cadapter = new Couponlistadapter(CouponActivity.this, couponlist);
        recycler_view.setAdapter(cadapter);

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showSnackBar(String msg) {
        Toast.makeText(CouponActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(CouponActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    private void xmlonclik() {
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
        addcoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CouponActivity.this,AddcouponActivity.class);
                startActivity(i);
            }
        });
//        back_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });




    }

    private void xmlinit() {
      //  back_arrow=(ImageView)findViewById(R.id.back_arrow);
        addcoupon=(TextView)findViewById(R.id.addcoupon);
        //toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("Coupon List");
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        //toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        //fab = findViewById(R.id.fab);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        // recycler_view.setLayoutManager(layoutManager);
       /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_view.getContext(),
                  layoutManager.getOrientation());
          recycler_view.addItemDecoration(dividerItemDecoration);
*/
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_view.setLayoutManager(layoutManager);
        Recyclerviewmargin itemdecorator = new Recyclerviewmargin(20);
        recycler_view.addItemDecoration(itemdecorator);
       /*recycler_view.addItemDecoration(new Recyclerviewmargin(this));
          layoutManager = new LinearLayoutManager(CouponActivity.this);
          recycler_view.setLayoutManager(layoutManager);
*/
    }
}