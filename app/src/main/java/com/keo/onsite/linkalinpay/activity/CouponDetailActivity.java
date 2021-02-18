package com.keo.onsite.linkalinpay.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.keo.onsite.linkalinpay.R;

public class CouponDetailActivity extends AppCompatActivity {
       TextView couponname,couponcode,amount,limit,expirydate,status;
        Intent mIntent;
        Button addcoupon,assigncoupon;
       @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_detail);
        xmlinit();
        xmlonclik();


    }

    private void xmlonclik() {
    addcoupon.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View view) {
          Intent i=new Intent(CouponDetailActivity.this,AddcouponActivity.class);
          startActivity(i);


       }
   });

        assigncoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(CouponDetailActivity.this,AssignCouponActivity.class);
                startActivity(i);
            }
        });



 }

         private void xmlinit() {
            mIntent=getIntent();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.coupondetails);
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


        couponname=(TextView)findViewById(R.id.couponname);
         couponname.setText(mIntent.getStringExtra("couponname"));
         couponcode=(TextView)findViewById(R.id.couponcode);
         couponcode.setText(mIntent.getStringExtra("couponcode"));
         amount=(TextView)findViewById(R.id.amount);
         amount.setText(mIntent.getStringExtra("amount"));
         limit=(TextView)findViewById(R.id.limit);
         limit.setText(mIntent.getStringExtra("limit"));
         expirydate=(TextView)findViewById(R.id.expirydate);
         expirydate.setText(mIntent.getStringExtra("expirydate"));
         status=(TextView)findViewById(R.id.status);
         status.setText(mIntent.getStringExtra("status"));
         addcoupon=(Button)findViewById(R.id.addcoupon);
        assigncoupon=(Button)findViewById(R.id.assigncoupon);


    }
}