package com.keo.onsite.linkalinpay.adapter;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.keo.onsite.linkalinpay.activity.BannerlistActivity;
import com.keo.onsite.linkalinpay.activity.Productcategory;
import com.keo.onsite.linkalinpay.activity.model.Bannerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bannerlistadapter extends RecyclerView.Adapter<Bannerlistadapter.ViewHolder> {
    //this context we will use to inflate the layout
      private Context mCtx;
      //we are storing all the products in a list
     private List<Bannerlistmodelclass> productList;
    //getting the context and product list with constructor
     private ProgressDialog progressDialog;
      String banner_id;
      String status;
     Translator t;


      public Bannerlistadapter(Context mCtx, List<Bannerlistmodelclass> productList) {
        this.mCtx = mCtx;
        this.productList = productList;
          Translator t =  new Translator();
          t.getTranslateService(mCtx);


      }

    @Override
    public Bannerlistadapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.cat_item_layout, null);
        return new Bannerlistadapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Bannerlistadapter.ViewHolder holder, final int position) {

        //binding the data with the viewholder views
        holder.bannername.setText(t.translate(mCtx,productList.get(position).banner_title));
        holder.description.setText(t.translate(mCtx,productList.get(position).banner_desc));
        String IMg = productList.get(position).banner_image;
        banner_id=productList.get(position).banner_id;
      holder.status_btn.setText(t.translate(mCtx,productList.get(position).banner_status));
        holder.statusicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callstatusapi();
            }
        });
        status=productList.get(position).banner_status;


        if (!IMg.equals("")) {
            try {
                //String apiLink = context.getResources().getString(R.string.banner)+IMg;
                String apiLink = IMg;
                //Log.d("User Profile Image Parsing", "apiLink:"+apiLink);
                String encodedurl = "";
                encodedurl = apiLink.substring(0, apiLink.lastIndexOf('/')) + "/" + Uri.encode(apiLink.substring(
                        apiLink.lastIndexOf('/') + 1));
                Log.d("User", "encodedurl:" + encodedurl);
                if (!apiLink.equals("") && apiLink != null) {
                    Picasso.with(mCtx)
                            .load(encodedurl) // load: This path may be a remote URL,
                            .placeholder(R.drawable.defaultpic)
                            //.resize(130, 130)
                            .error(R.drawable.defaultpic)
                            .into(holder.imageView); // Into: ImageView into which the final image has to be passed
                    //  .resize(130, 130)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        holder. delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calldeleteapi();
                remove(position);

            }
        });



    }

    private void callstatusapi() {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();




            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Bannerstatus ,



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
                                    //status_inactive.setVisibility(View.VISIBLE);
                                    //status_btn.setVisibility(View.GONE);
                                }

                                else {
                                    Toast.makeText(mCtx,t.translate(mCtx,msg),Toast.LENGTH_LONG).show();
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
                    params.put("banner_id", banner_id);
                    params.put("banner_status", status);
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

    private void remove(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
    }

    private void calldeleteapi() {
        if (VolleySingleton.getInstance(mCtx).isConnected()) {
            showProgress();
            StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.Bannerdelete ,

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
                    params.put("banner_id", banner_id);
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

    @Override
    public int getItemCount() {
        return productList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView bannername, description;
        ImageView imageView,delete,statusicon;
        Button status_btn,status_inactive;
        public ViewHolder(View itemView) {
            super(itemView);

            bannername = itemView.findViewById(R.id.text);
            imageView = itemView.findViewById(R.id.image);
            description = itemView.findViewById(R.id.desc);
            delete=itemView.findViewById(R.id.delete);
            status_btn=itemView.findViewById(R.id.status);
            statusicon=itemView.findViewById(R.id.statusicon);
            status_inactive=itemView.findViewById(R.id.status_inactive);
        }
    }
}









