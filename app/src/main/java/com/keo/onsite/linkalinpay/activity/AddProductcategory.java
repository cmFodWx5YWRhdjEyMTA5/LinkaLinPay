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

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;
import java.util.Map;

public class AddProductcategory extends AppCompatActivity {
    Spinner cat_add;
    EditText catname,catdesc;
    Button btn_submit;
   int position=1;
   String catname_str,catdesc_str;
    private ProgressDialog progressDialog;
    UserShared psh;
   ArrayList<Parentcategorymodelclass>pcategorylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_productcategory);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.addcategory);
       // setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        setSupportActionBar(toolbar);
        psh=new UserShared(this);
        xmlinit();
        xmlonclick();
        callcategoryapi();
    }

        private void callcategoryapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Addparentcatapi , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("category");
                        pcategorylist=new ArrayList<>();
                        if (status.equals("success")) {
                        JSONArray jsonArray = jobjMain.getJSONArray("category");

                         for(int i=0;i<jsonArray.length();i++){
                         JSONObject jobj=jsonArray.getJSONObject(i);
                         Parentcategorymodelclass pcmc=new Parentcategorymodelclass(jobj.getString("category_id"),
                                 jobj.getString("category_sellerid"),jobj.getString("category_name"));

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
            VolleySingleton.getInstance(AddProductcategory.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }

   }

    private void setadapter() {
    Productcategoryadapter padapter=new Productcategoryadapter(AddProductcategory.this,pcategorylist,R.layout.catgry_item);
    cat_add.setAdapter(padapter);

    cat_add.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

           if(i>0){
           position=Integer.parseInt(pcategorylist.get(i-1).category_id);

           }


        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    });


        /*cat_add.post(new Runnable() {
            @Override public void run() {
                cat_add.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        // Only called when the user changes the selection
                        position=Integer.parseInt(pcategorylist.get(position).category_id);

                       }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
*/



    }










    private void xmlonclick() {
    btn_submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Addcatapi();
        }
    });
    }

        private void Addcatapi() {
        catname_str = catname.getText().toString();
        catdesc_str = catdesc.getText().toString();

        if (catname_str.equals("")) {
            showSnackBar("Please Enter Category Name");
        } else if (catdesc_str.equals("")) {
            showSnackBar("Enter  Category Description");
        }
        else {
            catapi();


        }





    }

        private void catapi() {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Selleraddcat , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");

                        if (status.equals("success"))
                            Toast.makeText(AddProductcategory.this, msg, Toast.LENGTH_SHORT).show();
                             stopProgress();
                         Intent i = new Intent(AddProductcategory.this, Productcategory.class);
                        startActivity(i);


                    } catch (JSONException e) {
                        e.printStackTrace();

                    }

                }
            }, new Response.ErrorListener() {
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("seller_id", psh.getSellerid());
                    params.put("parent_category_name","2");
                    params.put("category_name", catname_str);
                    params.put("category_desc", catdesc_str);


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
            VolleySingleton.getInstance(AddProductcategory.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }



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

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(AddProductcategory.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void showSnackBar(String msg) {
   Toast.makeText(AddProductcategory.this,msg,Toast.LENGTH_LONG).show();

    }

    private void xmlinit() {
    cat_add=(Spinner)findViewById(R.id.parent_category);
    catname=(EditText)findViewById(R.id.catname);
    catdesc=(EditText)findViewById(R.id.catdesc);
    btn_submit=(Button)findViewById(R.id.btn_submit);
        /*List<String> categories = new ArrayList<String>();
        categories.add("Ladies Jeans");
        categories.add("Mens Accessories");
         // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        cat_add.setAdapter(dataAdapter);
        position= cat_add.getSelectedItemPosition();
*/

    }
}