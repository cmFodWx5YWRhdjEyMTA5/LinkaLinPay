package com.keo.onsite.linkalinpay.utils;//


// Created by Rajat Saha on 14-12-2020.
// Copyright (c) 2020 HTSM. All rights reserved.
//

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.preferences.SharedPrefrence;

import java.util.Locale;

public class DialogCallLang {

    SharedPrefrence prefrence;
    TextView tv;

    String language;

    public String  dialogchangelang(final Context ctx) {

        prefrence = SharedPrefrence.getInstance(ctx);



        final Dialog dialog = new Dialog(ctx/*, android.R.style.Theme_Dialog*/);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dailog_lang);
        dialog.show();
        dialog.setCancelable(true);
        LinearLayout us = dialog.findViewById(R.id.us);
        LinearLayout arabic = dialog.findViewById(R.id.arabic);

        us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefrence.setValue("lang","us");
                new LanguageChange("",ctx);
//                try {
//
//                    Activity activity = (Activity) ctx;
//
//                    tv = activity.findViewById(R.id.lang);
//
//                    tv.setText("English");
//
//
//                }
//                catch (Exception e){}





                Context context = ctx;

                Locale locale;
                //Log.e("Lan",session.getLanguage());
                locale = new Locale("");
                Configuration config = new Configuration(context.getResources().getConfiguration());
                Locale.setDefault(locale);
                config.setLocale(locale);
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());



//                Locale locale;
//                //Log.e("Lan",session.getLanguage());
//                locale = new Locale("");
//                Configuration config = new Configuration(getResources().getConfiguration());
//                Locale.setDefault(locale);
//                config.setLocale(locale);
//                getResources().updateConfiguration(config, getResources().getDisplayMetrics());



                //recreate();
                Activity activity = (Activity) context;


                activity.recreate();
                dialog.dismiss();

                language = "English";



            }
        });
        arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefrence.setValue("lang","arabic");
                //setup1
                new LanguageChange("ar",ctx);
//                Locale locale;
//                //Log.e("Lan",session.getLanguage());
//                locale = new Locale("");
//                Configuration config = new Configuration(getResources().getConfiguration());
//                Locale.setDefault(locale);
//                config.setLocale(locale);
//                getResources().updateConfiguration(config, getResources().getDisplayMetrics());


                Context  context = ctx;

                Locale locale;
                //Log.e("Lan",session.getLanguage());
                locale = new Locale("ar");
                Configuration config = new Configuration(context.getResources().getConfiguration());
                Locale.setDefault(locale);
                config.setLocale(locale);
                context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());


                Activity activity = (Activity) context;


                activity.recreate();
                dialog.dismiss();

//                try {
//
//
//
//                    tv = activity.findViewById(R.id.lang);
//
//                    tv.setText("Arabic");
//
//
//                }
//                catch (Exception e){}

                language = "Arabic";


            }
        });


        return language;

    }


}
