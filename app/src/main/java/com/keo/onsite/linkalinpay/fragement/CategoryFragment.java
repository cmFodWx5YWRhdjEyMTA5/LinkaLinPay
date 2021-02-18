package com.keo.onsite.linkalinpay.fragement;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.keo.onsite.linkalinpay.activity.FragmentCommunication;
import com.keo.onsite.linkalinpay.activity.model.Bannermodelclass;
import com.keo.onsite.linkalinpay.activity.model.Categorymodelclass;
import com.keo.onsite.linkalinpay.activity.model.Locationmodelclass;
import com.keo.onsite.linkalinpay.activity.model.Vendorlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.Locationadapter;
import com.keo.onsite.linkalinpay.adapter.SellerCategoryadapter;
import com.keo.onsite.linkalinpay.adapter.Sellerlistadapter;
import com.keo.onsite.linkalinpay.adapter.Vendorlistadapter;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class  CategoryFragment extends Fragment {

    View view;
    Spinner search_spinner;
    ViewPager viewPager;
    ArrayList<Bannermodelclass> bannerlist;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ProgressDialog progressDialog;
    ArrayList<Categorymodelclass>catmodelclass;
    SellerCategoryadapter sadapter;
    ArrayList<Locationmodelclass>locationlist;
    Locationadapter ladapter;
    Spinner spinner1,sorting;
    String[] sort={"a to z","z to a"};
    String location,category,sorting_str;
   ArrayList<Vendorlistmodelclass>sellerlist;
    Vendorlistadapter vadpter;
   RecyclerView recyclerview;
    UserShared psh;
    int position=0;
    String loc,sellerid,sort_str;
    SharedPreferences prefs;

    int first_visit_1 = 0;
    int first_visit_2 = 0;
    int first_visit_3 = 0;


    ArrayList<String> cat_name = new ArrayList<>();
    ArrayList<String> cat_id = new ArrayList<>();
    ArrayList<String> loc_name = new ArrayList<>();
    ArrayList<String> loc_id = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
           view= inflater.inflate(R.layout.fragment_category, container, false);
           psh=new UserShared(getActivity());
           prefs=getActivity().getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);

           xmlinit();






          callfinalapi();
          callviewpagerapi();
           categoryapi();
           locationapi();

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(getString(R.string.shared_vendor_id), sellerid);
        editor.putBoolean(getString(R.string.shared_loggedin_status_customer), true);
        editor.commit();
           return view;
    }


        private void categoryapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()){
            showProgress();
            StringRequest stringRequest=new StringRequest(Request.Method.POST,
                    Constants.catcustapi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status=jobjMain.getString("status");
                        catmodelclass = new ArrayList<Categorymodelclass>();
                          if (status.equals("success")) {
                            JSONArray jarray=jobjMain.getJSONArray("category");
                            for(int i=0;i<jarray.length();i++){
                                JSONObject jobj=jarray.getJSONObject(i);
                                Categorymodelclass bmc=new Categorymodelclass(jobj.getString("category_id"),
                                        jobj.getString("category_name"),jobj.getString("category_status"));
                                catmodelclass.add(bmc);
                            }

                                setadapter_cat();
                             //============
                            stopProgress();
                        }
                        else{
                            //Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                            stopProgress();

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();


                    }

                }
            }, new Response.ErrorListener() {
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
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    //params.put("password", password_str);
                    //params.put("name", name_str);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    //headers.put("x-api-key", "8fe7268ecbcec47fe11fa8fcb3a09470");
                    // headers.put("header2", "header2");

                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


    }

     private void setadapter_cat() {
        try {


            for(int i = 0 ; i < catmodelclass.size();i++)
            {
                cat_name.add(catmodelclass.get(i).category_name);
                cat_id.add(catmodelclass.get(i).category_id);
            }

         //   sadapter = new SellerCategoryadapter(getContext(), cat_name, android.R.layout.simple_spinner_item);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cat_name);
            search_spinner.setAdapter(dataAdapter);

            //search_spinner.setAdapter(sadapter);
            search_spinner.post(new Runnable() {
                @Override public void run() {
                    search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int positions, long id) {
                            // Only called when the user changes the selection
                          //  position=Integer.parseInt(catmodelclass.get(position).category_id);

                            position = Integer.parseInt(cat_id.get(positions));

                                callsortingapi();

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }
            });
        }
        catch (Exception e){}








        /*search_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                 position=Integer.parseInt(catmodelclass.get(i).category_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/


    }

           private void callviewpagerapi() {
            if (VolleySingleton.getInstance(getActivity()).isConnected()){
                showProgress();
                StringRequest stringRequest=new StringRequest(Request.Method.POST,
                        Constants.Appbanerlist, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jobjMain = new JSONObject(response);
                            String status=jobjMain.getString("status");
                            bannerlist = new ArrayList<Bannermodelclass>();

                            // String msg=jobjMain.getString("message");

                            if (status.equals("success")) {
                                JSONArray jarray=jobjMain.getJSONArray("banner");
                                for(int i=0;i<jarray.length();i++){
                                    JSONObject jobj=jarray.getJSONObject(i);
                                    Bannermodelclass bmc=new Bannermodelclass(jobj.getString("id"),
                                            jobj.getString("banner_image"));
                                    bannerlist.add(bmc);
                                }
                                viewPager.setAdapter(new StaticSlidingImage_Adapter(getActivity(), bannerlist));
                                NUM_PAGES = bannerlist.size();

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
                                stopProgress();

                            }
                            else{
                                //Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                                stopProgress();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();


                        }

                    }
                }, new Response.ErrorListener() {
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
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();

                         //params.put("seller_id", "1");
                        //params.put("password", password_str);
                        //params.put("name", name_str);

                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        //headers.put("x-api-key", "8fe7268ecbcec47fe11fa8fcb3a09470");
                        // headers.put("header2", "header2");

                        return headers;
                    }
                };

                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        0,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                 VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
            } else {
                //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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
        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    public void parseVolleyError(VolleyError error) {
//        try {
//            String responseBody = new String(error.networkResponse.data, "utf-8");
//            JSONObject data = new JSONObject(responseBody);
//            String errors = data.getString("error");
//            Log.e("VolleyError", errors);
//            showSnackBar(errors);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    private void showSnackBar(String msg) {
//    Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

    }


     private void xmlinit() {
     viewPager = (ViewPager)view. findViewById(R.id.viewPager);
     setHasOptionsMenu(true);
     search_spinner=view.findViewById(R.id.search_spinner);
     //search_spinner.setTitle("Choose a specific category");
      spinner1 = view.findViewById(R.id.location_spinner);
      sorting=view.findViewById(R.id.sorting);

      recyclerview=(RecyclerView)view.findViewById(R.id.recyclerview);
         LinearLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2 );
         recyclerview.setLayoutManager(layoutManager);
      // Creating adapter for spinner
         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, sort);
         // Drop down layout style - list view with radio button
         sorting.setAdapter(dataAdapter);
         /*sorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
             sort_str=adapterView.getSelectedItem().toString();
                callsortingapi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
*/
         // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //sorting_str= sorting.getSelectedItem().toString();
         sorting.post(new Runnable() {
             @Override public void run() {
                 sorting.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                     @Override
                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                         // Only called when the user changes the selection
                         sort_str=parent.getSelectedItem().toString();

                         callsortingapi();

                     }

                     @Override
                     public void onNothingSelected(AdapterView<?> parent) {
                     }
                 });
             }
         });




     }

       private void callsortingapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()){
            showProgress();
            StringRequest stringRequest=new StringRequest(Request.Method.POST,
                    Constants.filterlistapi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("category_resp>>>",response);
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status=jobjMain.getString("status");
                        String msg=jobjMain.getString("msg");
                        sellerlist = new ArrayList<Vendorlistmodelclass>();
                        if (status.equals("success")) {
                            Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

                            JSONArray jarray=jobjMain.getJSONArray("shoppers");
                            for(int i=0;i<jarray.length();i++){
                                JSONObject jobj=jarray.getJSONObject(i);
                                Vendorlistmodelclass bmc=new Vendorlistmodelclass(jobj.getString("seller_id"),
                                        jobj.getString("seller_logo"),jobj.getString("seller_name"),
                                        jobj.getString("seller_temp_store_name"),
                                        jobj.getString("location"),jobj.getString("category_id"),jobj.getString("totalproduct"));
                                     sellerlist.add(bmc);
                                //sellerid=jobj.getString("seller_id");
                                //SharedPreferences.Editor editor = prefs.edit();
                                //editor.putString(getString(R.string.shared_seller_id), sellerid);
                                //editor.commit();
                      }

                            filteradapter();
                           // setVendorlistadapter();
                            //============
                            stopProgress();

                        }
                        else{
                            //Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                            stopProgress();

                        }
                       // filteradapter();
                    }catch (JSONException e) {
                        e.printStackTrace();


                    }

                }
            }, new Response.ErrorListener() {
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
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("category", position == 0 ? "" : String.valueOf(position) );
                    params.put("location", loc == null ? "" : loc);
                    params.put("sortby", sort_str == null ? "" : sort_str.replaceAll("\\s", "") );

/*String.valueOf(position)*/
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    //headers.put("x-api-key", "8fe7268ecbcec47fe11fa8fcb3a09470");
                    // headers.put("header2", "header2");

                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

   }

    private void filteradapter() {

        vadpter=new Vendorlistadapter(getActivity(),sellerlist);

//        vadpter.filter();
        recyclerview.setAdapter(vadpter);
    }

    private void callfinalapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()){
            showProgress();
            StringRequest stringRequest=new StringRequest(Request.Method.POST,
                    Constants.Sellerlistapi, new Response.Listener<String>()
            {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status=jobjMain.getString("status");
                        String msg=jobjMain.getString("msg");
                        sellerlist = new ArrayList<Vendorlistmodelclass>();
                        if (status.equals("success")) {
                        //  Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();

                            JSONArray jarray=jobjMain.getJSONArray("shoppers");
                            for(int i=0;i<jarray.length();i++){
                                JSONObject jobj=jarray.getJSONObject(i);
                                sellerid=jobj.getString("seller_id");
                                Vendorlistmodelclass bmc=new Vendorlistmodelclass(jobj.getString("seller_id"),
                                       jobj.getString("seller_logo"),jobj.getString("seller_name"),
                                        jobj.getString("seller_temp_store_name"),
                                        jobj.getString("location"),jobj.getString("category_id"),jobj.getString("totalproduct"));
                                        sellerlist.add(bmc);



                            }





                            setVendorlistadapter();
                            //============
                            stopProgress();

                        }
                        else{
                            //Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                            stopProgress();

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();


                    }

                }
            }, new Response.ErrorListener() {
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
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("category", "");
                    params.put("location", "");
                    params.put("sortby", "");

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    //headers.put("x-api-key", "8fe7268ecbcec47fe11fa8fcb3a09470");
                    // headers.put("header2", "header2");

                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void setVendorlistadapter() {
        try {
            vadpter=new Vendorlistadapter(getActivity(),sellerlist);

            recyclerview.setAdapter(vadpter);

        }
        catch (Exception e){}
       }

    private void locationapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()){
            showProgress();
            StringRequest stringRequest=new StringRequest(Request.Method.POST,
                    Constants.locationapi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status=jobjMain.getString("status");
                        locationlist = new ArrayList<Locationmodelclass>();
                        if (status.equals("success")) {
                            JSONArray jarray=jobjMain.getJSONArray("location");
                            for(int i=0;i<jarray.length();i++){
                                JSONObject jobj=jarray.getJSONObject(i);
                                Locationmodelclass bmc=new Locationmodelclass(jobj.getString("id"),
                                        jobj.getString("location"),jobj.getString("status"));
                                locationlist.add(bmc);
                            }
                            setlocadapter();
                            //============
                            stopProgress();

                        }
                        else{
                            //Toast.makeText(SignupActivity.this, msg, Toast.LENGTH_SHORT).show();
                            stopProgress();

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();


                    }

                }
            }, new Response.ErrorListener() {
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
            }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    //params.put("password", password_str);
                    //params.put("name", name_str);

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<>();
                    //headers.put("x-api-key", "8fe7268ecbcec47fe11fa8fcb3a09470");
                    // headers.put("header2", "header2");

                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    0,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

         private void setlocadapter() {

        try {
            for(int i = 0 ; i < locationlist.size();i++)
            {
                loc_name.add(locationlist.get(i).location);
                loc_id.add(locationlist.get(i).id);
            }

            //   sadapter = new SellerCategoryadapter(getContext(), cat_name, android.R.layout.simple_spinner_item);
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, loc_name);

           // ladapter = new Locationadapter(getActivity(), locationlist, R.layout.catgry_item);
            spinner1.setAdapter(dataAdapter);
            spinner1.post(new Runnable() {
                @Override public void run() {
                    spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // Only called when the user changes the selection
                           // loc=locationlist.get(position).location;
                               loc = loc_name.get(position);

                                callsortingapi();



                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            });
        }
        catch (Exception e){}







             /*spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loc=locationlist.get(i).location;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.cart);
        if(item!=null)
            item.setVisible(false);
    }



        //=========view pager adapter setting=============
    public class StaticSlidingImage_Adapter extends PagerAdapter {

        private ArrayList<Bannermodelclass> IMAGES;
        private LayoutInflater inflater;

        public StaticSlidingImage_Adapter(Context context, ArrayList<Bannermodelclass> IMAGES) {
            this.IMAGES=IMAGES;
            try {
                inflater = LayoutInflater.from(getActivity().getApplicationContext());

            }
            catch (Exception e){}
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

            Glide.with(getActivity())
                    .load(IMAGES.get(position).banner_image)
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