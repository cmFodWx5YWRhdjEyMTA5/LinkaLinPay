package com.keo.onsite.linkalinpay.utils;//


// Created by Rajat Saha on 22-07-2020.
// Copyright (c) 2020 HTSM. All rights reserved.
//

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class LanguageChange {

    public LanguageChange(){

    }

    public LanguageChange(String lang, Context baseContext){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        baseContext.getResources().updateConfiguration(config,baseContext.getResources().getDisplayMetrics());
        //sp
        SharedPreferences.Editor editor = baseContext.getSharedPreferences("Settings",MODE_PRIVATE).edit();
        editor.putString("mylang",lang);
        editor.apply();


    }
    public void LoadLocal(Context ctx){

        SharedPreferences preferences = ctx.getSharedPreferences("Settings",MODE_PRIVATE);
        String Lang = preferences.getString("mylang","");
        new LanguageChange(Lang,ctx);


    }

}
