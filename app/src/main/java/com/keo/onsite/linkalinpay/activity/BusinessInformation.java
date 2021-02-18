package com.keo.onsite.linkalinpay.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.connection.ConnectionDetector;
import com.keo.onsite.linkalinpay.activity.other.Constants;
import com.keo.onsite.linkalinpay.activity.other.Utility;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class BusinessInformation extends AppCompatActivity {
    EditText organisationname, accountnumber, beneficiaryname;
    Button personal_id, licence, signatorycopy, save;
    String organisationname_str, accountnumber_str, beneficiaryname_str;
   TextView personalid,licencecopyid,signatoryid;

    private static final int FILE_SELECT_CODE = 0;
    private Uri uri;
    String path = "";
    private static final int SELECT_FILE1 = 1;
    private static final int SELECT_FILE2 = 2;
    private static final int SELECT_FILE3 = 3;

    String selectedPath1 = "NONE";
    String selectedPath2 = "NONE";
    String selectedPath3 = "NONE";



    private static final String TAG = "";
    private File profile_upload_file,profile_upload_file1,profile_upload_file2;
    private MultipartEntity reqEntity;
    ConnectionDetector connection;
     UserShared psh;
    private ProgressDialog progressDialog;
    String responseString = null;
    TextView filepath,filepath_licence,signtory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_information);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.bankinfo);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        psh=new UserShared(this);
        connection=new ConnectionDetector(this);
        xmlinit();
        xmlonclik();


    }

    private void xmlonclik() {
        personal_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser(SELECT_FILE1);
            }
        });

        licence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser(SELECT_FILE2);
            }
        });

        signatorycopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileChooser(SELECT_FILE3);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                organisationname_str = organisationname.getText().toString();
                accountnumber_str = accountnumber.getText().toString();
                beneficiaryname_str = beneficiaryname.getText().toString();

                if (organisationname_str.equals("")) {
                    showSnackBar("Please Enter Organisation Name");

                } else if (accountnumber_str.equals("")) {
                    showSnackBar("Please Enter Account Number");
                } else if (beneficiaryname_str.equals("")) {
                    showSnackBar("Enter Beneficiary Name");
                } else {
                    businessinfoapi();


                }

            }
        });


    }

    private void FileChooser(int req_code) {
     Intent intent = new Intent();
     intent.setType("image/*");
     intent.setAction(Intent.ACTION_GET_CONTENT);
     startActivityForResult(Intent.createChooser(intent,"Select file to upload "), req_code);
    }




    /*private void uploaddocument(int req_code) {
        boolean result = Utility.checkPermission(BusinessInformation.this);
        if (result)
            showFileChooser( req_code);


    }
*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            if (requestCode == SELECT_FILE1)
            {
                selectedPath1 = getPath(this,selectedImageUri);
                System.out.println("selectedPath1 : " + selectedPath1);
            }
            if (requestCode == SELECT_FILE2)
            {
                selectedPath2 = getPath(this,selectedImageUri);
                System.out.println("selectedPath2 : " + selectedPath2);
            }

            if (requestCode == SELECT_FILE3)
            {
                selectedPath3 = getPath(this,selectedImageUri);
                System.out.println("selectedPath3 : " + selectedPath3);
            }


            personalid.setText("Selected File paths : " + selectedPath1  );
            licencecopyid.setText("Selected File paths : " + selectedPath2  );
            filepath_licence.setText("Selected File paths : " + selectedPath3  );


        }

    }

         public String getPath(Context context,Uri uri) {
             String result = null;
             String[] proj = { MediaStore.Images.Media.DATA };
             Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
             if(cursor != null){
                 if ( cursor.moveToFirst( ) ) {
                     int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                     result = cursor.getString( column_index );
                 }
                 cursor.close( );
             }
             if(result == null) {
                 result = "Not found";
             }
             return result;

         }


    private void businessinfoapi() {
        /*if (uri != null) {
            profile_upload_file = new File(getFilePathFromURI(this, uri));
        }
*/
        try {
            reqEntity = null;
            reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);

            reqEntity.addPart("seller_id", new StringBody(psh.getSellerid()));
            reqEntity.addPart("organisation_name", new StringBody(organisationname_str));
            reqEntity.addPart("bank_account_number", new StringBody(accountnumber_str));
            reqEntity.addPart("beneficiary_name", new StringBody(beneficiaryname_str));
            File file1 = new File(selectedPath1);
            File file2 = new File(selectedPath2);
            File file3 = new File(selectedPath3);
            reqEntity.addPart("auth_person_id", new FileBody(file1));
            reqEntity.addPart("org_licence_copy", new FileBody(file2));
            reqEntity.addPart("auth_sign_copy", new FileBody(file3));


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

    private void showToastLong(String msg) {
    Toast.makeText(BusinessInformation.this,msg,Toast.LENGTH_LONG).show();
    }

    /*public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(context.getFilesDir() + File.separator + fileName);
            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }*/

    /*public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copyFile(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    /*private static void copyFile(InputStream inputStream, OutputStream outputStream) throws IOException {
        int BUFFER_SIZE = 1024 * 2;
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(inputStream, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(outputStream, BUFFER_SIZE);
        int n;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                //JxdUtils.E("" + e);
            }
            try {
                in.close();
            } catch (IOException e) {
                //JxdUtils.E("" + e);
            }
        }
    }*/
    /*public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String fileName = null;
        String path = uri.getPath();
        if (path == null) return null;
        int cut = path.lastIndexOf('/');
        if (cut != -1) {
            fileName = path.substring(cut + 1);
        }
        return fileName;
    }
*/

    private void showSnackBar(String msg) {
        Toast.makeText(BusinessInformation.this, msg, Toast.LENGTH_LONG).show();

    }

    private void xmlinit() {
        personal_id = (Button) findViewById(R.id.personid);
        licence = (Button) findViewById(R.id.licence);
        signatorycopy = (Button) findViewById(R.id.signatorycopy);
        organisationname = (EditText) findViewById(R.id.organisationname);
        accountnumber = (EditText) findViewById(R.id.accountnumber);
        beneficiaryname = (EditText) findViewById(R.id.beneficiaryname);
        save = (Button) findViewById(R.id.save);
        personalid=(TextView)findViewById(R.id.personalid);
        licencecopyid=(TextView)findViewById(R.id.licencecopyid);
        filepath_licence=(TextView)findViewById(R.id.signatoryid);


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
                        Intent i=new Intent(BusinessInformation.this,SettingActivity.class);
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