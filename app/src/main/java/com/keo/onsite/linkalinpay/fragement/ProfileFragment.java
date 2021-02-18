package com.keo.onsite.linkalinpay.fragement;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.AddBannerActivity;
import com.keo.onsite.linkalinpay.activity.BannerlistActivity;
import com.keo.onsite.linkalinpay.activity.CustomerInfo;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.Utility;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
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
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {
  UserShared psh;
  View view;
  EditText username, email, mobilenumber;
  CircleImageView img;
  ImageView edit;
  String username_str,mobilenumber_str;
  private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
  private String userChoosenTask;
  private Uri picturepath;
  private File profile_upload_file;
  ConnectionDetector connection;
  private ProgressDialog progressDialog;
  String responseString = null;
  private static String TAG;
  MultipartEntity reqEntity;
 TextView update;
 SharedPreferences prefs;
String imgageview;
    public ProfileFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view=inflater.inflate(R.layout.fragment_profile, container, false);
         prefs=getActivity().getSharedPreferences("MY_SHARED_PREF", Context.MODE_PRIVATE);

         xmlinit();
         xmlonclik();
        return view;

    }

    private void xmlonclik() {
    edit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            selectImage();
        }
    });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username_str = username.getText().toString().trim();
                mobilenumber_str = mobilenumber.getText().toString().trim();
                boolean cancel = false;
                String message = "";
                View focusView = null;
                boolean tempCond = false;
                if (picturepath != null) {

                    profile_upload_file = new File(getRealPathFromURI_API19(getActivity(), picturepath));
                }



                if (cancel) {
                    // focusView.requestFocus();
                    if (!tempCond) {
                        focusView.requestFocus();
                    }
                    showToastLong(message);
                } else {
                    InputMethodManager imm = (InputMethodManager) getActivity()
                            .getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(username.getWindowToken(), 0);


                    try {

                        reqEntity = null;
                        reqEntity = new MultipartEntity(
                                HttpMultipartMode.BROWSER_COMPATIBLE);


                        reqEntity.addPart("cust_id", new StringBody(psh.getCustomerid()));
                        reqEntity.addPart("name", new StringBody(username_str));
                        reqEntity.addPart("email", new StringBody(psh.getUsername_mail()));
                        reqEntity.addPart("mobile", new StringBody(mobilenumber_str));

                        if (picturepath != null) {
                            reqEntity.addPart("profile", new FileBody(profile_upload_file));
                        } else {
                            reqEntity.addPart("profile", new FileBody(null));
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    Boolean isInternetPresent = connection.isConnectingToInternet();

                    if (isInternetPresent) {
                        /*progressDialog = ProgressDialog.show(getActivity(),
                                "",
                                getString(R.string.progress_bar_loading_message),
                                false);
*/
                        UploadFileToServer editProfileAsyncTask = new UploadFileToServer();
                        editProfileAsyncTask.execute((Void) null);
                    } else {
                        showToastLong(getString(R.string.no_internet_message));
                    }
                }


            }
        });

    }

    private void showToastLong(String message) {
    Toast.makeText(getActivity(),message,Toast.LENGTH_LONG).show();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.addphoto);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

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
        connection=new ConnectionDetector(getActivity());
        psh=new UserShared(getActivity());
        username=(EditText)view.findViewById(R.id.username);
       username.setText(psh.getUsername_cust());
       email=(EditText)view.findViewById(R.id.email);
       email.setText(psh.getUsername_mail());
       mobilenumber=(EditText)view.findViewById(R.id.mobilenumber);
       mobilenumber.setText(psh.getUsername_mobile());
     img=(CircleImageView)view.findViewById(R.id.img_profile);
     edit=(ImageView)view.findViewById(R.id.edit);
     update=(TextView)view.findViewById(R.id.update);
      imgageview = psh.getUsername_pic();

      Log.d("image>>", imgageview.toString());
            if (!imgageview.equals("")) {
                try {
                    //String apiLink = context.getResources().getString(R.string.banner)+IMg;
                    String apiLink = imgageview;
                    Glide.with(getActivity())
                            .load(imgageview)
                            .into(img);


                    //Log.d("User Profile Image Parsing", "apiLink:"+apiLink);
                    String encodedurl = "";
                    encodedurl = apiLink.substring(0, apiLink.lastIndexOf('/')) + "/" + Uri.encode(apiLink.substring(
                            apiLink.lastIndexOf('/') + 1));
                    Log.d(" Social List adapter", "encodedurl:" + encodedurl);
                    if (!apiLink.equals("") && apiLink != null) {
//                        Picasso.with(getActivity())
//                                .load(encodedurl) // load: This path may be a remote URL,
//                                .placeholder(R.drawable.defaultpic)
//                                //.resize(130, 130)
//                                .error(R.drawable.defaultpic)
//                                .into(img); // Into: ImageView into which the final image has to be passed
                        //  .resize(130, 130)


//                        Log.d("Imageurl>>>",encodedurl);

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
        if (requestCode == SELECT_FILE || requestCode == REQUEST_CAMERA  && resultCode == RESULT_OK) {
            if (data.getData() != null) {
                picturepath = data.getData();

              } else {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                //profile_iv.setImageBitmap(photo);

                // picturepath = getImageUri(ProfileScreen.this, photo);
            }

            if (requestCode == SELECT_FILE) {

                onSelectFromGalleryResult(data);
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
            }

        }

        else if (resultCode == RESULT_CANCELED) {

            // user cancelled Image capture
            Toast.makeText(getActivity(),
                    "User cancelled image capture", Toast.LENGTH_SHORT)
                    .show();
        } else {

            // failed to capture image
            Toast.makeText(getActivity(),
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
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        img.setImageBitmap(bm);

    }
    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            /*progressDialog = ProgressDialog.show(getActivity(),
                    "",
                    getString(R.string.progress_bar_loading_message),
                    false);
  */

            super.onPreExecute();
        }


        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.updateprofile);

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
                        //progressDialog.dismiss();
                        //progressDialog.cancel();
                        showToastLong(msg);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putString(getString(R.string.shared_customer_name),jsonObject.getString("cust_name"));
                        //editor.putString(getString(R.string.shared_customer_email), jsonObject.getString("cust_email"));
                        editor.putString(getString(R.string.shared_customer_mobilenumber), jsonObject.getString("cust_mobile"));
                        editor.putString(getString(R.string.shared_customer_pic), jsonObject.getString("cust_profile"));

                        editor.putBoolean(getString(R.string.shared_loggedin_status_customer), true);
                        editor.commit();




                        //stopprogress();
                        //Intent i=new Intent(getActivity(), BannerlistActivity.class);
                        //startActivity(i);
                    } else {
                        //progressDialog.dismiss();
                        //progressDialog.cancel();
                        showToastLong(msg);
                    }
                } else {
                    //progressDialog.dismiss();
                    //progressDialog.cancel();
                    showToastLong("Sorry! Problem cannot be recognized.");
                }
            } catch (Exception e) {
               // progressDialog.dismiss();
                //progressDialog.cancel();
                e.printStackTrace();
            }
        }

    }

    /*private void stopprogress() {
    if (progressDialog != null && progressDialog.isShowing()) {
      progressDialog.dismiss();


    }
*/


    }


