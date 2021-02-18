package com.keo.onsite.linkalinpay.activity.Translation;//


// Created by Rajat Saha on 30-11-2020.
// Copyright (c) 2020 HTSM. All rights reserved.
//

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.StrictMode;

import com.keo.onsite.linkalinpay.R;

import java.io.IOException;
import java.io.InputStream;

public class Translation  {
//    public void getTranslateService(Context ctx) {
//
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);
//
//        try (InputStream is = ctx.getResources().openRawResource(R.raw.credentials)) {
//
//            //Get credentials:
//            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);
//
//            //Set credentials and get translate service:
//            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
//            translate = translateOptions.getService();
//
//        } catch (IOException ioe) {
//            ioe.printStackTrace();
//
//        }
//    }
//
//    public void translate() {
//
//        //Get input text to be translated:
//        originalText = inputToTranslate.getText().toString();
//        Translation translation = translate.translate(originalText, Translate.TranslateOption.targetLanguage("tr"), Translate.TranslateOption.model("base"));
//        translatedText = translation.getTranslatedText();
//
//        //Translated text and original text are set to TextViews:
//        translatedTv.setText(translatedText);
//
//    }
//
//    public boolean checkInternetConnection(Context ctx) {
//
//        //Check internet connection:
//        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        //Means that we are connected to a network (mobile or wi-fi)
//        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
//                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
//
//        return connected;
//    }

}



