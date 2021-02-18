package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;

public class HelpSupport extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_support);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.helpands);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // getSupportActionBar().setTitle("Donation List");
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
    }

    public void whatsapp(View view) {
        String contact = "+91 111111111"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact;
        try {
            PackageManager pm = getApplication().getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(HelpSupport.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public void submitQuery(View view) {

        Intent intent = new Intent(HelpSupport.this,SubmitQuery.class);
        startActivity(intent);


    }

    public void back(View view) {
        onBackPressed();
    }
}