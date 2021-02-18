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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Parentcategorymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddInvoiceActivity extends AppCompatActivity {
     EditText customername,emailid,mobilenumber,payableamnt,desc;
      String customername_str,emailid_str,mobilenumber_str,payableamnt_str,desc_str;
       Spinner  currency;
      Button btn_submit;
       private ProgressDialog progressDialog;
       String item;
      UserShared psh;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_invoice);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.createquickinv);
        setSupportActionBar(toolbar);
          getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          // getSupportActionBar().setTitle("Donation List");
          toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
          toolbar.setNavigationOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  finish();
              }
          });
          psh=new UserShared(this);
         xmlinit();
         xmlonclik();


     }

    private void xmlonclik() {
    btn_submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Addinvoiceapi();
        }
    });


  }

    private void Addinvoiceapi() {
        customername_str=customername.getText().toString();
        emailid_str=emailid.getText().toString();
        mobilenumber_str=mobilenumber.getText().toString();
        payableamnt_str=payableamnt.getText().toString();
        desc_str=desc.getText().toString();

        if (customername_str.equals("")) {
            showSnackBar("Please Enter Customer Name");
        } else if (emailid_str.equals("")) {
            showSnackBar("Enter  Email Address");
        }else if (mobilenumber_str.equals("")) {
            showSnackBar("Enter  Mobile Number");
        }else if (payableamnt_str.equals("")) {
            showSnackBar("Enter  Amount");
        }else if (desc_str.equals("")) {
            showSnackBar("Write  Description");
        }

        else {
            Addinvoice();


        }








   }

    private void Addinvoice() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Addinvoiceapi , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            Toast.makeText(AddInvoiceActivity.this,msg,Toast.LENGTH_LONG).show();
                            stopProgress();
                            Intent i=new Intent(AddInvoiceActivity.this,InvoiceActivity.class);
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
                    params.put("seller_id", psh.getSellerid());
                    params.put("name", customername_str);
                    params.put("email", emailid_str);
                    params.put("mobile", mobilenumber_str);
                    params.put("currency", item);
                    params.put("amount", payableamnt_str);
                    params.put("desc", desc_str);


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
            VolleySingleton.getInstance(AddInvoiceActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
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
    Toast.makeText(AddInvoiceActivity.this,msg,Toast.LENGTH_LONG).show();

     }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(AddInvoiceActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

       private void xmlinit() {
        customername=(EditText)findViewById(R.id.customername);
        emailid=(EditText)findViewById(R.id.emailid);
        mobilenumber=(EditText)findViewById(R.id.mobilenumber);
        payableamnt=(EditText)findViewById(R.id.payableamnt);
        desc=(EditText)findViewById(R.id.desc);
        btn_submit=(Button)findViewById(R.id.btn_submit);
        currency=(Spinner)findViewById(R.id.currency);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("KD");
        categories.add("USD");
        categories.add("EGP");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        currency.setAdapter(dataAdapter);

       currency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });


    }


}