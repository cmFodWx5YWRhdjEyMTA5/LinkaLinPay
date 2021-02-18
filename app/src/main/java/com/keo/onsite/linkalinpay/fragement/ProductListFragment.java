package com.keo.onsite.linkalinpay.fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.keo.onsite.linkalinpay.activity.BannerlistActivity;
import com.keo.onsite.linkalinpay.activity.CartlistActivity;
import com.keo.onsite.linkalinpay.activity.MainDashboardActivity;
import com.keo.onsite.linkalinpay.activity.ProductDetailsActivity;
import com.keo.onsite.linkalinpay.activity.filter.RangeBar;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productlistmodelclass_cust;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.ProductlistAdapter_cust;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductListFragment extends Fragment {
    View view;
    CardView cardview;
    RecyclerView recyclerview;
    private ProgressDialog progressDialog;
    ArrayList<Productlistmodelclass_cust> productlist;
    ProductlistAdapter_cust ladapter;
    Intent mIntent;
    String sellerid,sellernamr;
    String catid;
    private String EndArea,StartArea;
    private RangeBar rangebar;
   TextView price_range,filterproduct;
   LinearLayout filterlayout;
    RadioButton atoz;
    RadioButton ztoa;
    private RadioGroup radioGroup;
    private String selectedType="";
    Button apply;
    TextView textCartItemCount;
    int mCartItemCount = 10;
    UserShared psh;
    public ProductListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            sellerid = arguments.get("seller_id").toString();
            catid = arguments.get("category_id").toString();
            sellernamr=arguments.get("sellernamr").toString();

        }
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_product_list, container, false);
        mIntent = getActivity().getIntent();
        psh=new UserShared(getActivity());
      //  ((MainDashboardActivity) getActivity())
            //    .getSupportActionBar().setTitle(sellernamr);


         xmlinit();
         xmlonclik();
         productlistapi();


        //cardview = view.findViewById(R.id.cardview);

        /*cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), ProductDetailsActivity.class);
                startActivity(intent);
            }
        });*/
        return view;
    }

       private void xmlonclik() {
        rangebar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex,
                                              int rightPinIndex,
                                              String leftPinValue, String rightPinValue) {
                //  leftIndexValue.setText("$" + leftPinIndex);
                // rightIndexValue.setText("$" + rightPinIndex);

                EndArea=rightPinValue;
                StartArea=leftPinValue;
               // rangee.setText(EndArea);
                price_range.setText("Find Product within : "+StartArea+""+" - "+EndArea+"");


            }

        });

           radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
               @Override
               public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                   if(checkedId==R.id.atoz){
                       selectedType = atoz.getText().toString();

                   }else if(checkedId==R.id.ztoa){
                       selectedType = ztoa.getText().toString();


                   }

               }
           });






           filterproduct.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   //filterproduct.setVisibility(View.VISIBLE);
                   filterlayout.setVisibility(View.VISIBLE);


               }
           });
           apply.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   callfilterapi();
               }
           });
    }

    private void callfilterapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.productlistcustapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                productlist = new ArrayList<Productlistmodelclass_cust>();
                               // sellerid = jsonObject.getString("seller_id");
                                if (status.equals("success")) {
                                   filterlayout.setVisibility(View.GONE);

                                    JSONArray jarray = jsonObject.getJSONArray("productlist");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Productlistmodelclass_cust blmc = new

                                                Productlistmodelclass_cust(jsonObject.getString("seller_id"), jobj1.getString("product_id"),
                                                jobj1.getString("product_cat"),
                                                jobj1.getString("product_name"),
                                                jobj1.getString("product_desc"),
                                                jobj1.getString("product_currency"),
                                                jobj1.getString("product_price"),
                                                jobj1.getString("product_offerprice"),
                                                jobj1.getString("product_image")
                                        );
                                        productlist.add(blmc);
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
                    params.put("seller_id", sellerid);
                   //  params.put("category_id", catid);
                     params.put("minprice", StartArea);
                     params.put("maxprice", EndArea);
                     params.put("sortby", selectedType);
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
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    private void productlistapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.productlistcustapi,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                productlist = new ArrayList<Productlistmodelclass_cust>();
                               // sellerid = jsonObject.getString("seller_id");
                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("productlist");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Productlistmodelclass_cust blmc = new Productlistmodelclass_cust(jsonObject.getString("seller_id"), jobj1.getString("product_id"),
                                                jobj1.getString("product_cat"),
                                                jobj1.getString("product_name"),
                                                jobj1.getString("product_desc"),
                                                jobj1.getString("product_currency"),
                                                jobj1.getString("product_price"),
                                                jobj1.getString("product_offerprice"),
                                                jobj1.getString("product_image")
                                        );
                                        productlist.add(blmc);
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
                    params.put("seller_id", sellerid);
                    // params.put("category_id", catid);
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
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setAdapter() {
        ladapter = new ProductlistAdapter_cust(getActivity(), productlist,sellerid);
        recyclerview.setAdapter(ladapter);

    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    public void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
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
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void xmlinit() {
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(sellernamr);

        radioGroup=(RadioGroup)view.findViewById(R.id.radioGroup);
        atoz=(RadioButton)view.findViewById(R.id.atoz);
        ztoa=(RadioButton)view.findViewById(R.id.ztoa);
        price_range=(TextView)view.findViewById(R.id.price_txt);
        rangebar=(RangeBar)view.findViewById(R.id.rangebar1);
        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);
        //LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        //recyclerview.setLayoutManager(layoutManager);
// set a GridLayoutManager with 3 number of columns
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        recyclerview.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        filterproduct=(TextView) view.findViewById(R.id.filterproduct);
        apply=(Button)view.findViewById(R.id.apply);
        filterlayout=(LinearLayout)view.findViewById(R.id.filterlayout);




    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

       getActivity().getMenuInflater().inflate(R.menu.main_dashboard, menu);

        final MenuItem menuItem = menu.findItem(R.id.cart);

        View actionView = menuItem.getActionView();
        //textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);

        setupBadge();

        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setupBadge() {
        /*if (textCartItemCount != null) {
            if (mCartItemCount == 0) {
                if (textCartItemCount.getVisibility() != View.GONE) {
                    textCartItemCount.setVisibility(View.GONE);
                }
            } else {
                textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                if (textCartItemCount.getVisibility() != View.VISIBLE) {
                    textCartItemCount.setVisibility(View.VISIBLE);
                }
            }
        }*/


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //handle menu item clicks
        int id = item.getItemId();
        if (id == R.id.cart) {
            //do your function here
            //Toast.makeText(getActivity(), "Settings", Toast.LENGTH_SHORT).show();
             Intent i=new Intent(getActivity(), CartlistActivity.class);
            i.putExtra("sellerid", sellerid);
           // i.putExtra("cust_id", psh.getCustomerid());
            //i.putExtra("product_id",String.valueOf(id));
            //i.putExtra("qty", "1");

            getActivity().startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }













}




