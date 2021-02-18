package com.keo.onsite.linkalinpay.activity.tabswaper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.model.Myordermodelclass;
import com.keo.onsite.linkalinpay.activity.model.cartlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by ANDROID DEV on 12/15/2017.
 */

public class MainActicityNewTwo extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    ConnectionDetector connection;
    ArrayList<Myordermodelclass> myorderlist;
    ArrayList<Myordermodelclass> myorderlist1;
    ArrayList<Myordermodelclass> myorderlist2;
    private ProgressDialog progressDialog;
    View mIndicator;
    private int indicatorWidth;
    ImageView img;
   UserShared psh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_order);
        psh=new UserShared(this);
        tabLayout = (TabLayout) findViewById(R.id.tab);
       // tabLayout.setTitle(titles);
        img=(ImageView)findViewById(R.id.img);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        connection = new ConnectionDetector(this);
        //mIndicator = findViewById(R.id.indicator);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        callmyorderapi();
    }

    private void callmyorderapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.myorder_cust,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                myorderlist = new ArrayList<>();
                                myorderlist1=new ArrayList<>();
                                myorderlist2=new ArrayList<>();
                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("myorder");


                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj = jarray.getJSONObject(i);
                                        JSONArray jarray1=jobj.getJSONArray("productdata");

                                       for(int i1=0;i1<jarray1.length();i1++){
                                      JSONObject jobj2=jarray1.getJSONObject(i1);
                                       if(jobj.getString("orderstatus").equals("Pending")){

                                          Myordermodelclass clms = new Myordermodelclass(jobj.getString("orderstatus"),
                                                  jobj.getString("order_date"),jobj.getString("currency"), jobj.getString("total_amount"), jobj.getString("order_id"),
                                                  jobj.getString("couponamount"), jobj.getString("couponcode"), jobj.getString("couponname"),
                                                  jobj2.getString("product_name"), jobj2.getString("product_price"),
                                                  jobj2.getString("product_offerprice"),jobj2.getString("product_image"));

                                           myorderlist.add(clms);


                                      }else if((jobj.getString("orderstatus").equals("Completed"))){
                                           Myordermodelclass clm = new Myordermodelclass(jobj.getString("orderstatus"),
                                                   jobj.getString("order_date"),jobj.getString("currency"), jobj.getString("total_amount"), jobj.getString("order_id"),
                                                   jobj.getString("couponamount"), jobj.getString("couponcode"), jobj.getString("couponname"),
                                                   jobj2.getString("product_name"), jobj2.getString("product_price"),
                                                   jobj2.getString("product_offerprice"),jobj2.getString("product_image"));

                                           myorderlist1.add(clm);





                                       }else if(jobj.getString("orderstatus").equals("Rejected")){
                                           Myordermodelclass clm1 = new Myordermodelclass(jobj.getString("orderstatus"),
                                                   jobj.getString("order_date"),jobj.getString("currency"), jobj.getString("total_amount"), jobj.getString("order_id"),
                                                   jobj.getString("couponamount"), jobj.getString("couponcode"), jobj.getString("couponname"),
                                                   jobj2.getString("product_name"), jobj2.getString("product_price"),
                                                   jobj2.getString("product_offerprice"),jobj2.getString("product_image"));

                                           myorderlist2.add(clm1);

                                      }
                                      setdata(myorderlist,myorderlist1,myorderlist2);





                                        }
                                       stopProgress();





                                    }



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
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    //params.put("seller_id", "1");
                    params.put("cust_id",psh.getCustomerid() );


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

    private void showSnackBar(String no_internet_connection) {
    Toast.makeText(MainActicityNewTwo.this,no_internet_connection,Toast.LENGTH_LONG);

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    private void showProgress() {


    }


    private void showToastLong(String string) {
        // TODO Auto-generated method stub

    }


    private void setdata(ArrayList<Myordermodelclass> dBoardArraylist2,
                         ArrayList<Myordermodelclass> dBoardArraylist,
                         ArrayList<Myordermodelclass> dBoardArraylist1
                         )


     {
        // TODO Auto-generated method stub
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(PendingFragment.newInstance(dBoardArraylist2.size(), dBoardArraylist2, "Pending"), getString(R.string.Pending));
        pagerAdapter.addFragment(CompleteFragment.newInstance(dBoardArraylist.size(), dBoardArraylist, "Completed"), getString(R.string.Completed));
        pagerAdapter.addFragment(CancelFragment.newInstance(dBoardArraylist1.size(), dBoardArraylist1, "Cancelled"), getString(R.string.Cancelled));
          viewPager.setAdapter(pagerAdapter);
      //  TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
         //Determine indicator width at runtime
         tabLayout.post(new Runnable() {
             @Override
             public void run() {
                 indicatorWidth = tabLayout.getWidth() / tabLayout.getTabCount();

                 //Assign new width
                // FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                // indicatorParams.width = indicatorWidth;
                // mIndicator.setLayoutParams(indicatorParams);
             }
         });


         /*viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

             //To move the indicator as the user scroll, we will need the scroll offset values
             //positionOffset is a value from [0..1] which represents how far the page has been scrolled
             //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
             @Override
             public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                 FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)mIndicator.getLayoutParams();

                 //Multiply positionOffset with indicatorWidth to get translation
                 float translationOffset =  (positionOffset+i) * indicatorWidth ;
                 params.leftMargin = (int) translationOffset;
                 mIndicator.setLayoutParams(params);
             }

             @Override
             public void onPageSelected(int i) {

             }

             @Override
             public void onPageScrollStateChanged(int i) {

             }
         });*/





     }

    static class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        // private final ArrayList<FoodModel> Data = new ArrayList<>();

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



