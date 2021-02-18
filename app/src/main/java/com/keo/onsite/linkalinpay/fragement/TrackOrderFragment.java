package com.keo.onsite.linkalinpay.fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import com.keo.onsite.linkalinpay.activity.AddInvoiceActivity;
import com.keo.onsite.linkalinpay.activity.InvoiceActivity;
import com.keo.onsite.linkalinpay.activity.TrackOrderActivity;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TrackOrderFragment extends Fragment {
    View root;
    private ProgressDialog progressDialog;
     String orderid;
     UserShared psh;
    TextView  orderdate;
            //ordertime;
    EditText orderid_edt;
    Button submit;

    public TrackOrderFragment() {
        // Required empty public constructor
    }



    /*@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            orderid = arguments.get("orderid").toString();

        }


    }
*/
    @Override

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

     root = inflater.inflate(R.layout.fragment_slideshow, container, false);
       psh=new UserShared(getActivity());

         xmlinit();
         xmlonclik();

        //trackorderapi();
         return root;
    }

    private void xmlonclik() {
    submit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
         orderid=orderid_edt.getText().toString();
        Intent i=new Intent(getActivity(), TrackOrderActivity.class);
        i.putExtra("orderid",orderid);

            getActivity().startActivity(i);

        }
    });


    }

    private void trackorderapi() {
        if (VolleySingleton.getInstance(getActivity()).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.trackorderapi , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                       // String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {

                            Log.d("Trackorder>>>",response.toString());
//                           JSONObject jobj=jobjMain.getJSONObject("trackorderdetail");
//                           orderdate.setText(jobj.getString("order_date"));
                            //ordertime.setText(jobj.getString(""));

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
                    params.put("order_id", "5626");
                    params.put("cust_id", psh.getCustomerid());


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
            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
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

    private void showProgress() {

    }

    private void xmlinit() {
    orderid_edt=(EditText)root.findViewById(R.id.orderid);
    orderdate=(TextView)root.findViewById(R.id.orderdate);
//    ordertime=(TextView)root.findViewById(R.id.ordertime);
    submit=(Button)root.findViewById(R.id.submit);
     }
}