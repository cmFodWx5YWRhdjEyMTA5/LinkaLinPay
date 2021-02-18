package com.keo.onsite.linkalinpay.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
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

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.Utility;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.utils.Translator;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;



public class InformationActivity extends AppCompatActivity {
    EditText fullname, email, mobilenumber,
            businessname, instagramname, username,
            description,termshopname,sellerdomain,
            selleraddress,sellerlocation,desciption;

    String fullname_str,email_str,mobilenumber_str,
            businessname_str,instagramname_str,username_str,
            description_str,termshopname_str,sellerdomain_str,selleraddress_str,
            desciption_str;
//
    GridLayout gridcity, gridsellerlocation, gridcountry, gridstate;

    UserShared psh;
    Spinner spinner;
    CircleImageView imageview;
    ImageView edit;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private Uri picturepath;
    Button save;
    private MultipartEntity reqEntity;
    ConnectionDetector connection;
    private ProgressDialog progressDialog;
    String responseString = null;
    private File profile_upload_file;
    private static final String TAG = "";

    Translator t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.accountinfo);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        psh = new UserShared(this);
        connection=new ConnectionDetector(this);
        xmlinit();
        xmlonclick();
        checkboxes();
    }

    private void checkboxes() {

        //select location

        t = new Translator();
        t.getTranslateService(getApplicationContext());
        if (VolleySingleton.getInstance(this).isConnected()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.alllocation , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        Log.d("Response>>>",response);
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
//                            Toast.makeText(InformationActivity.this,msg,Toast.LENGTH_LONG).show();

                            JSONArray slots =  jobjMain.getJSONArray("data");
                            LayoutInflater li =  (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            for(int i = 0;i < slots.length();i++ ) {
                                Log.d("Response>>>",slots.toString());

                                View tempView = li.inflate(R.layout.item_checkbox, null);

                                CheckBox cb = tempView.findViewById(R.id.listid);

                                cb.setText(t.translate(getApplicationContext(),slots.getJSONObject(i).getString("location")));



                                gridsellerlocation.addView(tempView);

                            }




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


                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
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
            VolleySingleton.getInstance(InformationActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        //country


        if (VolleySingleton.getInstance(this).isConnected()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.allcountry , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        Log.d("Response>>>",response);
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
//                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
 //                           Toast.makeText(InformationActivity.this,msg,Toast.LENGTH_LONG).show();

                            JSONArray slots =  jobjMain.getJSONArray("country");
                            LayoutInflater li =  (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            for(int i = 0;i < slots.length();i++ ) {
                                Log.d("Response>>>",slots.toString());

                                View tempView = li.inflate(R.layout.item_checkbox, null);

                                CheckBox cb = tempView.findViewById(R.id.listid);

                                cb.setText(t.translate(getApplicationContext(),slots.getJSONObject(i).getString("name")));



                                gridcountry.addView(tempView);

                            }




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


                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
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
            VolleySingleton.getInstance(InformationActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

// state

        if (VolleySingleton.getInstance(this).isConnected()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.allstate , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        Log.d("Response>>>",response);
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
//                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            //                           Toast.makeText(InformationActivity.this,msg,Toast.LENGTH_LONG).show();

                            JSONArray slots =  jobjMain.getJSONArray("data");
                            LayoutInflater li =  (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            for(int i = 0;i < slots.length();i++ ) {
                                Log.d("Response>>>",slots.toString());

                                View tempView = li.inflate(R.layout.item_checkbox, null);

                                CheckBox cb = tempView.findViewById(R.id.listid);

                                cb.setText(slots.getJSONObject(i).getString("name"));



                                gridstate.addView(tempView);

                            }




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


                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
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
            VolleySingleton.getInstance(InformationActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }


        //city

        if (VolleySingleton.getInstance(this).isConnected()) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.allcity , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        Log.d("Response>>>",response);
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
//                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            //                           Toast.makeText(InformationActivity.this,msg,Toast.LENGTH_LONG).show();

                            JSONArray slots =  jobjMain.getJSONArray("data");
                            LayoutInflater li =  (LayoutInflater)getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            for(int i = 0;i < slots.length();i++ ) {
                                Log.d("Response>>>",slots.toString());

                                View tempView = li.inflate(R.layout.item_checkbox, null);

                                CheckBox cb = tempView.findViewById(R.id.listid);

                                cb.setText(t.translate(getApplicationContext(),slots.getJSONObject(i).getString("name")));



                                gridcity.addView(tempView);

                            }




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


                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
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
            VolleySingleton.getInstance(InformationActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }










    }

    private void xmlonclick() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveapi();
            }
        });



    }

    private void saveapi() {

        businessname_str=businessname.getText().toString();
        instagramname_str=instagramname.getText().toString();
        username_str=username.getText().toString();
        fullname_str = fullname.getText().toString();
        email_str=email.getText().toString();
        mobilenumber_str=mobilenumber.getText().toString();
        desciption_str=desciption.getText().toString();
        termshopname_str=termshopname.getText().toString();
        sellerdomain_str=sellerdomain.getText().toString();
        selleraddress_str=selleraddress.getText().toString();
    //    sellerlocation_str=sellerlocation.getText().toString();

        if (businessname_str.equals("")) {
            showSnackBar("Please Enter Business Name");

        } else if (instagramname_str.equals("")) {
            showSnackBar("Please Enter Instagrm Name");
        }else if (fullname_str.equals("")) {
            showSnackBar("Please Enter Full Name");
        }else if (email_str.equals("")) {
            showSnackBar("Please Enter Email Address");
        }else if (mobilenumber_str.equals("")) {
            showSnackBar("Please Enter Mobile Number");
        }else if (desciption_str.equals("")) {
            showSnackBar("Please Enter Description");
        }else if (termshopname_str.equals("")) {
            showSnackBar("Please Enter TermShopName");
        }else if (sellerdomain_str.equals("")) {
            showSnackBar("Please Enter Seller Domain");
        }else if (selleraddress_str.equals("")) {
            showSnackBar("Please Enter Seller Address");
        }

//        else if (sellerlocation_str.equals("")) {
//            showSnackBar("Please Enter Seller Location");
//        }

        else {
            informationapi();


        }






    }

    private void informationapi() {

        if (picturepath != null) {
       profile_upload_file = new File(getRealPathFromURI_API19(this, picturepath));
        }



        try {
            reqEntity = null;
            reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            reqEntity.addPart("seller_id", new StringBody(psh.getSellerid()));
            reqEntity.addPart("seller_name", new StringBody(username_str));
            reqEntity.addPart("seller_temp_store_name", new StringBody(termshopname_str));
            reqEntity.addPart("seller_email", new StringBody(email_str));
            reqEntity.addPart("seller_mobile", new StringBody(mobilenumber_str));
            reqEntity.addPart("seller_domain", new StringBody(sellerdomain_str));
            reqEntity.addPart("seller_address", new StringBody(selleraddress_str));
//            reqEntity.addPart("location", new StringBody(sellerlocation_str));

            if (picturepath != null) {
                reqEntity.addPart("seller_logo", new FileBody(profile_upload_file));
            } else {
                reqEntity.addPart("seller_logo", new FileBody(null));
            }




            /*if (uri != null) {
            reqEntity.addPart("auth_person_id", new FileBody(profile_upload_file));

           } else if(uri!=null) {
           reqEntity.addPart("org_licence_copy", new FileBody(profile_upload_file));
           }else if(uri!=null){
            reqEntity.addPart("auth_sign_copy", new FileBody(profile_upload_file));

         }
*/


        } catch (Exception e) {
            e.printStackTrace();
        }


        Boolean isInternetPresent = connection.isConnectingToInternet();

        if (isInternetPresent) {
            progressDialog = ProgressDialog.show(this,
                    "",
                    getString(R.string.progress_bar_loading_message),
                    false);

            UploadFileToServer editProfileAsyncTask = new UploadFileToServer();
            editProfileAsyncTask.execute((Void) null);
        } else {
            showToastLong(getString(R.string.no_internet_message));
        }



    }

    private void showToastLong(String string) {
    Toast.makeText(InformationActivity.this,string,Toast.LENGTH_LONG);

    }

    public static String getRealPathFromURI_API19(Context context, Uri uri) {
        String filePath = "";
        try { // FIXME NPE error when select image from QuickPic, Dropbox etc
            @SuppressLint({"NewApi", "LocalSuppress"})
            String wholeID = DocumentsContract.getDocumentId(uri);
            // Split at colon, use second item in the array
            String id = wholeID.split(":")[1];
            String[] column = {MediaStore.Images.Media.DATA};
            // where id is equal to
            String sel = MediaStore.Images.Media._ID + "=?";
            Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    column, sel, new String[]{id}, null);
            int columnIndex = cursor.getColumnIndex(column[0]);
            if (cursor.moveToFirst()) {
                filePath = cursor.getString(columnIndex);
            }

            cursor.close();

            return filePath;
        } catch (Exception e) { // this is the fix lol
            String result;
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = uri.getPath();
            } else {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
                cursor.close();
            }
            return result;
        }
    }

    private void showSnackBar(String s) {
    Toast.makeText(InformationActivity.this,s,Toast.LENGTH_LONG).show();

    }

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(InformationActivity.this);
        builder.setTitle(R.string.addphoto);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(InformationActivity.this);

                if (items[item].equals("Take Photo")) {
                    // userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();


    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

    private void xmlinit() {

        save=(Button)findViewById(R.id.save);
        imageview = (CircleImageView) findViewById(R.id.imageview);
        fullname = (EditText) findViewById(R.id.fullname);
        email = (EditText) findViewById(R.id.emailid);
        mobilenumber = (EditText) findViewById(R.id.mobilenumber);
        fullname.setText(psh.getSellerName());
        email.setText(psh.getSellerEmail());
        mobilenumber.setText(psh.getSellermobile());
        businessname = (EditText) findViewById(R.id.businessname);
        businessname.setText(psh.getSellerbusinessname());
        instagramname = (EditText) findViewById(R.id.instagramname);
        instagramname.setText(psh.getSellerinstagramname());
        edit = (ImageView) findViewById(R.id.edit);
        description=(EditText)findViewById(R.id.description);
        //desciption.setText(psh.get);
        termshopname=(EditText)findViewById(R.id.termshopname);
//        termshopname.setText(psh.get);
        sellerdomain=(EditText)findViewById(R.id.sellerdomain);
        selleraddress=(EditText)findViewById(R.id.selleraddress);
       // sellerlocation=(EditText)findViewById(R.id.sellerlocation);

        gridcity = findViewById(R.id.gridcity);

        gridsellerlocation = findViewById(R.id.gridsellerlocation);
        gridcountry= findViewById(R.id.gridcountry);
        gridstate = findViewById(R.id.gridstate);

        desciption=(EditText)findViewById(R.id.description);
        username = (EditText) findViewById(R.id.username);
        username.setText(psh.getSellerName());
        String img = psh.getSellerlogo();
     //  Toast.makeText(getApplicationContext(),img,Toast.LENGTH_LONG).show();

        if (!img.equals("")) {
            try {
                //String apiLink = context.getResources().getString(R.string.banner)+IMg;
                String apiLink = img;
                //Log.d("User Profile Image Parsing", "apiLink:"+apiLink);
                String encodedurl = "";
                encodedurl = apiLink.substring(0, apiLink.lastIndexOf('/')) + "/" + Uri.encode(apiLink.substring(
                        apiLink.lastIndexOf('/') + 1));
                Log.d("User", "encodedurl:" + encodedurl);
                if (!apiLink.equals("") && apiLink != null) {
                    Picasso.with(this)
                            .load(encodedurl) // load: This path may be a remote URL,
                            //.resize(130, 130)
                            .error(R.drawable.defaultpic)
                            .into(imageview); // Into: ImageView into which the final image has to be passed
                    //  .resize(130, 130)
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_FILE || requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                picturepath = data.getData();
                String path = picturepath.getPath();
                //picture_path.setText(path);
            } else {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //profile_iv.setImageBitmap(photo);

                // picturepath = getImageUri(ProfileScreen.this, photo);
            }

            if (requestCode == SELECT_FILE) {

                onSelectFromGalleryResult(data);
            }
            else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }

        } else if (resultCode == RESULT_CANCELED) {

            // user cancelled Image capture
            Toast.makeText(this,
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {

            // failed to capture image
            Toast.makeText(this,
                    "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                    .show();

        }


    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //profile_iv.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());


            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        imageview.setImageBitmap(bm);
    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            /*progressDialog = ProgressDialog.show(PersonalRegistrationActivity.this,
                    "",
					getString(R.string.progress_bar_loading_message),
					false);*/
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.Sellerbankdetailapi);

            try {

                /*to print in log*/

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                reqEntity.writeTo(bytes);

                String content = bytes.toString();

                Log.e("MultiPartEntityRequest:", content);

                /*to print in log*/
                // httppost.setHeader("X-Auth-Token", "4532794240e799529b08f39bc54a354f");
                //httppost.addHeader("Cache-Control", "no-cache");
                httppost.setEntity(reqEntity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();

                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    // Server response
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: "
                            + statusCode;
                }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;


        }


        @Override
        protected void onPostExecute(String result) {
            Log.e(TAG, "Response from server: " + result);

            try {
                if (!responseString.equals("")) {

                    JSONObject jsonObject = new JSONObject(responseString);
                    String Ack = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (Ack.equals("success")) {
                        progressDialog.dismiss();
                        progressDialog.cancel();
                        showToastLong(msg);
                        Intent i=new Intent(InformationActivity.this,SettingActivity.class);
                        startActivity(i);

                    } else {
                        progressDialog.dismiss();
                        progressDialog.cancel();
                        showToastLong(msg);
                    }
                } else {
                    progressDialog.dismiss();
                    progressDialog.cancel();
                    showToastLong("Sorry! Problem cannot be recognized.");
                }
            } catch (Exception e) {
                progressDialog.dismiss();
                progressDialog.cancel();
                e.printStackTrace();
            }
        }

    }



}