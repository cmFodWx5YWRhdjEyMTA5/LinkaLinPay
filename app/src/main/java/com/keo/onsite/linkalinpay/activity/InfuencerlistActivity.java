package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Influencerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.adapter.InfluencerAdapter_;
import com.keo.onsite.linkalinpay.adapter.Influenceradapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InfuencerlistActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recycler_view;
    private ProgressDialog progressDialog;
    ArrayList<Influencerlistmodelclass> influencerarraylist;
    InfluencerAdapter_ influencerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infuencerlist);
            xmlinit();
            xmlonclik();
            callinfluencerlistapi();
    }

    private void callinfluencerlistapi() {
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
            VolleySingleton.getInstance(InfuencerlistActivity.this).addToRequestQueue(stringRequest);
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
        Toast.makeText(InfuencerlistActivity.this,msg,Toast.LENGTH_LONG).show();
    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void setadapter() {
        influencerAdapter=new InfluencerAdapter_(InfuencerlistActivity.this,influencerarraylist);
        recycler_view.setAdapter(influencerAdapter);

    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(InfuencerlistActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void xmlonclik() {
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });

    }

        private void xmlinit() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.influencerlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //recycler_view.setLayoutManager(layoutManager);
            LinearLayoutManager layoutManager=new LinearLayoutManager(this);
            recycler_view.setLayoutManager(layoutManager);
            Recyclerviewmargin itemdecorator=new Recyclerviewmargin(20);
            recycler_view.addItemDecoration(itemdecorator);




    }
}