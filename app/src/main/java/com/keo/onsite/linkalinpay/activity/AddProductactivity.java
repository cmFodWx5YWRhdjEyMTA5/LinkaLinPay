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
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpicker.Config;
import com.gun0912.tedpicker.ImagePickerActivity;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.model.Currencymodelclass;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.Utility;
import com.keo.onsite.linkalinpay.activity.other.VolleySingleton;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.adapter.CurrencyAdapter;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddProductactivity extends AppCompatActivity {
     Spinner product_cat;
    int position=1;
   EditText productname,productprice,productofferprice,productqty,productdesc;
   Button choosefile_multiple,btn_submit,uploadprimaryimage;
   String productname_str,productprice_str,
   productofferprice_str,productqty_str,productdesc_str;
    private File profile_upload_file;
   private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private Uri picturepath;
      File image_pathhh;
    ImageView coverImage,image1,image2,image3;
    private MultipartEntity reqEntity;
    ConnectionDetector connection;
    private ProgressDialog progressDialog;
      String responseString = null;
      private static String TAG;
     UserShared psh;
      private static final int INTENT_REQUEST_GET_IMAGES = 13;
      ArrayList<Uri> image_uris = new ArrayList<Uri>();
      LinearLayout fl_selectedPhoto;
    TextView  currencysymbol,currencyofferprice;
      Spinner changecurrency;
    ArrayList<Currencymodelclass>currencylist;
     String selected,currency_symbol;
      @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_productactivity);
          psh=new UserShared(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.addproduct);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
          toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
          toolbar.setNavigationOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  finish();
              }
          });
          connection=new ConnectionDetector(this);
           xmlinit();
          xmlonclik();
    }

       private void xmlonclik() {
        choosefile_multiple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              image1.setVisibility(View.GONE);
              image2.setVisibility(View.GONE);
              image3.setVisibility(View.GONE);



              Config config = new Config();
                config.setCameraHeight(R.dimen.app_camera_height);
                config.setToolbarTitleRes(R.string.custom_title);
                config.setSelectionMin(1);
                config.setSelectionLimit(3);
                config.setSelectedBottomHeight(R.dimen.bottom_height);
                config.setFlashOn(true);
                getImages(config);

            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Addproductapi();
            }
        });

        uploadprimaryimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

     }

       private void getImages(Config config) {
       // ImagePickerActivity.setConfig(config);
        Intent intent = new Intent(this, ImagePickerActivity.class);
         if (image_uris != null) {
              intent.putParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS, image_uris);
          }
          startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);

      }
        private void Addproductapi() {
             productname_str = productname.getText().toString().trim();
             productprice_str = productprice.getText().toString().trim();
             productofferprice_str=productofferprice.getText().toString();
             productqty_str=productqty.getText().toString();
             productdesc_str=productdesc.getText().toString();
             boolean cancel = false;
            String message = "";
            View focusView = null;
            boolean tempCond = false;

            if (picturepath != null) {

                profile_upload_file = new File(getRealPathFromURI_API19(this, picturepath));
            }


            if (TextUtils.isEmpty(productname_str)) {
                message = "Please Enter Product Name";
                focusView = productname;
                cancel = true;
                tempCond = false;
            }

            if (TextUtils.isEmpty(productprice_str)) {
                message = "Please Enter Product Price";
                focusView = productprice;
                cancel = true;
                tempCond = false;
            }

                 if (TextUtils.isEmpty(productofferprice_str)) {
                     message = "Please Enter Offer Price";
                     focusView = productofferprice;
                     cancel = true;
                     tempCond = false;
                 }
                 if (TextUtils.isEmpty(productqty_str)) {
                     message = "Please Enter Product Quantity";
                     focusView = productqty;
                     cancel = true;
                     tempCond = false;
                 }
                 if (TextUtils.isEmpty(productdesc_str)) {
                     message = "Please Enter Product Descrption";
                     focusView = productdesc;
                     cancel = true;
                     tempCond = false;
                 }


            if (cancel) {
                // focusView.requestFocus();
                if (!tempCond) {
                    focusView.requestFocus();
                }
                showToastLong(message);
            } else {
                InputMethodManager imm = (InputMethodManager) this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                imm.hideSoftInputFromWindow(productname.getWindowToken(), 0);


                try {

                    reqEntity = null;
                    reqEntity = new MultipartEntity(
                            HttpMultipartMode.BROWSER_COMPATIBLE);

                    reqEntity.addPart("seller_id", new StringBody(psh.getSellerid()));
                    reqEntity.addPart("product_category", new StringBody(String.valueOf(position)));
                    reqEntity.addPart("product_name", new StringBody(productname_str));
                    reqEntity.addPart("product_qty", new StringBody(String.valueOf(productqty_str)));
                    reqEntity.addPart("product_desc", new StringBody(productdesc_str));
                    reqEntity.addPart("product_price", new StringBody(productprice_str));
                    reqEntity.addPart("product_offerprice", new StringBody(productofferprice_str));
                    reqEntity.addPart("currency", new StringBody(currency_symbol));


                       if (picturepath != null) {
                        reqEntity.addPart("product_image", new FileBody(profile_upload_file));
                    } else {
                        reqEntity.addPart("product_image", new FileBody(null));
                    }
                    for (int i = 0; i < image_uris.size(); i++) {
                        if (image_uris.get(i) != null) {

                            image_pathhh = new File(getRealPathFromURI_API19(this, image_uris.get(i)));
                            reqEntity.addPart("product_multiimages[]", new FileBody(image_pathhh));
                        }
                    }

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

    private void showToastLong(String message) {
    Toast.makeText(AddProductactivity.this,message,Toast.LENGTH_LONG).show();


    }

    private static String getRealPathFromURI_API19(Context context, Uri uri) {
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

    private void selectImage() {
        final CharSequence[] items = {"Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(AddProductactivity.this);
        builder.setTitle(R.string.addphoto);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(AddProductactivity.this);

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

       private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);

    }

    private void cameraIntent() {
    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    startActivityForResult(intent, REQUEST_CAMERA);

    }

       private void xmlinit() {
        fl_selectedPhoto = (LinearLayout) findViewById(R.id.fl_selectedPhoto);
        currencysymbol=(TextView) findViewById(R.id.currencysymbol);
        currencyofferprice=(TextView) findViewById(R.id.currencyofferprice);
           currency_symbol   =psh.getCurrencysymbol();
        currencyofferprice.setText("("+psh.getCurrencysymbol()+")");
        currencysymbol.setText("("+psh.getCurrencysymbol()+")");
         //currencyofferprice.setText(psh.getCurrencysymbol());
         product_cat=(Spinner)findViewById(R.id.product_category);
        // Spinner Drop down elements
       // changecurrency=(Spinner)findViewById(R.id.changecurrency);
       image1=(ImageView)findViewById(R.id.image1);
       image2=(ImageView)findViewById(R.id.image2);
       image3=(ImageView)findViewById(R.id.image3);

        List<String> categories = new ArrayList<String>();
        categories.add("name");
        categories.add("category name");
        categories.add("new category");
        categories.add("test sub category");
        categories.add("demo 123");
        categories.add("test category");
       // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        product_cat.setAdapter(dataAdapter);

          position= product_cat.getSelectedItemPosition();
         productname=(EditText)findViewById(R.id.productname);
         productprice=(EditText)findViewById(R.id.productprice);
         productofferprice=(EditText)findViewById(R.id.productofferprice);
         productqty=(EditText)findViewById(R.id.productqty);
         productdesc=(EditText)findViewById(R.id.productdesc);
        choosefile_multiple=(Button)findViewById(R.id.choosefile_multiple);
        uploadprimaryimage=(Button)findViewById(R.id.uploadprimaryimage);
        coverImage=(ImageView) findViewById(R.id.coverImage);
          btn_submit=(Button)findViewById(R.id.btn_submit);

     //  changecurrencyapi();

      }

      /*private void changecurrencyapi() {
          if (VolleySingleton.getInstance(this).isConnected()) {

              showProgress();

              StringRequest strRequest = new StringRequest(Request.Method.POST, Constants.currencylistapi ,

                      new Response.Listener<String>() {
                          @Override
                          public void onResponse(String response) {


                              try {
                                  JSONObject jsonObject = new JSONObject(response);
                                  String status = jsonObject.getString("status");
                                  currencylist = new ArrayList<Currencymodelclass>();
                                  if (status.equals("success")) {
                                      JSONArray jarray = jsonObject.getJSONArray("currency");
                                      for (int i = 0; i < jarray.length(); i++) {
                                          JSONObject jobj1 = jarray.getJSONObject(i);
                                          Currencymodelclass blmc = new Currencymodelclass(jobj1.getString("currencysymbol"),jobj1.getString("currencyname")                                              );
                                          currencylist.add(blmc);
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
                      //params.put("seller_id", psh.getSellerid());
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
              Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show();
          }




      }*/

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
    Toast.makeText(AddProductactivity.this,msg,Toast.LENGTH_LONG).show();

      }

    private void stopProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }


    }

    private void setAdapter() {
     CurrencyAdapter cadapter=new CurrencyAdapter(this,currencylist,R.layout.catgry_item);
      changecurrency.setAdapter(cadapter);
        /*changecurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {



                selected=currencylist.get(i).currencysymbol;

                currencysymbol.setText(selected);
                currencyofferprice.setText(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

*/
        // Post to avoid initial invocation
        /*changecurrency.post(new Runnable() {
            @Override public void run() {
                changecurrency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        // Only called when the user changes the selection
                        selected=currencylist.get(position).currencysymbol;
                        currencysymbol.setText("("+selected+")");
                        currencyofferprice.setText("("+selected+")");


                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }
        });
*/

    }



    private void showProgress() {


      }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == INTENT_REQUEST_GET_IMAGES) {

                image_uris = data.getParcelableArrayListExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);
                if (image_uris != null) {
                    showMedia();
                }
            }
        }

        if (requestCode == REQUEST_CAMERA || requestCode == SELECT_FILE && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                picturepath = data.getData();

            } else {

                Bitmap photo = (Bitmap) data.getExtras().get("data");
                picturepath = getImageUri(AddProductactivity.this, photo);
            }

            if (requestCode == SELECT_FILE) {

                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
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
     }

         public Uri getImageUri(Context inContext, Bitmap inImage) {
          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
          inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
          String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
          return Uri.parse(path);
      }


      private void showMedia() {
          fl_selectedPhoto.removeAllViews();
          if (image_uris.size() >= 1) {
              fl_selectedPhoto.setVisibility(View.VISIBLE);
          }

          int wdpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
          int htpx = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
           for (Uri uri : image_uris) {
              View imageHolder = LayoutInflater.from(this).inflate(R.layout.image_item, null);
              ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);

              Glide.with(this)
                      .load(uri.toString())
                      .fitCenter()
                      .into(thumbnail);

              fl_selectedPhoto.addView(imageHolder);

              thumbnail.setLayoutParams(new FrameLayout.LayoutParams(wdpx, htpx));


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

          coverImage.setImageBitmap(thumbnail);
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

        coverImage.setImageBitmap(bm);
    }

      private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
          @Override
          protected void onPreExecute() {
              // setting progress bar to zero
              progressDialog = ProgressDialog.show(AddProductactivity.this,
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

              HttpClient httpclient = new DefaultHttpClient();
              HttpPost httppost = new HttpPost(Constants.SellerAddproduct);

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
                        Intent i=new Intent(AddProductactivity.this,ProductManagementActivity.class);
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

      @Override
      public void onBackPressed() {
          //super.onBackPressed();
          finish();
      }
  }