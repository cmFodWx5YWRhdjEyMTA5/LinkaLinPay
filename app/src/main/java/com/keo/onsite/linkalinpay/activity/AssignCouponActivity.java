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
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.CouponListmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Influencerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.adapter.Couponadapter;
import com.keo.onsite.linkalinpay.adapter.Couponlistadapter;
import com.keo.onsite.linkalinpay.adapter.Influenceradapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AssignCouponActivity extends AppCompatActivity {
      Spinner couponname,influencerlist;
      Toolbar toolbar;
    RecyclerView recycler_view;
    ArrayList<CouponListmodelclass> couponlist;
    private ProgressDialog progressDialog;
    Couponadapter cadapter;
      String selected_coupon,influencer_mail;
    ArrayList<Influencerlistmodelclass> influencerarraylist;
    Influenceradapter iadapter;

    Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_coupon);
        xmlinit();
        xmlonclik();
        couponlist();
       influencerlist();

      }

    private void couponlist() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Couponlist ,

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
                                                jobj1.getString("expirydate"),jobj1.getString("email"),jobj1.getString("status")
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
            Toast.makeText(AssignCouponActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
    Toast.makeText(AssignCouponActivity.this,msg,Toast.LENGTH_LONG).show();


      }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
      }

    private void setAdapter() {
          cadapter=new Couponadapter(AssignCouponActivity.this,couponlist,R.layout.catgry_item);
          couponname.setAdapter(cadapter);
          couponname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                  selected_coupon=couponlist.get(i).id;
              }

              @Override
              public void onNothingSelected(AdapterView<?> adapterView) {

              }
          });
    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(AssignCouponActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();



    }

    private void influencerlist() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Influencerlist, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        influencerarraylist = new ArrayList<>();

                        // String msg = jobjMain.getString("msg");

                        if (status.equals("success")) {
                            stopProgress();
                            JSONArray jarray = jobjMain.getJSONArray("influencer");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject jobj = jarray.getJSONObject(i);
                                Influencerlistmodelclass infc = new Influencerlistmodelclass(jobj.getString("id"),
                                        jobj.getString("seller_id"), jobj.getString("name"),
                                        jobj.getString("email"), jobj.getString("mobile"), jobj.getString("status"));
                                influencerarraylist.add(infc);
                            }
                            setadapter();

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
                    params.put("seller_id", "1");


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
            VolleySingleton.getInstance(AssignCouponActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }






    }

    private void setadapter() {
        iadapter = new Influenceradapter(AssignCouponActivity.this, influencerarraylist, R.layout.catgry_item);
        influencerlist.setAdapter(iadapter);
        influencerlist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                influencer_mail = influencerarraylist.get(i).name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

    private void xmlonclik() {
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Assigncouponapi();
            }
        });

    }

    private void Assigncouponapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Selleraddassign, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");

                        if (status.equals("success")) {
                        Toast.makeText(AssignCouponActivity.this,msg,Toast.LENGTH_LONG).show();
                         stopProgress();
                            Intent i=new Intent(AssignCouponActivity.this,AddInfluencerActivity.class);
                            startActivity(i);



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
                    params.put("seller_id", "1");
                    params.put("coupon", selected_coupon);
                    params.put("email", influencer_mail);
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
            VolleySingleton.getInstance(AssignCouponActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }





    }

        private void xmlinit() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.assigncoupon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        couponname=(Spinner)findViewById(R.id.couponname);
        influencerlist=(Spinner)findViewById(R.id.emailist);
        btn_submit=(Button)findViewById(R.id.btn_submit);
    }
}