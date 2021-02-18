package com.keo.onsite.linkalinpay.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaScannerConnection;
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
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.model.Influencerlistmodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.Utility;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity {
    ImageView thumbnail, image1, image2, image3, remove_img1, remove_img2, remove_img3;
    EditText name_edt, productname_edt,
            price_edt, offerprice, category, productQuantity, productdes;
    Button save, button1, uploadprimaryimage, button3;
    Intent mIntent;
    String name_str, productname_str, price_str, offerprice_str, category_str, productquantity_str, description_str;
    private File profile_upload_file, profile_upload_file_,
            profile_upload_file1, profile_upload_filegallery1, profile_upload_file2, profile_upload_filegallery2, profile_upload_file3, profile_upload_filegallery3;
    private static int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 0;

    private String userChoosenTask, productimage1, productimage2, productimage3, productimage;
    private Uri picturepath, picturepath1, picturepath2, picturepath3, picturepath_, picturepathgaller1, picturepathgaller2, picturepathgaller3;
    TextView imagepath;
    private MultipartEntity reqEntity;
    ConnectionDetector connection;
    private ProgressDialog progressDialog;
    String responseString = null;
    private static String TAG;
    Spinner Spinner;
    TextView currencysymbol, currencyofferprice;
    UserShared psh;
    LinearLayout linearlayout1;
    private static final int PICK_IMAGE_2 = 2;
    private static final int PICK_IMAGE__3 = 3;
    private static final int PICK_IMAGE_4 = 4;
    Bitmap bitmap;
    String str, namestr;
    private static final String IMAGE_DIRECTORY = "/demonuts";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.editproduct);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //setSupportActionBar(toolbar);
        connection = new ConnectionDetector(this);
        xmlinit();
        xmlonclik();
    }

    private void xmlonclik() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // profile_upload_file=new File(picturepath.getPath());


                calleditproductapi();
                //Toast.makeText(EditProductActivity.this, "changed sucessfully ", Toast.LENGTH_LONG).show();
                //Intent i=new Intent(EditProductActivity.this,ProductDetailsActivity.class);
            }
        });
        uploadprimaryimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = Utility.checkPermission(EditProductActivity.this);
                if (result) {
                    // galleryIntent();
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, SELECT_FILE);

                }
            }
        });

        name_edt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(EditProductActivity.this, name_edt);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        String name_str = item.getTitle().toString();
                        name_edt.setText(name_str);
                        // Toast.makeText(EditProductActivity.this, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }


        });

        remove_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.remove_img1) {

                    str = productimage1;

                }
                callremoveimageapi(str);

            }
        });

        remove_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.remove_img2) {
                    str = productimage2;

                }
                callremoveimageapi(str);
            }


        });
        remove_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.remove_img3) {
                    str = productimage3;

                }
                callremoveimageapi(str);
            }
        });

        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = Utility.checkPermission(EditProductActivity.this);
                if (result)
                    galleryIntent();

            }

        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = Utility.checkPermission(EditProductActivity.this);
                if (result) {

                    Intent intent2 = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent2, PICK_IMAGE__3);

                }
            }
        });
        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = Utility.checkPermission(EditProductActivity.this);
                if (result) {
                    Intent intent3 = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(intent3, PICK_IMAGE_4);
                }
            }
        });


    }

    private void callremoveimageapi(final String str1) {
        if (VolleySingleton.getInstance(this).isConnected()) {
            showProgress();
            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    Constants.Removeimageapi, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jobjMain = new JSONObject(response);
                        String status = jobjMain.getString("status");
                        String msg = jobjMain.getString("msg");
                        if (status.equals("success")) {
                            Toast.makeText(EditProductActivity.this, msg, Toast.LENGTH_LONG).show();
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
                    params.put("product_id", mIntent.getStringExtra("productid"));
                    params.put("image", str1);


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
            VolleySingleton.getInstance(EditProductActivity.this).addToRequestQueue(stringRequest);
        } else {
            //Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void parseVolleyError(VolleyError error) {
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
        Toast.makeText(EditProductActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

    }

    private void showProgress() {
        if (progressDialog != null)
            progressDialog.dismiss();
        progressDialog = new ProgressDialog(EditProductActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setMessage(String.valueOf(getResources().getString(R.string.loadingplzwait)));
        progressDialog.setCancelable(false);
        progressDialog.show();


    }

    /*private void selectImage_edit(int req_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), req_code);


    }
*/


    private void galleryIntent() {
        Intent intent1 = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent1, PICK_IMAGE_2);
    }


        private void calleditproductapi() {
        name_str = name_edt.getText().toString();
        productname_str = productname_edt.getText().toString();
        price_str = price_edt.getText().toString();
        offerprice_str = offerprice.getText().toString();
        //category_str = category.getText().toString();
        description_str = productdes.getText().toString();
        productquantity_str = productQuantity.getText().toString();
        boolean cancel = false;
        String message = "";
        View focusView = null;
        boolean tempCond = false;
       /* if (picturepath_ != null) {
            profile_upload_file = new File(ImageFilePath.getPath(this, picturepath_));
        } else {
            picturepath = Uri.parse(productimage);

            profile_upload_file_ = new File(picturepath.toString());

        }
        if (picturepath1 != null) {
            profile_upload_file1 = new File(ImageFilePath.getPath(this, picturepath1));


        } else {
            picturepathgaller1 = Uri.parse(productimage1);
            profile_upload_filegallery1 = new File(picturepathgaller1.toString());

        }

        if (picturepath2 != null) {
            profile_upload_file2 = new File(ImageFilePath.getPath(this, picturepath2));
        } else {
            picturepathgaller2 = Uri.parse(productimage2);
            profile_upload_filegallery2 = new File(picturepathgaller2.toString());

        }

        if (picturepath3 != null) {
            profile_upload_file3 = new File(ImageFilePath.getPath(this, picturepath3));
        } else {
            picturepathgaller3 = Uri.parse(productimage3);
            profile_upload_filegallery3 = new File(picturepathgaller3.toString());

        }
*/
        if (cancel) {
            // focusView.requestFocus();
            if (!tempCond) {
                focusView.requestFocus();
            }
            showToastLong(message);
        } else {
            InputMethodManager imm = (InputMethodManager) this
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(productname_edt.getWindowToken(), 0);


            try {

                reqEntity = null;
                reqEntity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart("product_id", new StringBody(mIntent.getStringExtra("productid")));
                reqEntity.addPart("product_category", new StringBody(String.valueOf(name_str)));
                reqEntity.addPart("product_name", new StringBody(productname_str));
                reqEntity.addPart("product_qty", new StringBody(String.valueOf(productquantity_str)));
                reqEntity.addPart("product_price", new StringBody(price_str));
                reqEntity.addPart("product_offerprice", new StringBody(offerprice_str));
                reqEntity.addPart("product_desc", new StringBody(description_str));

                if(!productimage.equals("")){
                 // File file = new File(picturepath_.toString());
                    File  file = new File(productimage);
                     reqEntity.addPart("product_image", new FileBody(file));
                   }else if (productimage.equals("") ) {
                    File file = new File(getRealPathFromURI(picturepath_));
                    reqEntity.addPart("product_image", new FileBody(file));
                }
                if(!productimage1.equals("")){
                    File file1 = new File(productimage1);
                    reqEntity.addPart("product_image1", new FileBody(file1));

                }else if (productimage1.equals("")) {
                    File file1 = new File(getRealPathFromURI(picturepath1));
                    reqEntity.addPart("product_image1", new FileBody(file1));
                }
                if(!productimage2.equals("")){
                    File file2 = new File(productimage2);
                    reqEntity.addPart("product_image2", new FileBody(file2));

                }else if (productimage2 .equals("") ) {
                    File file2 = new File(getRealPathFromURI(picturepath2));
                    reqEntity.addPart("product_image2", new FileBody(file2));
                }
                if(!productimage3.equals("")){
                    File file3 = new File(productimage3);
                    reqEntity.addPart("product_image3", new FileBody(file3));

                }else if (productimage3.equals("")) {
                    File file3 = new File(getRealPathFromURI(picturepath3));
                    reqEntity.addPart("product_image3", new FileBody(file3));
                }

                /*if (picturepath_ != null) {
                    reqEntity.addPart("product_image", new FileBody(profile_upload_file));
                } else if (picturepath != null) {
                    reqEntity.addPart("product_image", new FileBody(profile_upload_file_));
                }

                if (picturepath1 != null) {
                    reqEntity.addPart("product_image1", new FileBody(profile_upload_file1));
                } else if (picturepathgaller1 != null) {
                    reqEntity.addPart("product_image", new FileBody(profile_upload_filegallery1));
                }


                if (picturepath2 != null) {
                    reqEntity.addPart("product_image2", new FileBody(profile_upload_file2));
                } else if (picturepathgaller2 != null) {
                    reqEntity.addPart("product_image2", new FileBody(profile_upload_filegallery2));
                }
                if (picturepath3 != null) {
                    reqEntity.addPart("product_image3", new FileBody(profile_upload_file3));
                } else if (picturepathgaller3 != null) {
                    //reqEntity.addPart("product_image3", new FileBody(null));
                    reqEntity.addPart("product_image3", new FileBody(profile_upload_filegallery3));
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


    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void showToastLong(String message) {
        Toast.makeText(EditProductActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SELECT_FILE:
                if (resultCode == Activity.RESULT_OK) {

                    if (data != null) {
                        picturepath_ = data.getData();
                        String realPath = ImageFilePath.getPath(this, picturepath_);
                        //profile_upload_file=new File(ImageFilePath.getPath(this, picturepath));


                        Log.i(TAG, "onActivityResult: file path : " + realPath);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picturepath_);
                            // Log.d(TAG, String.valueOf(bitmap));
                            thumbnail.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;

            case PICK_IMAGE_2:
                if (resultCode == Activity.RESULT_OK) {
                    boolean result = Utility.checkPermission(EditProductActivity.this);
                    if (result)

                        if (data != null) {
                            picturepath1 = data.getData();
                            String realPath = ImageFilePath.getPath(this, picturepath1);
                            Log.i(TAG, "onActivityResult: file path : " + realPath);
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picturepath1);
                                // Log.d(TAG, String.valueOf(bitmap));
                                image1.setImageBitmap(bitmap);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }


                }
                break;


            case PICK_IMAGE__3:
                if (resultCode == Activity.RESULT_OK) {

                    if (data != null) {
                        picturepath2 = data.getData();
                        String realPath = ImageFilePath.getPath(this, picturepath2);
                        Log.i(TAG, "onActivityResult: file path : " + realPath);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picturepath2);
                            // Log.d(TAG, String.valueOf(bitmap));
                            image2.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;
            case PICK_IMAGE_4:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        picturepath3 = data.getData();
                        String realPath = ImageFilePath.getPath(this, picturepath3);
                        Log.i(TAG, "onActivityResult: file path : " + realPath);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picturepath3);
                            // Log.d(TAG, String.valueOf(bitmap));
                            image3.setImageBitmap(bitmap);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                break;


            default:
        }


    }

    /*public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }*/


       private void xmlinit() {
        mIntent = getIntent();
        psh = new UserShared(this);
        productimage1 = mIntent.getStringExtra("produimg1");
        productimage2 = mIntent.getStringExtra("productimg2");
        productimage3 = mIntent.getStringExtra("productimg3");
        productimage = mIntent.getStringExtra("productimage");


        name_edt = (EditText) findViewById(R.id.productcatname);
        name_edt.setText(mIntent.getStringExtra("categoryname"));
        namestr = mIntent.getStringExtra("categoryname");

        productname_edt = (EditText) findViewById(R.id.productname);
        productname_edt.setText(mIntent.getStringExtra("productname"));
        price_edt = (EditText) findViewById(R.id.price);
        price_edt.setText(mIntent.getStringExtra("productprice"));
        offerprice = (EditText) findViewById(R.id.offerprice);
        offerprice.setText(mIntent.getStringExtra("offerprice"));
        thumbnail = (ImageView) findViewById(R.id.coverImage);
        productQuantity = (EditText) findViewById(R.id.productQuantity);
        productQuantity.setText(mIntent.getStringExtra("productqty"));
        productdes = (EditText) findViewById(R.id.productDesc);
        currencysymbol = (TextView) findViewById(R.id.currencysymbol);
        currencyofferprice = (TextView) findViewById(R.id.currencyofferprice);
        currencysymbol.setText("(" + psh.getCurrencysymbol() + ")");
        currencyofferprice.setText("(" + psh.getCurrencysymbol() + ")");
        //productdes.setText(mIntent.getStringExtra("productdesc"));
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);
        button1 = (Button) findViewById(R.id.choosefile_multiple);
        //button2=(Button)findViewById(R.id.button2);
        uploadprimaryimage = (Button) findViewById(R.id.uploadprimaryimage);
        linearlayout1 = (LinearLayout) findViewById(R.id.linearlayout1);
        remove_img1 = (ImageView) findViewById(R.id.remove_img1);
        remove_img2 = (ImageView) findViewById(R.id.remove_img2);
        remove_img3 = (ImageView) findViewById(R.id.remove_img3);


        if (mIntent.getStringExtra("produimg1").equals("")) {
            image1.setImageBitmap(null);

        } else if (!mIntent.getStringExtra("produimg1").equals("")) {

            Glide.with(this)
                    .load(mIntent.getStringExtra("produimg1"))
                    .into(image1);

        }




/*else{
            Glide.with(this)
                    .load(mIntent.getStringExtra("produimg1"))
                    .into(image1);

        }
*/
        /*Glide.with(this)
                .load(mIntent.getStringExtra("productimg2"))
                .into(image2);
*/
        if (mIntent.getStringExtra("productimg2").equals("")) {
            image2.setImageBitmap(null);

        } else if (!mIntent.getStringExtra("productimg2").equals("")) {

            Glide.with(this)
                    .load(mIntent.getStringExtra("productimg2"))
                    .into(image2);

        }

        if (mIntent.getStringExtra("productimg3").equals("")) {
            image3.setImageBitmap(null);

        } else if (!mIntent.getStringExtra("productimg3").equals("")) {

            Glide.with(this)
                    .load(mIntent.getStringExtra("productimg3"))
                    .into(image3);

        }



        /*Glide.with(this)
                .load(mIntent.getStringExtra("productimg3"))
                .into(image3);
*/
        Glide.with(this)
                .load(mIntent.getStringExtra("productimage"))
                .into(thumbnail);


        save = (Button) findViewById(R.id.btn_submit);

    }

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            progressDialog = ProgressDialog.show(EditProductActivity.this,
                    "",
                    getString(R.string.progress_bar_loading_message),
                    false);
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            HttpParams params = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(params, 10000);
            HttpConnectionParams.setSoTimeout(params, 10000);
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpProtocolParams.setUseExpectContinue(params, true);

            HttpClient httpclient = new DefaultHttpClient(params);
            HttpPost httppost = new HttpPost(Constants.editproduct);

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
                        Intent i = new Intent(EditProductActivity.this, ProductManagementActivity.class);
                        startActivity(i);
                        finish();
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