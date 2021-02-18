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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.model.cartlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Cartadapter;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CartlistActivity extends AppCompatActivity {

    ConnectionDetector connection;
    private ProgressDialog progressDialog;
    private MultipartEntity reqEntity;
    UserShared psh;
    ArrayList<cartlistmodelclass> demolist;
    public static double total = 0.0;
    TextView subtotal, update_tv, checkout, total_tv;
    ImageView backk_img;
    String productid, cartid, quantity_str, sellerid;
    RecyclerView recyclerView;
    Intent mintent;
    TextView checkoutt;
    String tot, couponcode_str;
    EditText couponcode;
    Button submit_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartlist);
        psh = new UserShared(this);
        mintent = getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.cartlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total = 0.0;
                finish();
            }
        });
        xmlinit();
        xmlonclick();

        cartlistapi();
    }

    private void xmlonclick() {
        checkoutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(total>1){
                  Intent i = new Intent(CartlistActivity.this, CheckoutActivity.class);
                  i.putExtra("sellerid", sellerid);

                  startActivity(i);

              }else if(total<0){
                 //Toast.makeText(CartlistActivity.this,"Thre is no item in the cart",Toast.LENGTH_LONG).show();
                clearcarapi();


              }else{
                  Toast.makeText(CartlistActivity.this,"Thre is no item in the cart",Toast.LENGTH_LONG).show();
              }


            }
        });

        submit_str.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponcodeapi();
            }
        });

    }

    private void clearcarapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Clearartpi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(CartlistActivity.this, msg, Toast.LENGTH_LONG).show();

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

                                //Toast.makeText(context, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                                showSnackBar(getResources().getString(R.string.nointernet));
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
                    params.put("cust_id", psh.getCustomerid());
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

    private void couponcodeapi() {
        couponcode_str = couponcode.getText().toString();
        if (couponcode_str.equals("")) {
            showSnackBar("Please Enter Coupon code");
        } else {
            Couponcodeapi();


        }

    }

    private void Couponcodeapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Couponapplyoncart, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                       //String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            JSONObject  jobj=jobjMain.getJSONObject("coupon");

                            //Toast.makeText(CartlistActivity.this, msg, Toast.LENGTH_LONG).show();
                            total_tv.setText(jobj.getString("totalamount"));

                            stopProgress();
                            // Intent i=new Intent(AddInvoiceActivity.this,InvoiceActivity.class);
                            //startActivity(i);


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
                    params.put("cust_id", psh.getCustomerid());
                    params.put("currency", "$");
                    params.put("totalamount", tot);
                    params.put("code", couponcode_str);

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
            VolleySingleton.getInstance(CartlistActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void cartlistapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Cartlistapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                demolist = new ArrayList<>();
                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("cartdata");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj = jarray.getJSONObject(i);
                                        cartlistmodelclass clms = new cartlistmodelclass(jobj.getString("cart_id"),
                                                jobj.getString("cart_token"), jobj.getString("cart_sellerid"), jobj.getString("cart_cust"),
                                                jobj.getString("cart_pid"), jobj.getString("cart_qty"), jobj.getString("product_name"), jobj.getString("product_currency"),
                                                jobj.getString("product_price"), jobj.getString("product_offerprice"), jobj.getString("product_image"));
                                        demolist.add(clms);
                                        //sellerid=jobj.getString("cart_sellerid");
                                        total = total + (Double.parseDouble(demolist.get(i).product_offerprice) * Double.parseDouble(demolist.get(i).cart_qty));
                                        tot = new Double(total).toString();
                                    }
                                    setadapter();
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
                    // params.put("seller_id", mintent.getStringExtra("sellerid"));
                    params.put("cust_id", psh.getCustomerid());
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
        Cartadapter cadapter = new Cartadapter(CartlistActivity.this, demolist, subtotal, total_tv, tot);
        recyclerView.setAdapter(cadapter);
        subtotal.setText(String.valueOf(total).format("%.2f", total));
        total_tv.setText(String.valueOf(total).format("%.2f", total));
        cadapter.notifyDataSetChanged();

    }


    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
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

    private void showSnackBar(String errors) {
        Toast.makeText(CartlistActivity.this, errors, Toast.LENGTH_LONG).show();
    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


    }

    private void xmlinit() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setNestedScrollingEnabled(true);
        checkoutt = (TextView) findViewById(R.id.checkoutt);
        subtotal = (TextView) findViewById(R.id.subtotal);
        //backk_img = (ImageView) findViewById(R.id.backk);
        total_tv = (TextView) findViewById(R.id.total);
        couponcode = (EditText) findViewById(R.id.couponcode);
        // update_tv = (TextView) findViewById(R.id.checkout);
        submit_str = (Button) findViewById(R.id.submit);

    }

}