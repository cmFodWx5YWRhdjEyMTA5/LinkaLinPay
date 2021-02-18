package com.keo.onsite.linkalinpay.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
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
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NothingSelectedSpinnerAdapter implements SpinnerAdapter, ListAdapter, AdapterView.OnItemSelectedListener {
    protected static final int EXTRA = 1;
    protected SpinnerAdapter adapter;
    protected Context context;
    protected int nothingSelectedLayout;
    protected int nothingSelectedDropdownLayout;
    protected LayoutInflater layoutInflater;
    String status,inv_id;
    private ProgressDialog progressDialog;
    /**
     * Use this constructor to have NO 'Select One...' item, instead use
     * the standard prompt or nothing at all.
     * @param spinnerAdapter wrapped Adapter.
     * @param nothingSelectedLayout layout for nothing selected, perhaps
     * you want text grayed out like a prompt...
     * @param context
     */
    public NothingSelectedSpinnerAdapter(
            SpinnerAdapter spinnerAdapter,
            int nothingSelectedLayout, Context context,String inv_id) {

        this(spinnerAdapter, nothingSelectedLayout, -1, context,inv_id);
    }

    /**
     * Use this constructor to Define your 'Select One...' layout as the first
     * row in the returned choices.
     * If you do this, you probably don't want a prompt on your spinner or it'll
     * have two 'Select' rows.
     * @param spinnerAdapter wrapped Adapter. Should probably return false for isEnabled(0)
     * @param nothingSelectedLayout layout for nothing selected, perhaps you want
     * text grayed out like a prompt...
     * @param nothingSelectedDropdownLayout layout for your 'Select an Item...' in
     * the dropdown.
     * @param context
     */
    public NothingSelectedSpinnerAdapter(SpinnerAdapter spinnerAdapter,
                                         int nothingSelectedLayout, int nothingSelectedDropdownLayout, Context context,String inv_id) {
        this.adapter = spinnerAdapter;
        this.context = context;
        this.nothingSelectedLayout = nothingSelectedLayout;
        this.nothingSelectedDropdownLayout = nothingSelectedDropdownLayout;
        layoutInflater = LayoutInflater.from(context);
        this.inv_id=inv_id;

    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        // This provides the View for the Selected Item in the Spinner, not
        // the dropdown (unless dropdownView is not set).
        if (position == 0) {
            return getNothingSelectedView(parent);
        }
        return adapter.getView(position - EXTRA, null, parent); // Could re-use
        // the convertView if possible.
    }

    /**
     * View to show in Spinner with Nothing Selected
     * Override this to do something dynamic... e.g. "37 Options Found"
     * @param parent
     * @return
     */
    protected View getNothingSelectedView(ViewGroup parent) {
        return layoutInflater.inflate(nothingSelectedLayout, parent, false);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        // Android BUG! http://code.google.com/p/android/issues/detail?id=17128 -
        // Spinner does not support multiple view types
        if (position == 0) {
            return nothingSelectedDropdownLayout == -1 ?
                    new View(context) :
                    getNothingSelectedDropdownView(parent);
        }

        // Could re-use the convertView if possible, use setTag...
        return adapter.getDropDownView(position - EXTRA, null, parent);
    }

    /**
     * Override this to do something dynamic... For example, "Pick your favorite
     * of these 37".
     * @param parent
     * @return
     */
    protected View getNothingSelectedDropdownView(ViewGroup parent) {
        return layoutInflater.inflate(nothingSelectedDropdownLayout, parent, false);
    }

    @Override
    public int getCount() {
        int count = adapter.getCount();
        return count == 0 ? 0 : count + EXTRA;
    }

    @Override
    public Object getItem(int position) {
        return position == 0 ? null : adapter.getItem(position - EXTRA);
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position >= EXTRA ? adapter.getItemId(position - EXTRA) : position - EXTRA;
    }

    @Override
    public boolean hasStableIds() {
        return adapter.hasStableIds();
    }

    @Override
    public boolean isEmpty() {
        return adapter.isEmpty();
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
        adapter.registerDataSetObserver(observer);
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
        adapter.unregisterDataSetObserver(observer);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return position != 0; // Don't allow the 'nothing selected'
        // item to be picked.
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        status = adapterView.getItemAtPosition(i).toString();
        callstatusapi();

    }

    private void callstatusapi() {
        if (VolleySingleton.getInstance(context).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.sellerupdatequickpay , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
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
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("inv_id", inv_id);
                    params.put("inv_status", status);


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
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
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
    private void stopProgress() {
    }

    public void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(context, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(context.getApplicationContext().getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
    private void showSnackBar(String msg) {
    Toast.makeText(context,msg,Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
