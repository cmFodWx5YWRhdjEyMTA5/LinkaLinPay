package com.keo.onsite.linkalinpay.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

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
import com.keo.onsite.linkalinpay.activity.EditProductselleractivity;
import com.keo.onsite.linkalinpay.activity.Productcategory;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Productcategorymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Productcategoryadapte extends RecyclerView.Adapter<Productcategoryadapte.ViewHolder> {
    //this context we will use to inflate the layout
    private Context mCtx;
    //we are storing all the products in a list
    private List<Productcategorymodelclass> productList;
    //getting the context and product list with constructor
    private ProgressDialog progressDialog;
     String categoryid;
    public Button status_btn;
    String Category_status;
    Translator t;
    public Productcategoryadapte(Context mCtx, List<Productcategorymodelclass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
        t =  new Translator();
        t.getTranslateService(mCtx);
    }

    @Override
    public Productcategoryadapte.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cat_item, null);
        return new Productcategoryadapte.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Productcategoryadapte.ViewHolder holder, final int position) {
        //binding the data with the viewholder views
       if(productList.get(position).parent_categoryname.equals("")){
           holder.parentcategory.setText(t.translate(mCtx,"No Category"));
           holder.subcategory.setText(t.translate(mCtx,productList.get(position).category_name));
          // holder.status.setText(productList.get(position).category_status);


       }else {

           holder.parentcategory.setText(t.translate(mCtx,productList.get(position).parent_categoryname));
           holder.subcategory.setText(t.translate(mCtx,productList.get(position).category_name));
           //holder.status.setText(productList.get(position).category_status);
       }

       holder. delete.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               calldeleteapi();
               remove(position);

           }
       });

        categoryid=productList.get(position).category_id;
        Category_status=productList.get(position).category_status;

      holder.edit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
          Intent i=new Intent(mCtx, EditProductselleractivity.class);
           i.putExtra("pcatid",productList.get(position).category_id);
           i.putExtra("pcatname",productList.get(position).parent_categoryname);
           i.putExtra("catname",productList.get(position).category_name);
           i.putExtra("catdesc",productList.get(position).category_desc);



          mCtx.startActivity(i);
          }
      });

   /* status_btn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            changestatusapi();
        }
    });*/

    }

    /*private void changestatusapi() {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Sellercatatatusapi ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg=jsonObject.getString("msg");

                                if (status.equals("success")){
                                    Toast.makeText(mCtx,msg,Toast.LENGTH_LONG).show();
                                    stopProgress();

                                   if(jsonObject.getString("category_status").equals("inactive")){
                                    status_btn.setText("INACTIVE");

                                    status_btn.setBackgroundColor(Color.parseColor("#FF0000"));

                                   }else{
                                       status_btn.setText("ACTIVE");

                                   }
                                    //notifyDataSetChanged();

                                }

                                else {
                                    Toast.makeText(mCtx,msg,Toast.LENGTH_LONG).show();
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
                    params.put("category_id", categoryid);
                    params.put("category_status", "inactive");
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
            VolleySingleton.getInstance(mCtx).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(mCtx, "No internet connection", Toast.LENGTH_SHORT).show();
        }



    }*/

    private void remove(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());

    }

    private void calldeleteapi() {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Sellerdelcat ,

                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {


                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                String status = jsonObject.getString("status");
                                String msg=jsonObject.getString("msg");

                                if (status.equals("success")){
                                    Toast.makeText(mCtx,t.translate(mCtx,msg),Toast.LENGTH_LONG).show();
                                    stopProgress();


                                    //notifyDataSetChanged();

                                }

                                else {
                                    Toast.makeText(mCtx,msg,Toast.LENGTH_LONG).show();
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
                    params.put("category_id", categoryid);
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
            VolleySingleton.getInstance(mCtx).addToRequestQueue(strRequest);
        } else {
            Toast.makeText(mCtx, "No internet connection", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(mCtx, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(mCtx.getResources().getString(R.string.loadingplzwait)));
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
    Toast.makeText(mCtx,msg,Toast.LENGTH_LONG).show();

    }


    @Override
    public int getItemCount() {
        return productList.size();
    }


 public class ViewHolder extends RecyclerView.ViewHolder {

        TextView parentcategory, subcategory;
        TextView status;
        TextView delete,edit;

     public RelativeLayout view_foreground,view_background;
        public ViewHolder(View itemView) {
            super(itemView);
            //view_background = itemView.findViewById(R.id.view_background);
            //view_foreground = itemView.findViewById(R.id.view_foreground);
            parentcategory = itemView.findViewById(R.id.parentcategory);
            subcategory = itemView.findViewById(R.id.subcategory);
          //  status = itemView.findViewById(R.id.status);
            delete=itemView.findViewById(R.id.delete);
            edit=itemView.findViewById(R.id.edit);
           // status_btn=itemView.findViewById(R.id.status);

        }
    }

    public void removeItem(int position) {
        productList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(Productcategorymodelclass item, int position) {
        productList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }


}
