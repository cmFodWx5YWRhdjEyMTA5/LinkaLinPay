package com.keo.onsite.linkalinpay.fragement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.keo.onsite.linkalinpay.activity.HelpSupport;
import com.keo.onsite.linkalinpay.activity.SubmitQuery;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class HelpSupportFragment extends Fragment {
  EditText fullname_edt,email_edt,phonenumber_edt,message_edt;
    Button btn_submit;
  String fullname_str,email_str,phonenumber_str,message_str;
  View view;
   private ProgressDialog progressDialog;
   UserShared psh;
   LinearLayout call,submitQuery,whatsapp;

  public HelpSupportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_help_support, container, false);
//         xmlinit();
//         xmlonclik();
        call = view.findViewById(R.id.call);
        submitQuery = view.findViewById(R.id.submitQuery);
        whatsapp = view.findViewById(R.id.whatsapp);


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                call(view);
            }
        });

        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                whatsapp(view);
            }
        });

        submitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                submitQuery(view);
            }
        });

        return view;
  }



    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
    }

    public void whatsapp(View view) {
        String contact = "+91 1111111"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getActivity().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(getActivity(), "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void submitQuery(View view) {

        Intent intent = new Intent(getActivity(), SubmitQuery.class);
        startActivity(intent);


    }

  
  
//    private void xmlonclik() {
//      btn_submit.setOnClickListener(new View.OnClickListener() {
//          @Override
//          public void onClick(View view) {
//
//              fullname_str=fullname_edt.getText().toString();
//              email_str=email_edt.getText().toString();
//              phonenumber_str=phonenumber_edt.getText().toString();
//              message_str=message_edt.getText().toString();
//              if (fullname_str.equals("")) {
//                  showSnackBar("Enter Full Name");
//              } else if (email_str.equals("")) {
//                  showSnackBar("Enter Email Address");
//              }else if (phonenumber_str.equals("")) {
//                  showSnackBar("Enter Phone Number");
//              }else if (message_str.equals("")) {
//                  showSnackBar("Enter Message");
//              }
//
//
//              else {
//                  supportapi();
//
//
//              }
//
//
//
//
//          }
//      });
//
//    }
//
//    private void supportapi() {
//        if (VolleySingleton.getInstance(getActivity()).isConnected()) {
//            showProgress();
//            StringRequest stringRequest = new StringRequest(Request.Method.POST,
//                    Constants.supportapi, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    try {
//
//                        JSONObject jobjMain = new JSONObject(response);
//                        String status = jobjMain.getString("status");
//                        String msg = jobjMain.getString("msg");
//                        if (status.equals("success")) {
//                            Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
//                            stopProgress();
//                            // Intent i=new Intent(AddInvoiceActivity.this,InvoiceActivity.class);
//                            //startActivity(i);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//
//                    }
//
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    //parseVolleyError(error);
//
//                    if (error instanceof NetworkError) {
//                        //Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
//                        showSnackBar("No internet connection");
//                    } else if (error instanceof ParseError) {
//
//                        showSnackBar("No internet connection");
//                    } else if (error instanceof NoConnectionError) {
//
//                        showSnackBar("No internet connection");
//                    } else {
//
//                        parseVolleyError(error);
//
//                        stopProgress();
//                    }
//                }
//            }) {
//                @Override
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("cust_id ",psh.getCustomerid() );
//                    params.put("name",fullname_str );
//                    params.put("email", email_str);
//                    params.put("mobile", phonenumber_str);
//                    params.put("subject", "hii");
//                    params.put("message", message_str);
//
//
//                    return params;
//                }
//
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    HashMap<String, String> headers = new HashMap<>();
//                    // headers.put(Constants.authKEY, Constants.authValue);
//                    // headers.put("header2", "header2");
//
//                    return headers;
//                }
//            };
//
//            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                    0,
//                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//            VolleySingleton.getInstance(getActivity()).addToRequestQueue(stringRequest);
//        } else {
//            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
//        }
//
//
//
//    }
//
//    private void parseVolleyError(VolleyError error) {
//
//        try {
//            String responseBody = new String(error.networkResponse.data, "utf-8");
//            JSONObject data = new JSONObject(responseBody);
//            String errors = data.getString("error");
//            Log.e("VolleyError", errors);
//            showSnackBar(errors);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//     private void stopProgress() {
//        if (progressDialog != null && progressDialog.isShowing()) {
//            progressDialog.dismiss();
//        }
//
//    }
//
//    private void showProgress() {
//        if (progressDialog != null)
//            progressDialog.dismiss();
//        progressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
//        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
//        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//
//    }
//
//    private void showSnackBar(String msg) {
//    Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
//   }
//
//    private void xmlinit() {
//    psh=new UserShared(getActivity());
//    fullname_edt=(EditText)view.findViewById(R.id.fullname);
//    email_edt=(EditText)view.findViewById(R.id.email_edt);
//    phonenumber_edt=(EditText)view.findViewById(R.id.phonenumber_edt);
//    message_edt=(EditText)view.findViewById(R.id.message);
//    btn_submit=(Button) view.findViewById(R.id.btn_submit);
//
//
//  }
}