package com.keo.onsite.linkalinpay.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.keo.onsite.linkalinpay.R;

public class MyBusinessDashboardActivity extends AppCompatActivity {
      LinearLayout mybusiness,category,productmanagement,order_list,coupon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_business_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.businessdash);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setBackgroundColor(Color.parseColor("#72C5C9"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        // setSupportActionBar(toolbar);
        xmlinit();
        xmlonclik();




    }

    private void xmlonclik() {
    mybusiness.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Intent i=new Intent(MyBusinessDashboardActivity.this,BannerlistActivity.class);
        startActivity(i);
        }
    });
    category.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent(MyBusinessDashboardActivity.this,Productcategory.class);
            startActivity(i);
        }
    });

     productmanagement.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View view) {
             Intent i=new Intent(MyBusinessDashboardActivity.this,ProductManagementActivity.class);
             startActivity(i);

         }
     });
        order_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyBusinessDashboardActivity.this,OrderlistActivity.class);
                startActivity(i);

            }
        });

        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MyBusinessDashboardActivity.this,CouponActivity.class);
                startActivity(i);
            }
        });


    }

    private void xmlinit() {
    mybusiness=(LinearLayout)findViewById(R.id.mybusiness);
    category=(LinearLayout)findViewById(R.id.category);
    productmanagement=(LinearLayout)findViewById(R.id.productmanagement);
    order_list=(LinearLayout)findViewById(R.id.order_list);
    coupon= (LinearLayout) findViewById(R.id.coupon);

    }
}