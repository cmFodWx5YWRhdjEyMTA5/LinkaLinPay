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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Currencymodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productcategorymodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.CurrencyAdapter;
import com.keo.onsite.linkalinpay.adapter.ProductlistAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductManagementActivity extends AppCompatActivity {
    Toolbar toolbar;
    FloatingActionButton fab;
    ArrayList<Productlistmodelclass> productlist;
    public ProductlistAdapter pladapter;
    private ProgressDialog progressDialog;
    RecyclerView recycler_view;
    UserShared psh;
    Spinner currencysetting;
    ArrayList<Currencymodelclass> currencylist;
    String selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_management);
        psh = new UserShared(this);
        xmlinit();
        xmlonclik();
        callapi();
        //currencysettingapi();


    }

    private void currencysettingapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {

            showProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.currencylistapi,

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
                                        Currencymodelclass blmc = new Currencymodelclass(jobj1.getString("currencysymbol"), jobj1.getString("currencyname"));
                                        currencylist.add(blmc);
                                    }
                                    setAdapter_currency();
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

    private void setAdapter_currency() {
        CurrencyAdapter cadapter = new CurrencyAdapter(this, currencylist, R.layout.catgry_item);
        currencysetting.setAdapter(cadapter);

        /*currencysetting.post(new Runnable() {
            @Override public void run() {
                currencysetting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Only called when the user changes the selection
                        selected=currencylist.get(position).currencysymbol;
                       //  callapi_setting();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });

*/


    }

    /*private void callapi_setting() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Sellerproductlist ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                productlist = new ArrayList<Productlistmodelclass>();

                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("product");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Productlistmodelclass blmc = new Productlistmodelclass(jobj1.getString("product_id"),
                                                jobj1.getString("product_sellerid"),
                                                jobj1.getString("product_cat"),
                                                jobj1.getString("product_name"),
                                                jobj1.getString("product_link"),
                                                jobj1.getString("product_qty"),
                                                jobj1.getString("product_desc"),
                                                jobj1.getString("product_status"),
                                                jobj1.getString("product_currency"),
                                                jobj1.getString("product_price"),
                                                jobj1.getString("product_offerprice"),
                                                jobj1.getString("product_image"),
                                                jobj1.getString("product_image1"),
                                                jobj1.getString("product_image2"),
                                                jobj1.getString("product_image3"),
                                                jobj1.getString("product_date"),
                                                jobj1.getString("product_ip"),
                                                jobj1.getString("product_featured"),
                                                jobj1.getString("add_home"),
                                                jobj1.getString("category_name")
                                        );
                                        productlist.add(blmc);
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
                    params.put("currency", selected);





                    // params.put("radius", "5");
                    *//*psh.getSellerid()*//*

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
            Toast.makeText(ProductManagementActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }
*/
    private void callapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Sellerproductlist,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                productlist = new ArrayList<Productlistmodelclass>();

                                if(status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("product");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Productlistmodelclass blmc = new Productlistmodelclass(jobj1.getString("product_id"),
                                                jobj1.getString("product_sellerid"),
                                                jobj1.getString("product_cat"),
                                                jobj1.getString("product_name"),
                                                jobj1.getString("product_link"),
                                                jobj1.getString("product_qty"),
                                                jobj1.getString("product_desc"),
                                                jobj1.getString("product_status"),
                                                jobj1.getString("product_currency"),
                                                jobj1.getString("product_price"),
                                                jobj1.getString("product_offerprice"),
                                                jobj1.getString("product_image"),
                                                jobj1.getString("product_image1"),
                                                jobj1.getString("product_image2"),
                                                jobj1.getString("product_image3"),
                                                jobj1.getString("product_date"),
                                                jobj1.getString("product_ip"),
                                                jobj1.getString("product_featured"),
                                                jobj1.getString("add_home"),
                                                jobj1.getString("category_name")
                                        );
                                        productlist.add(blmc);
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
                    params.put("currency", psh.getCurrencysymbol());


                    // params.put("radius", "5");
                    /*psh.getSellerid()*/

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
            Toast.makeText(ProductManagementActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(ProductManagementActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setAdapter() {
        pladapter = new ProductlistAdapter(ProductManagementActivity.this, productlist);
        recycler_view.setAdapter(pladapter);


    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(ProductManagementActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    private void xmlonclik() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.addproduct, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i = new Intent(ProductManagementActivity.this, AddProductactivity.class);
                startActivity(i);
            }
        });

    }

    private void xmlinit() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.productmanagement);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));

        //currencysetting=(Spinner)findViewById(R.id.currencysetting);
        fab = findViewById(R.id.fab);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
        //GridLayoutManager mGridLayoutManager = new GridLayoutManager(ProductManagementActivity.this, 2);
        //recycler_view.setLayoutManager(mGridLayoutManager);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        finish();
    }
}