package com.keo.onsite.linkalinpay.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;
import com.keo.onsite.linkalinpay.preferences.SharedPrefrence;
import com.keo.onsite.linkalinpay.utils.DialogCallLang;
import com.keo.onsite.linkalinpay.utils.LanguageChange;

public class SettingActivity extends AppCompatActivity {
    LinearLayout sign_out,pricing,currency_setting , hepl_rl;
          LinearLayout accountinfo,businessinfo;
          UserShared psh;
          TextView lang;
    SharedPrefrence prefrence;

    ImageView sellerlogo;

    @Override
      protected void onCreate(Bundle savedInstanceState) {
              new LanguageChange().LoadLocal(getBaseContext());
              super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.settings);
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
        prefrence = SharedPrefrence.getInstance(SettingActivity.this);


        xmlinit();
         xmlonclik();


         Intent getimg = getIntent();
        Glide.with(this)
                .load(getimg.getStringExtra("logo"))
                .fitCenter()
                .into(sellerlogo);


        if( prefrence.getValue("lang").equals("us"))
        {

            lang.setText("English");

        }
        else {
            lang.setText("Arabic");

        }

    }

    private void xmlonclik() {
    sign_out.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            psh.aleartLogOut();
        }
    });

     accountinfo.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent i=new Intent(SettingActivity.this,InformationActivity.class);
             startActivity(i);

         }
     });
        businessinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i1=new Intent(SettingActivity.this,BusinessInformation.class);
                startActivity(i1);
            }
        });

        pricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i2=new Intent(SettingActivity.this,PricingActivity.class);
                startActivity(i2);

            }
        });

       currency_setting.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent i=new Intent(SettingActivity.this,Currencyconverter.class);
               startActivity(i);

           }
       });

        hepl_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                DialogCallLang dl = new DialogCallLang();
               dl.dialogchangelang(SettingActivity.this);






            }
        });

    }

    private void xmlinit() {
     sign_out=(LinearLayout)findViewById(R.id.sign_out);
     psh=new UserShared(this);
     accountinfo=(LinearLayout)findViewById(R.id.accountinfo);
     businessinfo=(LinearLayout) findViewById(R.id.businessinfo);
     pricing=(LinearLayout)findViewById(R.id.pricing);
     currency_setting=(LinearLayout)findViewById(R.id.currency_setting);
        hepl_rl = findViewById(R.id.hepl_rl);
        lang = findViewById(R.id.lang);
        sellerlogo = findViewById(R.id.sellerlogo);
  }
}