package com.keo.onsite.linkalinpay.fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Wishlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.WishlistAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishListFragment extends Fragment {
  RecyclerView wishlist_recycler;
  View view;
  ImageView img_wish_list;
  ArrayList<Wishlistmodelclass>wishlist;
  private ProgressDialog progressDialog;
  UserShared psh;
 String sellerid;
  public WishListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
         //   sellerid = arguments.get("seller_id").toString();

        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_wish_list, container, false);
         xmlinit();
         callwishlistapi();

        return view;
    }

    private void callwishlistapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()) {
            showProgress();

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Wishlistapi ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                wishlist = new ArrayList<Wishlistmodelclass>();

                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("wishlistdata");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Wishlistmodelclass blmc = new Wishlistmodelclass(jobj1.getString("id"),
                                                jobj1.getString("seller_id"),
                                                jobj1.getString("cust_id"),
                                                jobj1.getString("product_id"),
                                                jobj1.getString("product_name"),
                                                jobj1.getString("product_price"),
                                                jobj1.getString("product_offerprice"),
                                                jobj1.getString("product_image")

                                                );
                                        wishlist.add(blmc);
                                        sellerid=jobj1.getString("seller_id");

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
                    //params.put("seller_id", psh.getVendorid());
                    params.put("cust_id", psh.getCustomerid());


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
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
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
    Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

  }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


    }

    private void setAdapter() {
    WishlistAdapter wAdapter=new WishlistAdapter(getActivity(),wishlist);
    wishlist_recycler.setAdapter(wAdapter);

  }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }

    private void xmlinit() {
    psh=new UserShared(getActivity());
    wishlist_recycler=(RecyclerView)view.findViewById(R.id.wishlist_recycler);
    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    wishlist_recycler.setLayoutManager(layoutManager);

    }

    private void wishDialog() {

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