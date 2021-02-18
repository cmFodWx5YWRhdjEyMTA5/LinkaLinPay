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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productcategorymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Productcategoryadapte;
import com.keo.onsite.linkalinpay.utils.Translator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Productcategory extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
       RecyclerView recycler_view;
       ArrayList<Productcategorymodelclass>productcategory;
       private ProgressDialog progressDialog;
       Productcategoryadapte catadapter;
       LinearLayout linearlayout;
       FloatingActionButton fab;
       UserShared psh;

      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productcategory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.categorymanagement);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               finish();
           }
       });
          psh=new UserShared(this);

        xmlinit();
        callproductapi();
    }

    private void callproductapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            final Translator t =  new Translator();
            t.getTranslateService(Productcategory.this);

            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Sellercatlist ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                productcategory = new ArrayList<Productcategorymodelclass>();

                                if (status.equals("success")) {
                                    JSONArray jarray = jsonObject.getJSONArray("category");
                                    for (int i = 0; i < jarray.length(); i++) {
                                        JSONObject jobj1 = jarray.getJSONObject(i);
                                        Productcategorymodelclass blmc = new Productcategorymodelclass(jobj1.getString("category_id"),
                                                jobj1.getString("category_sellerid"),
                                                t.translate(Productcategory.this,   jobj1.getString("category_name")),
                                                jobj1.getString("category_link"),
                                                t.translate(Productcategory.this,   jobj1.getString("category_desc")),
                                                t.translate(Productcategory.this,   jobj1.getString("category_featured")),
                                                t.translate(Productcategory.this,   jobj1.getString("category_status")),
                                                jobj1.getString("category_date"),
                                                jobj1.getString("category_ip"),
                                                jobj1.getString("category_subid"),
                                                t.translate(Productcategory.this,   jobj1.getString("parent_categoryname"))
//
//                                                jobj1.getString("parent_categoryname")
                                        );
                                        productcategory.add(blmc);
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
            Toast.makeText(Productcategory.this, "No internet connection", Toast.LENGTH_SHORT).show();
        }




    }

    private void setAdapter() {
    catadapter=new Productcategoryadapte(Productcategory.this,productcategory);
    recycler_view.setAdapter(catadapter);
      //ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, Productcategory.this);
        //new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recycler_view);

    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(Productcategory.this, ProgressDialog.THEME_HOLO_LIGHT);
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
    Toast.makeText(Productcategory.this,msg,Toast.LENGTH_LONG).show();

    }

    private void xmlinit() {
        linearlayout=(LinearLayout)findViewById(R.id.linearlayout);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.addproduct, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent i =new Intent(Productcategory.this,AddProductcategory.class);
                startActivity(i);
            }
        });



    recycler_view=(RecyclerView)findViewById(R.id.recycler_view);
    LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recycler_view.setLayoutManager(layoutManager);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recycler_view.getContext(),
                layoutManager.getOrientation());
        recycler_view.addItemDecoration(dividerItemDecoration);
*/
      }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof Productcategoryadapte.ViewHolder) {
            // get the removed item name to display it in snack bar
            String name = productcategory.get(viewHolder.getAdapterPosition()).parent_categoryname;

            // backup of removed item for undo purpose
            final Productcategorymodelclass deletedItem = productcategory.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();

            // remove the item from recycler view
            catadapter.removeItem(viewHolder.getAdapterPosition());

            // showing snack bar with Undo option
            Snackbar snackbar = Snackbar
                    .make(linearlayout, name + " removed from cart!", Snackbar.LENGTH_LONG);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    // undo is selected, restore the deleted item
                    catadapter.restoreItem(deletedItem, deletedIndex);
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }



}