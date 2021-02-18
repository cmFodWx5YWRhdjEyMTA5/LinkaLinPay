package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.keo.onsite.linkalinpay.activity.model.Countrymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.adapter.CountryAdapter;
import com.keo.onsite.linkalinpay.adapter.Countryadapter_new;
import com.keo.onsite.linkalinpay.adapter.StateAdapter;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BusinessSignupActivity extends AppCompatActivity {
    ArrayList<Countrymodelclass>countryarrlist;
    TextView txt_sign_in;
    EditText edt_businessname,edt_insta_name;
    Button btn_sign_up,verify;
    String business_type_str,businessname_str,instagramname_str;
    private ProgressDialog progressDialog;
    Spinner countrylist;
    Countryadapter_new cadapter;
    String selected_country;
    String Select_country_id;
    String insta_logo = "";
    EditText edt_fullname,edt_email,edt_mobile,edt_password,edt_c_password;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Spinner dropdown;

    LinearLayout businesstype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_signup);
        xmlinit();
        xmlonclik();
        Countrylistapi();



    }

    private void Countrylistapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.allcountry, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    stopProgress();
                    Log.d("COUNTRY>>>",response);
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        countryarrlist = new ArrayList<>();
                        //String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            //Toast.makeText(CheckoutActivity.this,msg,Toast.LENGTH_LONG).show();
                            JSONArray jarray = jobjMain.getJSONArray("country");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject jobj = jarray.getJSONObject(i);
                                Countrymodelclass cmc = new Countrymodelclass(jobj.getString("name"),jobj.getString("id"));
                                countryarrlist.add(cmc);
                                stopProgress();
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
                    //params.put("seller_id", "1");
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
            VolleySingleton.getInstance(BusinessSignupActivity.this).addToRequestQueue(stringRequest);
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
    Toast.makeText(BusinessSignupActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    private void setadapter() {
        cadapter = new Countryadapter_new(BusinessSignupActivity.this, countryarrlist, R.layout.catgry_item);
        countrylist.setAdapter(cadapter);
    //    countrylist.setAdapter(cadapter);

        countrylist.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0)
                {
                    selected_country = countryarrlist.get(i-1).name;
                    Select_country_id = countryarrlist.get(i-1).id;

                }
                else {
                    selected_country = countryarrlist.get(i).name;
                    Select_country_id = countryarrlist.get(i).id;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

       private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
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
    txt_sign_in=findViewById(R.id.txt_sign_in);
    //business_type=findViewById(R.id.business_type);
    edt_businessname=findViewById(R.id.edt_businessname);
    edt_insta_name=findViewById(R.id.edt_insta_name);
    btn_sign_up=findViewById(R.id.next);
        verify = findViewById(R.id.verify);
    countrylist=(Spinner)findViewById(R.id.countrylist);


        Toolbar toolbar = findViewById(R.id.toolbar);
        // setSupportActionBar(toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            onBackPressed();
            }
        });

        businesstype = findViewById(R.id.businesstype);
        edt_fullname=(EditText)findViewById(R.id.edt_fullname);
        edt_email=(EditText)findViewById(R.id.edt_email);
        edt_mobile=(EditText)findViewById(R.id.edt_mobile);
        edt_password=(EditText)findViewById(R.id.edt_c_password);
        edt_c_password=(EditText)findViewById(R.id.edt_c_password);
         dropdown = findViewById(R.id.spinner1);

        String[] items = new String[]{"Traders", "Services", "Real Estate"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        dropdown.setAdapter(adapter);

    }

    private void xmlonclik() {




        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyinsta();
            }
        });

        btn_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (edt_fullname.getText().toString().trim().equals("")) {
                    showSnackBar("Please Enter Full Name");
                } else if (edt_email.getText().toString().trim().equals("")) {
                    showSnackBar("Please Enter Valid Email");

                } else if (!edt_email.getText().toString().trim().matches(emailPattern)) {
                    showSnackBar("Please enter a valid email id");
                }else if (edt_password.getText().toString().trim().equals("")) {
                    showSnackBar("Please enter password");
                }else if (edt_c_password.getText().toString().trim().equals("")) {
                    showSnackBar("Confirm Your password");
                }else if (!edt_password.getText().toString().trim().equals(edt_c_password.getText().toString().trim())) {
                    showSnackBar("Password does not match");
                }
                else if (!edt_insta_name.getText().toString().trim().equals("") && insta_logo.equals("")) {
                    showSnackBar("Please verify instagram name");
                }
//                else if(insta_logo.equals(""))
//                {
//                    showSnackBar("Password does not match");
//                }
                else {
                    businessownerapi();


                }


