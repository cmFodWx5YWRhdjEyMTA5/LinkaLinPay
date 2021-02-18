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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Parentcategorymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Productcategoryadapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EditProductselleractivity extends AppCompatActivity {
    EditText catname, catdesc, productcatname;
    String catname_str,catdes_str,productcatname_str;
    Spinner spinner;
    Intent mintent;
    ArrayList<Parentcategorymodelclass> pcategorylist;
    private ProgressDialog progressDialog;
    UserShared psh;
     int position = 0;
   Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_productselleractivity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.editproduct);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mintent = getIntent();
        psh = new UserShared(this);
        xmlinit();
        xmlonclik();
    }

    private void xmlonclik() {
        productcatname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callcategoryapi();
                productcatname.setVisibility(View.GONE);

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               catname_str=catname.getText().toString();
               productcatname_str=productcatname.getText().toString();
               catdes_str=catdesc.getText().toString();
                callupdateapi();
            }
        });
    }

    private void callupdateapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.sellercatupdate, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                         Toast.makeText(EditProductselleractivity.this,msg,Toast.LENGTH_LONG).show();
                            stopProgress();
                        Intent i=new Intent(EditProductselleractivity.this,Productcategory.class);
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
                    params.put("category_id", String.valueOf(position));
                    params.put("parent_category_id", mintent.getStringExtra("pcatid"));
                    params.put("category_name", catname_str);
                    params.put("category_desc", catdes_str);



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
            VolleySingleton.getInstance(EditProductselleractivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }



    }

    private void callcategoryapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Addparentcatapi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("category");
                        pcategorylist = new ArrayList<>();
                        if (status.equals("success")) {
                            JSONArray jsonArray = jobjMain.getJSONArray("category");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jobj = jsonArray.getJSONObject(i);
                                Parentcategorymodelclass pcmc = new Parentcategorymodelclass(jobj.getString("category_id"),
                                        jobj.getString("category_sellerid"), jobj.getString("category_name"));

                                pcategorylist.add(pcmc);

                            }
                            setadapter();
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
                    params.put("seller_id", psh.getSellerid());


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
            VolleySingleton.getInstance(EditProductselleractivity.this).addToRequestQueue(stringRequest);
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
        Toast.makeText(EditProductselleractivity.this, msg, Toast.LENGTH_LONG).show();

    }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(EditProductselleractivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void setadapter() {
        Productcategoryadapter padapter = new Productcategoryadapter(EditProductselleractivity.this, pcategorylist, R.layout.catgry_item);
        spinner.setAdapter(padapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i > 0) {
                    position = Integer.parseInt(pcategorylist.get(i - 1).category_id);

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    private void xmlinit() {
        productcatname = (EditText) findViewById(R.id.parentcategory);
        productcatname.setText(mintent.getStringExtra("pcatname"));
        productcatname_str=mintent.getStringExtra("pcatname");
        catname = (EditText) findViewById(R.id.catname);
        catname.setText(mintent.getStringExtra("catname"));
        catname_str=mintent.getStringExtra("catname");
        catdesc = (EditText) findViewById(R.id.catdesc);
        catname.setText(mintent.getStringExtra("catname"));
        catdes_str=mintent.getStringExtra("catdesc");
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setVisibility(View.VISIBLE);
        btn_submit=(Button)findViewById(R.id.btn_submit);

    }
}