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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Influencerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddInfluencerActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText name,email,mobilenumber;
    Button btn_submit;
    String name_str,email_str,mobilenumber_str;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_influencer);
        xmlinit();
        xmlonclik();



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
                Addinfluencerapi();
            }
        });

    }

      private void Addinfluencerapi() {
        name_str=name.getText().toString();
        email_str=email.getText().toString();
        mobilenumber_str=mobilenumber.getText().toString();
         if(name_str.equals("")) {
             showSnackBar("Enter Name");

         }else if(email_str.equals("")){
             showSnackBar("Enter Email Address");
         } else if(mobilenumber_str.equals("")) {
         showSnackBar("Enter Mobile Number");

         }else{
           callapi();


         }


    }

    private void callapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Addinfluencer, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");

                        if (status.equals("success")) {
                            Toast.makeText(AddInfluencerActivity.this,msg,Toast.LENGTH_LONG).show();
                            stopProgress();
                            Intent i=new Intent(AddInfluencerActivity.this,InfuencerlistActivity.class);
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
                    params.put("name", name_str);
                    params.put("email", email_str);
                    params.put("mobile", mobilenumber_str);


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
            VolleySingleton.getInstance(AddInfluencerActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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
    Toast.makeText(AddInfluencerActivity.this,msg,Toast.LENGTH_LONG).show();

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(AddInfluencerActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }



    private void xmlinit() {
         toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.addinfluencer);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        mobilenumber=(EditText)findViewById(R.id.mobilenumber);
       btn_submit=(Button)findViewById(R.id.btn_submit);



    }
}