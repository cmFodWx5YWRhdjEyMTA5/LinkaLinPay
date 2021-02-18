package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ShopperActivity extends AppCompatActivity {

    TextView txt_sign_in;
    EditText edt_fullname,edt_email,edt_mobile,edt_password,edt_c_password,edt_address;
    Button btn_sign_up;
    String fullname_str,emailaddress_str,mobilenumber_str,password_str,conpass_str;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private ProgressDialog progressDialog;
    Intent mIntent;
    String businesstype_str,businessname_str,instagramname_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopper);
        xmlinit();
        xmlonclik();
        

    }

    private void xmlonclik() {
        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ShopperActivity.this,LoginActivity.class);
                intent.putExtra("selectedType","customer");

                startActivity(intent);
            }
        });
        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname_str = edt_fullname.getText().toString();
                emailaddress_str = edt_email.getText().toString();
                mobilenumber_str = edt_mobile.getText().toString();
                password_str=edt_password.getText().toString();
                conpass_str=edt_c_password.getText().toString();
                if (fullname_str.equals("")) {
                    showSnackBar("Please Enter Full Name");
                } else if (emailaddress_str.equals("")) {
                    showSnackBar("Please Enter Valid Email");

                } else if (!emailaddress_str.matches(emailPattern)) {
                    showSnackBar("Please enter a valid email id");
                }else if (password_str.equals("")) {
                    showSnackBar("Please enter password");
                }else if (conpass_str.equals("")) {
                    showSnackBar("Confirm Your password");
                }else if (!password_str.equals(conpass_str)) {
                    showSnackBar("Password does not match");
                }
                else if (edt_address.getText().toString().trim().equals("")) {
                    showSnackBar("Please provide address");
                }
                else {
                    customerapi();


                }

            }
        });





    }

    private void customerapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.register_shopper_URL , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");


                        Translator t =  new Translator();
                        t.getTranslateService(ShopperActivity.this);


                        if (status.equals("success"))
                            Toast.makeText(ShopperActivity.this,t.translate(ShopperActivity.this,msg),Toast.LENGTH_LONG).show();
                        stopProgress();
                             Intent i = new Intent(ShopperActivity.this, MainDashboardActivity.class);
                             startActivity(i);


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
                    params.put("email", emailaddress_str);
                    params.put("mobile", mobilenumber_str);
                    params.put("password", password_str);
                    params.put("name", fullname_str);
                    params.put("address", edt_address.getText().toString());

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
            VolleySingleton.getInstance(ShopperActivity.this).addToRequestQueue(stringRequest);
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

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(ShopperActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }


    private void showSnackBar(String msg) {
    Toast.makeText(ShopperActivity.this,msg,Toast.LENGTH_LONG).show();

    }

    private void xmlinit() {
        txt_sign_in=findViewById(R.id.txt_sign_in);
        edt_fullname=(EditText)findViewById(R.id.edt_fullname);
        edt_email=(EditText)findViewById(R.id.edt_email);
        edt_mobile=(EditText)findViewById(R.id.edt_mobile);
        edt_password=(EditText)findViewById(R.id.edt_c_password);
        edt_c_password=(EditText)findViewById(R.id.edt_c_password);
        btn_sign_up=(Button) findViewById(R.id.btn_sign_up);
        edt_address = findViewById(R.id.edt_address);


    }
}