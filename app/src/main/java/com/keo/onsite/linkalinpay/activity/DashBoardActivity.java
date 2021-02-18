package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.LanguageChange;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashBoardActivity extends AppCompatActivity {
 LinearLayout mybusiness,quikinvoice,generatecouponcode,quikinvoice_real,customerlist,help,kpays,calendar;
   ImageView img_setting;
   TextView switching,myrev;
    UserShared psh;
    RelativeLayout bankerror;
    ImageView logo;

    JSONObject kpaysobj,sellercalendarobj;
    String total;

    String switch_username,switch_email;

    String imageuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageChange().LoadLocal(getBaseContext());
        setContentView(R.layout.activity_dash_board);
        xmlinit();
        xmlonclik();
        getalldata();



    }


    @Override
    protected void onRestart() {
        super.onRestart();
//        getalldata();
 //       recreate();
     Intent   starterIntent = getIntent();

        finish();
        startActivity(starterIntent);
    }

    private void getalldata() {

        if (VolleySingleton.getInstance(this).isConnected()) {

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.sellerdashboard,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response)
                        {
                            Log.d("response>>",response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");

                                if (status.equals("success")) {


                                    JSONObject dash = jsonObject.getJSONObject("dashboard");

                                    if(dash.getString("bank").equals("0"))
                                    {
                                        bankerror.setVisibility(View.VISIBLE);
                                    }
                                    else
                                    {
                                        bankerror.setVisibility(View.GONE);
                                    }

                                    kpaysobj = dash.getJSONObject("kpays");
                                    imageuri = kpaysobj.getString("seller_logo");
                                    switch_username = kpaysobj.getString("seller_name");
                                    switch_email = kpaysobj.getString("seller_email");
                                    sellercalendarobj = jsonObject.getJSONObject("revenue");
                                    total = jsonObject.getString("income");
                                    myrev.setText(total);
                                    Glide.with(getApplicationContext())
                                            .load(kpaysobj.getString("seller_logo"))
                                            .error(R.drawable.logo)
                                            .into(logo);




                                } else {

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
//                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
//                                showSnackBar("No internet connection");
                            } else if (error instanceof ParseError) {

//                                showSnackBar("No internet connection");
                            } else if (error instanceof NoConnectionError) {

//                                showSnackBar("No internet connection");
                            } else {

                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("sellerid", psh.getSellerid());
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
            Toast.makeText(DashBoardActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }



}

    private void xmlonclik() {
    mybusiness.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent(DashBoardActivity.this,MyBusinessDashboardActivity.class);
            startActivity(i);

        }
    });


    calendar.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent i=new Intent(DashBoardActivity.this,SellerCalendar.class);
            i.putExtra("sellercal",sellercalendarobj.toString());
            i.putExtra("total", total);

            startActivity(i);

        }
    });

        bankerror.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(DashBoardActivity.this,BusinessInformation.class);
                startActivity(i);

            }
        });


        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DashBoardActivity.this,SettingActivity.class);
                i.putExtra("logo",imageuri);
               startActivity(i);

            }
        });

        quikinvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DashBoardActivity.this,InvoiceActivity.class);
                startActivity(i);


            }
        });

        quikinvoice_real.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DashBoardActivity.this, AddInvoiceActivity.class);
                startActivity(i);

            }
        });


        generatecouponcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(DashBoardActivity.this,CouponActivity.class);
                startActivity(i);

            }
        });

        customerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashBoardActivity.this,CustomerList.class);
                startActivity(i);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashBoardActivity.this,HelpSupport.class);
                startActivity(i);
            }
        });

        switching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashBoardActivity.this,SwitchAccount.class);
                i.putExtra("username",switch_username);
                i.putExtra("email",switch_email);
                i.putExtra("logo",imageuri);
                startActivity(i);
            }
        });

        kpays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(DashBoardActivity.this,Kpays.class);
                i.putExtra("kpayobj",kpaysobj.toString());
                startActivity(i);
            }
        });

}

    private void xmlinit() {
    mybusiness=(LinearLayout)findViewById(R.id.mybusiness);
    img_setting=(ImageView)findViewById(R.id.img_setting);
    quikinvoice=(LinearLayout)findViewById(R.id.quikinvoice);
    generatecouponcode=(LinearLayout)findViewById(R.id.generatecouponcode);
    quikinvoice_real = findViewById(R.id.quikinvoice_real);
    customerlist = findViewById(R.id.customerlist);
    help = findViewById(R.id.help);
    myrev = findViewById(R.id.total);
    switching = findViewById(R.id.switching);
        kpays = findViewById(R.id.kpays);
        psh = new UserShared(this);
        bankerror = findViewById(R.id.bankerror);
        calendar = findViewById(R.id.calendar);
        logo = findViewById(R.id.logo);


    }
}