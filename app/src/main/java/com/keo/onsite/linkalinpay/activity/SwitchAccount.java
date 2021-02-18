package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.model.Customermodel;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

import java.util.ArrayList;

public class SwitchAccount extends AppCompatActivity {

    RecyclerView recyclerview;
    ArrayList<Customermodel> Customerlist;
    private ProgressDialog progressDialog;
    UserShared psh;
    ImageView b;
    String totrecord;
    ImageView userimg;
    TextView name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_account);
        psh=new UserShared(this);
        userimg = findViewById(R.id.userimg);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);

        Intent i = getIntent();


        Glide.with(getApplicationContext())
                .load(i.getStringExtra("logo"))
                .into(userimg)
                ;


        name.setText(i.getStringExtra("username"));
        email.setText(i.getStringExtra("email"));


    }

    public void back(View view) {
        onBackPressed();

    }

    public void logout(View view) {

        psh.aleartLogOut();

    }
}