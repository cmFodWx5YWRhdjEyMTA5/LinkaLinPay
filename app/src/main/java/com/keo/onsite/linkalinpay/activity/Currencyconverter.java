package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Currencymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.CurrencyAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Currencyconverter extends AppCompatActivity {
        Spinner spinner;
    private ProgressDialog progressDialog;
   ArrayList<Currencymodelclass>currencylist;
     String selected;
    UserShared psh;
    TextView update;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currencyconverter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.changecurr);
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
        prefs=getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        xmlinit();
        callcurrencylistapi();


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savecurrency();

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected=currencylist.get(i).currencysymbol;
                sellerdetailapi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void callcurrencylistapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {

            showProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.currencylistapi ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                currencylist = new ArrayList<Currencymodelclass>();
                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("currency");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Currencymodelclass blmc = new Currencymodelclass(jobj1.getString("currencysymbol"),jobj1.getString("currencyname")                                              );
                                        currencylist.add(blmc);
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
                    //params.put("seller_id", psh.getSellerid());
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
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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
    Toast.makeText(this,msg,Toast.LENGTH_LONG).show();

        }

    private void stopProgress() {
     if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

   }

    private void setAdapter() {
    CurrencyAdapter cadapter=new CurrencyAdapter(this,currencylist,R.layout.catgry_item);
      spinner.setAdapter(cadapter);


  }



    private void savecurrency() {




        if (VolleySingleton.getInstance(this).isConnected()) {

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.sellercurrencyupdate ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String messages = jsonObject.getString("msg");
                                Toast.makeText(getApplicationContext(),messages,Toast.LENGTH_SHORT).show();
                                if (status.equals("success")) {
                                    JSONObject jobj = jsonObject.getJSONObject("seller");
                                       /*if(psh.getType().equals("seller")) {
                                           SharedPreferences.Editor editor = prefs.edit();
                                           editor.putString(getString(R.string.shared_seller_currency), jobj.getString("currency"));
                                           editor.putBoolean(getString(R.string.shared_loggedin_status_seller), true);
                                           editor.commit();
                                       }*/
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

                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sellerid", psh.getSellerid());
                    params.put("symbol", selected);
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
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }



    }

    private void sellerdetailapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {

            showProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Sellerdetailapi ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                   if (status.equals("success")) {
                                    JSONObject jobj = jsonObject.getJSONObject("seller");
                                       /*if(psh.getType().equals("seller")) {
                                           SharedPreferences.Editor editor = prefs.edit();
                                           editor.putString(getString(R.string.shared_seller_currency), jobj.getString("currency"));
                                           editor.putBoolean(getString(R.string.shared_loggedin_status_seller), true);
                                           editor.commit();
                                       }*/
                                       SharedPreferences.Editor editor = prefs.edit();
                                       editor.putString(getString(R.string.shared_seller_currency), selected);
                                       editor.putBoolean(getString(R.string.shared_loggedin_status_seller), true);
                                       editor.commit();

                                       stopProgress();
                                     //  Intent i=new Intent(Currencyconverter.this,ProductManagementActivity.class);
                                       //startActivity(i);
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
                    params.put("currency", selected);
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
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }



    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void xmlinit() {
    spinner=(Spinner)findViewById(R.id.spinner);
    update = findViewById(R.id.update);

    }
}