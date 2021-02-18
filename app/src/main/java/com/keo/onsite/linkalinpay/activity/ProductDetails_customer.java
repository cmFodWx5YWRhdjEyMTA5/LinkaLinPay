package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Bannermodelclass;
import com.keo.onsite.linkalinpay.activity.model.RelatedProductmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Relatedimagemodelclss;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Relatedproductadapter;
import com.keo.onsite.linkalinpay.fragement.CategoryFragment;
import com.smarteist.autoimageslider.SliderView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ProductDetails_customer extends AppCompatActivity {
    Toolbar toolbar;
    ViewPager viewPager;
    TextView price, offerprice, quantity, productname, description, add_cart;
    private ProgressDialog progressDialog;
    RecyclerView recyclerview;
    Intent mIntent;
    ArrayList<Relatedimagemodelclss> relatedproductarraylist;
    ArrayList<RelatedProductmodelclass> relatedproductlist;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    CirclePageIndicator indicator;
    ImageView increment, decrement,cart,wishlist,imageview;
    String qty_str;
    int minteger = 1;
    int mItemCount = 0;
    UserShared psh;
    TextView count;
    String final_str;
    String sellerid,productid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details_customer);
        psh = new UserShared(this);
        xmlinit();
        xmlonclik();
        callproductdetailapi();

    }

    private void callproductdetailapi() {

        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.custproddettapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                relatedproductarraylist = new ArrayList<>();
                                relatedproductlist = new ArrayList<>();

                                if (status.equals("success")) {
                                    Toast.makeText(ProductDetails_customer.this, msg, Toast.LENGTH_LONG).show();
                                    stopProgress();
                                    price.setText(jsonObject.getString("product_currency")+" "+jsonObject.getString("product_price"));
                                    price.setPaintFlags(price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                                    offerprice.setText(jsonObject.getString("product_currency")+" "+jsonObject.getString("product_offerprice"));
                                  //  quantity.setText(jsonObject.getString("product_qty"));
                                   // final_str = jsonObject.getString("product_qty");
                                   // count.setText(final_str);

                                    productname.setText(jsonObject.getString("product_name"));
                                    description.setText(jsonObject.getString("product_desc"));
                                    JSONArray jarray = jsonObject.getJSONArray("product_image");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Relatedimagemodelclss rimc = new Relatedimagemodelclss(jobj1.getString("product_image"));
                                        relatedproductarraylist.add(rimc);
                                        JSONArray jarray1 = jsonObject.getJSONArray("relatedproduct");
                                        for (int i1 = 0; i1 < jarray1.length(); i1++) {
                                            JSONObject jobj2 = jarray1.getJSONObject(i1);
                                            RelatedProductmodelclass rpmc = new RelatedProductmodelclass(jobj2.getString("product_id"),
                                                    jobj2.getString("product_cat"), jobj2.getString("product_name"),
                                                    jobj2.getString("product_desc"), jobj2.getString("product_price"),
                                                    jobj2.getString("product_offerprice"), jobj2.getString("product_image"));
                                            relatedproductlist.add(rpmc);


                                        }

                                        viewPager.setAdapter(new StaticSlidingImage_Adapter(ProductDetails_customer.this, relatedproductarraylist));
                                        float density = getResources().getDisplayMetrics().density;
                                        indicator.setViewPager(viewPager);
                                        indicator.setRadius(4 * density);

                                        NUM_PAGES = relatedproductarraylist.size();

                                        // Auto start of viewpager
                                        final Handler handler = new Handler();
                                        final Runnable Update = new Runnable() {
                                            public void run() {
                                                if (currentPage == NUM_PAGES) {
                                                    currentPage = 0;
                                                }
                                                viewPager.setCurrentItem(currentPage++, true);
                                            }
                                        };
                                        Timer swipeTimer = new Timer();
                                        swipeTimer.schedule(new TimerTask() {
                                            @Override
                                            public void run() {
                                                handler.post(Update);
                                            }
                                        }, 5000, 5000);
                                        //============

                                        setadapter();


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
                    params.put("product_id", mIntent.getStringExtra("productid"));
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
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void setadapter() {
        Relatedproductadapter rpadapter = new Relatedproductadapter(ProductDetails_customer.this, relatedproductlist,sellerid,productid);
        recyclerview.setAdapter(rpadapter);

    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(ProductDetails_customer.this, ProgressDialog.THEME_HOLO_LIGHT);
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


    private void showSnackBar(String msg) {
        Toast.makeText(ProductDetails_customer.this, msg, Toast.LENGTH_LONG).show();


    }


    private void xmlonclik() {
    /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            finish();
        }
    });*/

       imageview.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wishlistapi();
            }
        });




        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtocartapi();
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ProductDetails_customer.this,CartlistActivity.class);
                i.putExtra("sellerid",mIntent.getStringExtra("sellerid"));

                startActivity(i);

            }
        });



        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mItemCount = Integer.parseInt(quantity.getText().toString());
                mItemCount++;
                //minteger=Integer.parseInt(qty_str)+1;
                quantity.setText(String.valueOf("" + mItemCount));
                count.setText(String.valueOf("" + mItemCount));
                final_str = String.valueOf(mItemCount);

            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemCount = Integer.parseInt(quantity.getText().toString());
                if (mItemCount <= 0) {
                    mItemCount = 0;
                } else {
                    mItemCount--;
                    quantity.setText("" + mItemCount);
                    count.setText(String.valueOf("" + mItemCount));
                    final_str = String.valueOf(mItemCount);
                }


            }
        });

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


    }

       private void wishlistapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Wishtlistapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(ProductDetails_customer.this, msg, Toast.LENGTH_LONG).show();
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
                    params.put("seller_id", mIntent.getStringExtra("sellerid"));
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id", mIntent.getStringExtra("productid"));



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

    private void addtocartapi() {

        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.AddtoCartapiapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.getString("msg");
                                if (status.equals("success")) {
                                    Toast.makeText(ProductDetails_customer.this, msg, Toast.LENGTH_LONG).show();
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
                    params.put("seller_id", mIntent.getStringExtra("sellerid"));
                    params.put("cust_id", psh.getCustomerid());
                    params.put("product_id", mIntent.getStringExtra("productid"));
                    params.put("qty", String.valueOf(final_str));
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
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void xmlinit() {
        imageview=(ImageView)findViewById(R.id.imageview);

        mIntent = getIntent();
        //toolbar=(Toolbar)findViewById(R.id.toolbar);
        //toolbar.setTitle("Product Details");
        sellerid= mIntent.getStringExtra("sellerid");
        productid=mIntent.getStringExtra("productid");


        viewPager = (ViewPager) findViewById(R.id.viewPager);
        price = (TextView) findViewById(R.id.price);
        offerprice = (TextView) findViewById(R.id.offerprice);
        quantity = (TextView) findViewById(R.id.quantity);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerview.setLayoutManager(layoutManager);
        productname = (TextView) findViewById(R.id.productname);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        description = (TextView) findViewById(R.id.description);
        increment = (ImageView) findViewById(R.id.increment);
        decrement = (ImageView) findViewById(R.id.decrement);
        add_cart = (TextView) findViewById(R.id.add_cart);
        count = (TextView) findViewById(R.id.count);
        cart=(ImageView)findViewById(R.id.cart);
        wishlist=(ImageView)findViewById(R.id.wishlist);



    }

    //=========view pager adapter setting=============
    public class StaticSlidingImage_Adapter extends PagerAdapter {

        private ArrayList<Relatedimagemodelclss> IMAGES;
        private LayoutInflater inflater;

        public StaticSlidingImage_Adapter(Context context, ArrayList<Relatedimagemodelclss> IMAGES) {
            this.IMAGES = IMAGES;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public int getCount() {
            return IMAGES.size();


        }

        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            View imageLayout = inflater.inflate(R.layout.custom_layout, view, false);
            assert imageLayout != null;
            final ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.itemImage);

            Glide.with(ProductDetails_customer.this)
                    .load(IMAGES.get(position).product_image)
                    .into(imageView);


            view.addView(imageLayout, 0);
            return imageLayout;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }


        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
        }

        @Override
        public Parcelable saveState() {
            return null;
        }

    }


}