//
//                business_type_str = business_type.getText().toString();
//                businessname_str = edt_businessname.getText().toString();
//                instagramname_str = edt_insta_name.getText().toString();
//
//
//            Intent i=new Intent(BusinessSignupActivity.this,BusinessfinalsignupActivity.class);
//             i.putExtra("businesstype",business_type_str);
//             i.putExtra("businessname",businessname_str);
//             i.putExtra("instagramname",instagramname_str);
//              i.putExtra("selected_country",selected_country);
//             startActivity(i);
//



            }
        });





        txt_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(BusinessSignupActivity.this,LoginActivity.class);
                intent.putExtra("selectedType","seller");
                //startActivity(intent);

                startActivity(intent);
            }
        });

            }





    private void businessownerapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.businessownerregister , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");

                        Translator t =  new Translator();
                        t.getTranslateService(BusinessSignupActivity.this);


                        if (status.equals("success"))
                            Toast.makeText(BusinessSignupActivity.this,t.translate(BusinessSignupActivity.this,msg),Toast.LENGTH_LONG).show();
                          stopProgress();

                        Intent i = new Intent(BusinessSignupActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();


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
                    params.put("business_type", dropdown.getSelectedItem().toString() );
                    params.put("business_name", edt_businessname.getText().toString());
                    params.put("instagram_name", edt_insta_name.getText().toString());
                    params.put("email", edt_email.getText().toString());
                    params.put("mobile", edt_mobile.getText().toString());
                    params.put("password", edt_password.getText().toString());
                    params.put("name", edt_fullname.getText().toString());
//                    params.put("country", selected_country);
//                    params.put("cid", Select_country_id);
                    params.put("country", selected_country);
                    params.put("cid",Select_country_id );
                    params.put("insta_logo", insta_logo);

                   Log.d("PARAMS>>>",params.toString());
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
            VolleySingleton.getInstance(BusinessSignupActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }



    }


    private void verifyinsta() {


        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.GET,
                    "https://www.instagram.com/"+edt_insta_name.getText().toString()+"/?__a=1", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("INSTA_IMG>>>>",response);

                    stopProgress();

                    try {
                        JSONObject jobj = new JSONObject(response);
                       if(edt_insta_name.getText().toString().trim().equals(""))
                       {
                           edt_insta_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

                           edt_insta_name.setError(getResources().getString(R.string.pleaseverify));

                       }
                       else {

//                           edt_insta_name.setError("Got Image");
                           edt_insta_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                           edt_insta_name.setCompoundDrawablesWithIntrinsicBounds(null, null,
                                   ContextCompat.getDrawable(BusinessSignupActivity.this,R.drawable.ic_baseline_check_circle_24),
                                   null);
                           insta_logo = jobj.getJSONObject("graphql").getJSONObject("user").getString("profile_pic_url_hd");
                           Log.d("INSTA_IMG>>>>",insta_logo);
                       }

                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    //parseVolleyError(error);
                    edt_insta_name.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    edt_insta_name.setError(getResources().getString(R.string.invaliduser));
                    stopProgress();

                    if (error instanceof NetworkError) {
                        //Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
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
            });

//            {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    //params.put("seller_id", "1");
//                    return params;
//                }
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<>();
//                    // headers.put(Constants.authKEY, Constants.authValue);
//                    // headers.put("header2", "header2");
//
//                    return headers;
//                }
//            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(BusinessSignupActivity.this).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }





    }

       /*private void businessownerapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.registerURL , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("Status");
                        String msg = jobjMain.getString("Message");

                        if (status.equals("1"))
                            Toast.makeText(BusinessSignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                             stopProgress();

                        Intent i = new Intent(BusinessSignupActivity.this, DashBoardActivity.class);
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
                    params.put("business_type", business_type_str);
                    params.put("business_name", businessname_str);
                    params.put("instagram_name", instagramname_str);
                    params.put("email", "abc@gmail.com");
                    params.put("mobile", "9939038805");
                    params.put("password", "surseed123");
                    params.put("name", "surseed123");

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
            VolleySingleton.getInstance(BusinessSignupActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }



    }
*/
    /*public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(BusinessSignupActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(R.string.loadingplzwait));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }*/

    /*public void parseVolleyError(VolleyError error) {
        try {
            String responseBody = new String(error.networkResponse.data, "utf-8");
            JSONObject data = new JSONObject(responseBody);
            String errors = data.getString("error");
            Log.e("VolleyError", errors);
            showSnackBar(errors);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    /*public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }*/
    /*private void showSnackBar(String msg) {
    Toast.makeText(BusinessSignupActivity.this,msg,Toast.LENGTH_LONG).show();

    }*/
}