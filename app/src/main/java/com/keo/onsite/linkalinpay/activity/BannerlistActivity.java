package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
import android.content.Intent;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Bannerlistadapter;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BannerlistActivity extends AppCompatActivity {
    RecyclerView recycler_view;
    LinearLayout layout;
    ArrayList<Bannerlistmodelclass> bannerlist;
    UserShared psh;
    private ProgressDialog progressDialog;
    Bannerlistadapter badapter;
    ImageView back_arrow_img;
    TextView addbanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bannerlist);
       // Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setTitle("BANNER LIST");
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
*/
        xmlinit();
        xmlonclik();
        callbannerlistapi();

    }

    private void xmlonclik() {
    back_arrow_img.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });
        addbanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(BannerlistActivity.this,AddBannerActivity.class);
                startActivity(i);

            }
        });

    }

    private void callbannerlistapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {

            showProgress();

            final Translator t =  new Translator();
            t.getTranslateService(BannerlistActivity.this);


            stopProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.BannerlistUrl ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                bannerlist = new ArrayList<Bannerlistmodelclass>();

                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("banner");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Bannerlistmodelclass blmc = new Bannerlistmodelclass(jobj1.getString("banner_id"),
                                                jobj1.getString("banner_sellerid"),
                                                jobj1.getString("banner_image"),
                                                t.translate(BannerlistActivity.this,jobj1.getString("banner_title")),
                                                t.translate(BannerlistActivity.this,jobj1.getString("banner_desc")),
                                                t.translate(BannerlistActivity.this,jobj1.getString("banner_status")),
                                                jobj1.getString("banner_date")
                                                );
                                        bannerlist.add(blmc);
                                    }
                                    setAdapter();
                                    stopProgress();


                                } else {

                                    stopProgress();

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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("seller_id", psh.getSellerid());
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
            Toast.makeText(BannerlistActivity.this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(BannerlistActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

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
    private void setAdapter() {
    badapter = new Bannerlistadapter(BannerlistActivity.this, bannerlist);
    recycler_view.setAdapter(badapter);






    }

    private void showSnackBar(String msg) {
    Toast.makeText(BannerlistActivity.this,msg,Toast.LENGTH_LONG);
    }

    private void xmlinit() {
       psh=new UserShared(this);

        back_arrow_img=(ImageView)findViewById(R.id.back_arrow);
        addbanner=(TextView)findViewById(R.id.addbanner);
        recycler_view = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
        //layout = (LinearLayout) findViewById(R.id.layout);




    }
}