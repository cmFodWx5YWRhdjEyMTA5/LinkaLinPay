package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Orderlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productcategorymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Orderlistadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Kpays extends AppCompatActivity {

    TextView tv_url;
    EditText edit_url;
    Button update,share;
    ImageView logo;
    TextView businessname;

    private ProgressDialog progressDialog;
    UserShared psh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kpays);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.kpyas);
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


        tv_url = findViewById(R.id.tv_url);
        edit_url = findViewById(R.id.edit_url);
        update = findViewById(R.id.update);
        share = findViewById(R.id.share);
        psh=new UserShared(this);
        logo = findViewById(R.id.logo);
        businessname = findViewById(R.id.businessname);
        Intent getinfo = getIntent();
        //Log.d()

        try {
            JSONObject obj = new JSONObject(getinfo.getStringExtra("kpayobj"));


            Glide.with(this)
                    .load(obj.getString("seller_logo"))
                    .error(R.drawable.user)
                    .into(logo);
            tv_url.setText(obj.getString("store_url"));
            businessname.setText(obj.getString("business_name"));
            edit_url.setText(obj.getString("seller_temp_store_name"));



        } catch (JSONException e) {
            e.printStackTrace();
        }



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edit_url.getText().toString().trim().equals(""))
                {
                    edit_url.setError("please provide url");
                }
                else
                {
                    updateurl();
                }

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(Intent.EXTRA_TEXT, "Hi, \n" +
                        "I am sharing my store "+tv_url.getText().toString()+". You can click and access my services and products.\n" +
                        " Thank you");

                //sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.my_code) + " " + code);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.app_name)));

            }
        });



    }
    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(Kpays.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    private void updateurl() {

        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.sellerurledit ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("response>>",response);

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("success")) {
                                    stopProgress();
                                    showSnackBar(jsonObject.getString("msg"));
                                    tv_url.setText(jsonObject.getString("url"));



                                } else {

                                    stopProgress();
                                    showSnackBar(jsonObject.getString("msg"));


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
                                stopProgress();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sellerid", psh.getSellerid());
                    params.put("url", edit_url.getText().toString().trim());

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
            Toast.makeText(Kpays.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }





    }

    private void showSnackBar(String msg) {
        Toast.makeText(Kpays.this,msg,Toast.LENGTH_LONG).show();

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}