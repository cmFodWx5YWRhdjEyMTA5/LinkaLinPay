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
import com.github.channguyen.rsv.RangeSliderView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.keo.onsite.linkalinpay.activity.other.Constants.trackorderapi;

public class TrackOrderActivity extends AppCompatActivity {
     Toolbar toolbar;
     private ProgressDialog progressDialog;
     UserShared psh;
    TextView orderdate,ordertime;
     Intent mintent;
    private RangeSliderView smallSlider;
    String[] descriptionData = {"Pending", "Processed", "Shipped", "Delivered","Completed"};



    TextView l1,l2,l3,l4,l5;
    TextView t1,t2,t3,t4,t5;
    TextView d1,d2,d3,d4,d5;
    ImageView c1,c2,c3,c4,c5;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);
       psh=new UserShared(this);
       mintent=getIntent();
        xmlinit();
        xmlonclik();
        trackorderapi();

    }

       private void trackorderapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.trackorderapi , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("Track_response>>",response);

                    try {

                        JSONObject jobjMain = new JSONObject(response);

                        JSONArray track = jobjMain.getJSONArray("track");

                        for(int i = 0; i < 7; i++) {

                            if (i == 0) {
                                t1.setText(track.getJSONObject(i).getString("name"));
                                //   c1.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                if (track.getJSONObject(i).getString("date").equals("0")) {
                                    d1.setText(R.string.pending);
                                } else {
                                    d1.setText(track.getJSONObject(i).getString("date"));
                                    d1.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    l1.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    t1.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    c1.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                }
                            }
                            if (i == 1) {
                                t2.setText(track.getJSONObject(i).getString("name"));
                                //   c1.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                if (track.getJSONObject(i).getString("date").equals("0")) {
                                    d2.setText(R.string.pending);
                                } else {
                                    d2.setText(track.getJSONObject(i).getString("date"));
                                    d2.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    l2.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    t2.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    c2.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                }
                            }
                            if (i == 2) {
                                t3.setText(track.getJSONObject(i).getString("name"));
                                //   c1.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                if (track.getJSONObject(i).getString("date").equals("0")) {
                                    d3.setText(R.string.pending);
                                } else {
                                    d3.setText(track.getJSONObject(i).getString("date"));
                                    d3.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    l3.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    t3.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    c3.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                }
                            }
                            if (i == 3) {
                                t4.setText(track.getJSONObject(i).getString("name"));
                                //   c1.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                if (track.getJSONObject(i).getString("date").equals("0")) {
                                    d4.setText(R.string.pending);
                                } else {
                                    d4.setText(track.getJSONObject(i).getString("date"));
                                    d4.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    l4.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    t4.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    c4.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                }
                            }
                            if (i == 4) {

                                if(track.getJSONObject(i).getString("name").toLowerCase().equals("completed")) {
                                    t5.setText(track.getJSONObject(i).getString("name"));
                                    //   c1.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    if (track.getJSONObject(i).getString("date").equals("0")) {
                                        d5.setText(R.string.pending);
                                    } else {
                                        d5.setText(track.getJSONObject(i).getString("date"));
                                        d5.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                        l5.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                        t5.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                        c5.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                    }
                                }
                                else {

                                    t5.setText(track.getJSONObject(i).getString("name"));
                                    d5.setText(track.getJSONObject(i).getString("date"));
                                    d5.setTextColor(getResources().getColorStateList(R.color.colorAccent));

                                    t5.setTextColor(getResources().getColorStateList(R.color.colorAccent));
                                    c5.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                    c4.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                    c3.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                    c2.setImageTintList(getResources().getColorStateList(R.color.colorAccent));
                                    c1.setImageTintList(getResources().getColorStateList(R.color.colorAccent));

                                    l1.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    l2.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    l3.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    l4.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));
                                    l5.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccent));


                                }
                            }







                        }
                        stopProgress();




//                        String status = jobjMain.getString("status");
//                        // String msg = jobjMain.getString("msg");
//                        if (status.equals("success")) {
//                            stopProgress();
//                            JSONObject jobj=jobjMain.getJSONObject("trackorderdetail");
//                            orderdate.setText(jobj.getString("order_date"));
//                            //ordertime.setText(jobj.getString(""));
//                        }

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
                    params.put("order_id",mintent.getStringExtra("orderid") );
                    params.put("cust_id",psh.getCustomerid() );

/*psh.getCustomerid()*/
                    /*mintent.getStringExtra("orderid")*/
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
            VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
        } else {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    private void showSnackBar(String msg) {
    Toast.makeText(TrackOrderActivity.this,msg,Toast.LENGTH_LONG).show();

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
    toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });


    }

        private void xmlinit() {
      // StateProgressBar stateProgressBar = (StateProgressBar) findViewById(R.id.your_state_progress_bar_id);
//       stateProgressBar.setStateDescriptionData(descriptionData);
//       stateProgressBar.setStateDescriptionTypeface("fonts/RobotoSlab-Light.ttf");
//       stateProgressBar.setStateNumberTypeface("fonts/Questrial-Regular.ttf");
       //stateProgressBar.setStateNumberIsDescending(true);


             l1 =(TextView)findViewById(R.id.l1);
             l2=(TextView)findViewById(R.id.l2);
             l3=(TextView)findViewById(R.id.l3);
             l4=(TextView)findViewById(R.id.l4);
             l5 =(TextView)findViewById(R.id.l5);

             t1 =(TextView)findViewById(R.id.t1);
             t2 =(TextView)findViewById(R.id.t2);
             t3 =(TextView)findViewById(R.id.t3);
             t4 =(TextView)findViewById(R.id.t4);
             t5 =(TextView)findViewById(R.id.t5);

             d1  =(TextView)findViewById(R.id.d1);
             d2  =(TextView)findViewById(R.id.d2);
             d3  =(TextView)findViewById(R.id.d3);
             d4 =(TextView)findViewById(R.id.d4);
             d5  =(TextView)findViewById(R.id.d5);

             c1  = findViewById(R.id.c1);
             c2  = findViewById(R.id.c2);
             c3  = findViewById(R.id.c3);
             c4  = findViewById(R.id.c4);
             c5  = findViewById(R.id.c5);


            orderdate=(TextView)findViewById(R.id.orderdate);
       // ordertime=(TextView)findViewById(R.id.ordertime);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.trackorder);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));

    }
}