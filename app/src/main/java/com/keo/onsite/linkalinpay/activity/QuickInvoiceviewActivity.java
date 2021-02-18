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

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.CustomSpinnerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuickInvoiceviewActivity extends AppCompatActivity {
       TextView date,totalamnt,ordernumber,orderstatus,invoicename,mobilenumber,email,count;
       Intent mIntent;
       ImageView img;
       Spinner statusupdate;
       String status_str;
       String inv_id;
     final String []categories={"Pending", "Cancel", "Failed","Sucess"};
       UserShared psh;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_invoiceview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mIntent=getIntent();
       inv_id=mIntent.getStringExtra("invid");
       psh=new UserShared(this);
        xmlinit();
        xmlonclik();



       }

       private void callstatusapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.sellerupdatequickpay , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            Toast.makeText(QuickInvoiceviewActivity.this,msg,Toast.LENGTH_LONG).show();
                            stopProgress();
                            orderstatus.setText(status_str);

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
                    params.put("inv_id", mIntent.getStringExtra("invid"));
                    params.put("inv_status", status_str);


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
            VolleySingleton.getInstance(QuickInvoiceviewActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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
    Toast.makeText(QuickInvoiceviewActivity.this,msg,Toast.LENGTH_LONG).show();

       }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(QuickInvoiceviewActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    private void xmlonclik() {
    img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

  }

    private void xmlinit() {
     statusupdate=(Spinner)findViewById(R.id.statusupdate);
        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Pending");
        categories.add("Cancel");
        categories.add("Failed");
        categories.add("Success");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusupdate.setAdapter(dataAdapter);

          statusupdate.post(new Runnable() {
            @Override public void run() {
                statusupdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Only called when the user changes the selection
                        status_str = parent.getItemAtPosition(position).toString();

                        callstatusapi();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });



        count=findViewById(R.id.count);
       count.setText(mIntent.getStringExtra("totrecords"));
       img=(ImageView)findViewById(R.id.img);
     date=findViewById(R.id.date);
     date.setText(mIntent.getStringExtra("orderdate"));
     totalamnt=findViewById(R.id.totalamnt);
     totalamnt.setText(psh.getCurrencysymbol()+" "+mIntent.getStringExtra("tatalamnt"));
     ordernumber=findViewById(R.id.ordernumber);
     ordernumber.setText(mIntent.getStringExtra("ordernumber"));
     orderstatus=findViewById(R.id.orderstatus);
    orderstatus.setText(mIntent.getStringExtra("orderstatus"));
     invoicename=findViewById(R.id.invoicename);
    invoicename.setText(mIntent.getStringExtra("personname"));
     mobilenumber=findViewById(R.id.mobilenumber);
     mobilenumber.setText(mIntent.getStringExtra("phonenumber"));
    email=findViewById(R.id.email);
    email.setText(mIntent.getStringExtra("email"));

    }
}