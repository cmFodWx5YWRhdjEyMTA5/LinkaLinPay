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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Countrymodelclass;
import com.keo.onsite.linkalinpay.activity.model.Statemodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.CountryAdapter;
import com.keo.onsite.linkalinpay.adapter.StateAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity {
    CheckBox toggle;
    LinearLayout linearlayout, linear_layout_lv;
    TextView continueer;
    EditText fullname, streetaddress,  town, phonenumber, streetaddress_sh,  town_edt_sh;
    Spinner country, country_sh,state,state_edt_sh;
    ArrayList<Countrymodelclass> countrylist;
    ArrayList<Statemodelclass> statelist;
     Intent mIntent;
    private ProgressDialog progressDialog;
    String fullname_str, streetaddress_str, state_str, town_str, phonenumber_str, streetaddress_sh_str, state_edt_sh_str, town_edt_sh_str;
    UserShared psh;
    String selected_country, selectedcountry_shipping;
    CountryAdapter cadapter;
    StateAdapter sadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.checkoutmeth);
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
        psh = new UserShared(this);
        xmlinit();
        xmlonclik();
        callcountryapi();
        stateapi();

    }

    private void stateapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Stateapi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        statelist = new ArrayList<>();
                        //String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            //Toast.makeText(CheckoutActivity.this,msg,Toast.LENGTH_LONG).show();
                            JSONArray jarray = jobjMain.getJSONArray("state");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject jobj = jarray.getJSONObject(i);
                                Statemodelclass smc = new Statemodelclass(jobj.getString("name"));
                                statelist.add(smc);
                                stopProgress();
                            }
                            setadapter_state();
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
            VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }







    }

    private void setadapter_state() {
     sadapter=new StateAdapter(CheckoutActivity.this,statelist,R.layout.catgry_item);
     state.setAdapter(sadapter);
     state_edt_sh.setAdapter(sadapter);
      state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
              state_str=statelist.get(i).name;
          }

          @Override
          public void onNothingSelected(AdapterView<?> adapterView) {

          }
      });

        state_edt_sh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                state_edt_sh_str = statelist.get(i).name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



    }

    private void callcountryapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.alllocation, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        countrylist = new ArrayList<>();
                        //String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            //Toast.makeText(CheckoutActivity.this,msg,Toast.LENGTH_LONG).show();
                            JSONArray jarray = jobjMain.getJSONArray("data");
                            for (int i = 0; i < jarray.length(); i++) {
                                JSONObject jobj = jarray.getJSONObject(i);

                                Countrymodelclass cmc = new Countrymodelclass(jobj.getString("location"),"id");
                                countrylist.add(cmc);

                            Log.d("Response>>",response);
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
              VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(stringRequest);
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

    private void showSnackBar(String msg) {
        Toast.makeText(CheckoutActivity.this, msg, Toast.LENGTH_LONG).show();
    }

        private void setadapter() {
         cadapter = new CountryAdapter(CheckoutActivity.this, countrylist, R.layout.catgry_item);
        country.setAdapter(cadapter);
        country_sh.setAdapter(cadapter);

        country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_country = countrylist.get(i).name;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        country_sh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedcountry_shipping = countrylist.get(i).name;
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

    private void xmlonclik() {
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                   // linear_layout_lv.setVisibility(View.VISIBLE);
                    fullname_str = fullname.getText().toString();
                    streetaddress_str = streetaddress.getText().toString();
                    //state_str = state.getText().toString();
                    town_str = town.getText().toString();
                    streetaddress_sh_str = streetaddress_sh.getText().toString();
                    town_edt_sh_str = town_edt_sh.getText().toString();
                    //     state_edt_sh_str = streetaddress_sh.getText().toString();
                    phonenumber_str = phonenumber.getText().toString();
                    streetaddress_sh.setText(streetaddress_str);
                   country_sh.setAdapter(cadapter);
                   state_edt_sh.setAdapter(sadapter);
                   town_edt_sh.setText(town_str);

           } else {
                   //linear_layout_lv.setVisibility(View.GONE);

                    streetaddress_sh.setText("");
                    country_sh.setAdapter(cadapter);
                    state_edt_sh.setAdapter(sadapter);
                    town_edt_sh.setText("");
                    fullname_str = fullname.getText().toString();
                    streetaddress_str = streetaddress.getText().toString();
                    //state_str = state.getText().toString();
                    town_str = town.getText().toString();
                    streetaddress_sh_str = streetaddress_sh.getText().toString();
                    town_edt_sh_str = town_edt_sh.getText().toString();
                    //     state_edt_sh_str = streetaddress_sh.getText().toString();
                    phonenumber_str = phonenumber.getText().toString();

                }

            }
        });

        continueer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(CheckoutActivity.this,"Coming soon",Toast.LENGTH_LONG).show();


//                if (fullname_str.equals("")) {
//                    showSnackBar("Please Enter Customer Name");
//                } else if (streetaddress_str.equals("")) {
//                    showSnackBar("Enter  Email Address");
//                } else if (state_str.equals("")) {
//                    showSnackBar("Enter  Mobile Number");
//                } else if (town_str.equals("")) {
//                    showSnackBar("Enter  Amount");
//                } else {
                    callsaveapi();


//                }


            }
        });


    }

    private void callsaveapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Saveorder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            Toast.makeText(CheckoutActivity.this, msg, Toast.LENGTH_LONG).show();
                            stopProgress();

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
                    params.put("cust_id", psh.getCustomerid());
                    params.put("paymentmethod", "COD");
                    params.put("name", fullname_str);
                    params.put("email", "abc@gmail.com");
                    params.put("mobile", phonenumber_str);
                    params.put("address", streetaddress_str);
                    params.put("city", town_str);
                    params.put("country", selected_country);
                   if(streetaddress_sh_str.equals("")){
                       params.put("saddress", streetaddress_str);
                   }else{
                       params.put("saddress", streetaddress_sh_str);
                   }

                    if(town_edt_sh_str.equals("")){
                        params.put("scity", town_str);
                    }else{
                        params.put("scity", town_edt_sh_str);
                    }

                    params.put("sstate", state_edt_sh_str);
                    params.put("scountry", selectedcountry_shipping);


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
            VolleySingleton.getInstance(CheckoutActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

        private void xmlinit() {
        mIntent = getIntent();
        toggle = (CheckBox) findViewById(R.id.chk_Book);
        linearlayout = (LinearLayout) findViewById(R.id.linearlayout);
        linear_layout_lv = (LinearLayout) findViewById(R.id.linear_layout);
        fullname = (EditText) findViewById(R.id.fullname);
        streetaddress = (EditText) findViewById(R.id.streetaddress);
        state = (Spinner) findViewById(R.id.state);
        town = (EditText) findViewById(R.id.town);
        phonenumber = (EditText) findViewById(R.id.phonenumber);
        streetaddress_sh = (EditText) findViewById(R.id.streetaddress_sh);
        state_edt_sh = (Spinner) findViewById(R.id.state_edt_sh);
        town_edt_sh = (EditText) findViewById(R.id.town_edt_sh);
        country = (Spinner) findViewById(R.id.country);
        country_sh = (Spinner) findViewById(R.id.country_edt_sh);
       continueer = (TextView) findViewById(R.id.continueer);

    }
}