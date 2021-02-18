package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    TextView txt_sign_up;
    EditText edt_mobile,edt_password;
    Button btn_signin;
    TextView txt_register,forgot_password;
    String edt_mobile_str,pass_str;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    LinearLayout layout;
    private ProgressDialog progressDialog;
    RadioGroup radioGroup;
    RadioButton selectedRadioButton,seller,customer;
    //private String selectedType="";
    SharedPreferences prefs;
   Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs=getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);
        mIntent=getIntent();
        xmlinit();
        xmlonclik();


    }

    private void xmlonclik() {
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent=new Intent(LoginActivity.this,DashBoardActivity.class);
                //startActivity(intent);
                callloginapi();



            }
        });

        txt_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if( mIntent.getStringExtra("selectedType").equals("seller"))
                {

                    Intent intent=new Intent(LoginActivity.this,BusinessSignupActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent=new Intent(LoginActivity.this,ShopperActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        /*radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if(checkedId==R.id.seller){
                    selectedType = seller.getText().toString();

                }else if(checkedId==R.id.customer){
                    selectedType = customer.getText().toString();


                }

            }

        });
*/


    }

    private void callloginapi() {
        edt_mobile_str=edt_mobile.getText().toString();
        pass_str=edt_password.getText().toString();


        if (edt_mobile_str.equals("")) {
            showSnackBar("Please Enter Mobile Number/Email Address");

        } else if (pass_str.equals("")) {
            showSnackBar("Please Enter Password");
        }
        else {
            loginapi();


        }


    }

    private void loginapi() {
        if (VolleySingleton.getInstance(this).isConnected()){
            showProgress();
            StringRequest stringRequest=new StringRequest(Request.Method.POST,
                    Constants.loginURL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status=jobjMain.getString("status");
                        String msg=jobjMain.getString("msg");

                        if (status.equals("success")) {
                            stopProgress();
                            Translator t =  new Translator();
                            t.getTranslateService(LoginActivity.this);

                            Toast.makeText(LoginActivity.this,t.translate(LoginActivity.this,msg),Toast.LENGTH_LONG).show();

                            if(jobjMain.getString("type").equals("seller")) {
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(getString(R.string.shared_user_type), jobjMain.getString("type"));
                                editor.putString(getString(R.string.shared_seller_id), jobjMain.getString("seller_id"));
                                editor.putString(getString(R.string.shared_seller_name), jobjMain.getString("seller_name"));
                                editor.putString(getString(R.string.shared_seller_logo), jobjMain.getString("seller_logo"));

                                editor.putString(getString(R.string.shared_seller_mobile), jobjMain.getString("seller_mobile"));
                                editor.putString(getString(R.string.shared_seller_email), jobjMain.getString("seller_email"));
                                editor.putString(getString(R.string.shared_seller_businessname), jobjMain.getString("business_name"));
                                editor.putString(getString(R.string.shared_seller_instagramname), jobjMain.getString("instagram_name"));

                                editor.putBoolean(getString(R.string.shared_loggedin_status_seller), true);


                                editor.commit();
                                Intent i = new Intent(LoginActivity.this, DashBoardActivity.class);
                                startActivity(i);
                            }else if (jobjMain.getString("type").equals("customer")){
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString(getString(R.string.shared_user_type), jobjMain.getString("type"));
                                editor.putString(getString(R.string.shared_customer_id), jobjMain.getString("cust_id"));
                                editor.putString(getString(R.string.shared_customer_name), jobjMain.getString("cust_name"));
                                editor.putString(getString(R.string.shared_customer_email), jobjMain.getString("cust_email"));
                                editor.putString(getString(R.string.shared_customer_mobilenumber),jobjMain.getString("cust_mobile"));
                                editor.putString(getString(R.string.shared_customer_pic),jobjMain.getString("cust_profile"));


                                editor.putBoolean(getString(R.string.shared_loggedin_status_customer), true);
                                editor.commit();

                                Intent i = new Intent(LoginActivity.this, MainDashboardActivity.class);
                                startActivity(i);

                            }


                        }else{
                             stopProgress();
                            Translator t =  new Translator();
                            t.getTranslateService(LoginActivity.this);

                            Toast.makeText(LoginActivity.this,t.translate(LoginActivity.this,msg),Toast.LENGTH_LONG).show();
                                //showSnackBar(msg);
                            }

                    }catch (JSONException e) {
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
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("mobile", edt_mobile_str);
                    params.put("password", pass_str);
                    params.put("type", mIntent.getStringExtra("selectedType"));
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
            VolleySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }





    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(LoginActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
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
        Translator t =  new Translator();
        t.getTranslateService(LoginActivity.this);

        Toast.makeText(LoginActivity.this,t.translate(LoginActivity.this,msg),Toast.LENGTH_LONG).show();


    }


    private void xmlinit() {
        txt_sign_up=findViewById(R.id.txt_sign_up);
        edt_mobile=findViewById(R.id.edt_mobile);
        edt_password=findViewById(R.id.edt_password);
        btn_signin=findViewById(R.id.btn_signin);
       // radioGroup=(RadioGroup) findViewById(R.id.radioGroup);
       // seller=(RadioButton)findViewById(R.id.seller);
        //customer=(RadioButton)findViewById(R.id.customer);
    }

}