package com.keo.onsite.linkalinpay.activity;

import android.app.DatePickerDialog;
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

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Influencerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.adapter.Influenceradapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddcouponActivity extends AppCompatActivity {
    EditText couponname, couponcode, limit, discountamount, expirydate;
    Spinner influenceremail, discounttype;
    Toolbar toolbar;
    Button btn_submit;
    String couponname_str, couponcode_str, limit_str, discountamount_str, expirydate_str, selected, type_str;
    private ProgressDialog progressDialog;
    ArrayList<Influencerlistmodelclass> influencerarraylist;
    Influenceradapter iadapter;
    DatePickerDialog picker;
    String[] type = {"Flat Discount", "Percentage"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcoupon);
        xmlinit();
        xmlonclik();
        influencerlistapi();
    }

    private void influencerlistapi() {
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
                        //Toast.makeText(context, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                        showSnackBar(getResources().getString(R.string.nointernet));
                    } else if (error instanceof ParseError) {

                        showSnackBar(getResources().getString(R.string.nointernet));
                    } else if (error instanceof NoConnectionError) {

                        showSnackBar(getResources().getString(R.string.nointernet));
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
            VolleySingleton.getInstance(AddcouponActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }


    }

    private void setadapter() {
        iadapter = new Influenceradapter(AddcouponActivity.this, influencerarraylist, R.layout.catgry_item);
        influenceremail.setAdapter(iadapter);
        influenceremail.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected = influencerarraylist.get(i).name;
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
                Addcouponapi();
            }
        });

        expirydate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(AddcouponActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                expirydate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                                //  expirydate_str=expirydate.getText().toString();
                                expirydate_str = (year + dayOfMonth + "/" + (monthOfYear + 1));

                            }
                        }, year, month, day);
                picker.show();


            }


        });

    }

    private void Addcouponapi() {
        couponname_str = couponname.getText().toString();
        couponcode_str = couponcode.getText().toString();
        limit_str = limit.getText().toString();
        discountamount_str = discountamount.getText().toString();
        expirydate_str = expirydate.getText().toString();

        if (couponname_str.equals("")) {
            showSnackBar("Please Enter Coupon Name");
        } else if (couponcode_str.equals("")) {
            showSnackBar("Enter  Coupon Code");
        } else if (limit_str.equals("")) {
            showSnackBar("Enter  Limit");
        } else if (discountamount_str.equals("")) {
            showSnackBar("Enter Discount  Amount");
        } else if (expirydate_str.equals("")) {
            showSnackBar("Enter Expiry Date");
        } else {
            AddCouponapi();


        }


    }

    private void AddCouponapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.AddCoupon, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            Toast.makeText(AddcouponActivity.this, msg, Toast.LENGTH_LONG).show();
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
                        //Toast.makeText(context, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                        showSnackBar(getResources().getString(R.string.nointernet));
                    } else if (error instanceof ParseError) {

                        showSnackBar(getResources().getString(R.string.nointernet));
                    } else if (error instanceof NoConnectionError) {

                        showSnackBar(getResources().getString(R.string.nointernet));
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
                    params.put("name", couponname_str);
                    params.put("code", couponcode_str);
                    params.put("type", type_str);
                    params.put("amount", discountamount_str);
                    params.put("limit", limit_str);
                    params.put("email", selected);
                    params.put("expirydate", expirydate_str);


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
            VolleySingleton.getInstance(AddcouponActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
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

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(AddcouponActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    private void showSnackBar(String msg) {
        Toast.makeText(AddcouponActivity.this, msg, Toast.LENGTH_LONG).show();

    }

    private void xmlinit() {


        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.addcoupon);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        couponname = (EditText) findViewById(R.id.couponname);
        couponcode = (EditText) findViewById(R.id.couponcode);
        limit = (EditText) findViewById(R.id.limit);
        discountamount = (EditText) findViewById(R.id.discountamount);
        discounttype = (Spinner) findViewById(R.id.discounttype);
        expirydate = (EditText) findViewById(R.id.expirydate);
        influenceremail = (Spinner) findViewById(R.id.influenceremail);
        btn_submit = (Button) findViewById(R.id.btn_submit);
//Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, type);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        discounttype.setAdapter(aa);

        discounttype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                type_str = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}