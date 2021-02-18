package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.preferences.SharedPrefrence;
import com.keo.onsite.linkalinpay.utils.DialogCallLang;
import com.keo.onsite.linkalinpay.utils.LanguageChange;

public class MainActivity extends AppCompatActivity {

    LinearLayout l_shopper,l_business;
    SharedPrefrence prefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new LanguageChange().LoadLocal(getBaseContext());
        setContentView(R.layout.activity_main);
        l_business=findViewById(R.id.l_business);
        l_shopper=findViewById(R.id.l_shopper);
        prefrence = SharedPrefrence.getInstance(MainActivity.this);
        ImageView langimg = findViewById(R.id.langimg);
        TextView lang = findViewById(R.id.lang);

        l_business.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                intent.putExtra("selectedType","seller");
                startActivity(intent);
            }
        });

        l_shopper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent intent=new Intent(MainActivity.this,LoginActivity.class);

                Intent intent=new Intent(MainActivity.this,MainDashboardActivity.class);

                intent.putExtra("selectedType","customer");
                startActivity(intent);
                //startActivity(intent);

            }
        });


        if(prefrence.getValue("lang") == null)
        {

            prefrence.setValue("lang","us");
        }

        else {
            if (prefrence.getValue("lang").equals("us")) {

                langimg.setImageResource(R.drawable.usa);
                lang.setText("English");

            } else {
                langimg.setImageResource(R.drawable.kuwait);

                lang.setText("عربى");

            }
        }

    }

    public void langchange(View view) {


        DialogCallLang dl = new DialogCallLang();
        dl.dialogchangelang(MainActivity.this);



    }
}