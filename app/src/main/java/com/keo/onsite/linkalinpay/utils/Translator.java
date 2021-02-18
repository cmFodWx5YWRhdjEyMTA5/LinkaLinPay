package com.keo.onsite.linkalinpay.utils;//


// Created by Rajat Saha on 25-08-2020.
// Copyright (c) 2020 HTSM. All rights reserved.
//


import android.content.Context;
import android.os.StrictMode;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.MainActivity;
import com.keo.onsite.linkalinpay.preferences.SharedPrefrence;

import java.io.IOException;
import java.io.InputStream;

public class Translator {

    private EditText inputToTranslate;
    private TextView translatedTv;
    private String originalText;
    private String translatedText;
    private boolean connected;
    Translate translate;
    SharedPrefrence prefrence;

//    public void translation(String text, final Trans trans) {
//
//
//        //translation
//        TranslateAPI translateAPI = new TranslateAPI(
//                Language.AUTO_DETECT,   //Source Language
//                Language.ARABIC,         //Target Language
//                text);           //Query Text
//
//        translateAPI.setTranslateListener(new TranslateAPI.TranslateListener() {
//            @Override
//            public void onSuccess(String translatedText) {
//
//                trans.backResponseTrans(translatedText);
//
////                result = translatedText;
////                Toast.makeText(context, translatedText, Toast.LENGTH_SHORT).show();
//
//            }
//
//            @Override
//            public void onFailure(String ErrorText) {
//                //Log.d(TAG, "onFailure: "+ErrorText);
//            }
//        });
//
//    }


    public void getTranslateService(Context ctx) {

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        prefrence = SharedPrefrence.getInstance(ctx);
        try (InputStream is = ctx.getResources().openRawResource(R.raw.tcredential)) {

            //Get credentials:

            final GoogleCredentials myCredentials = GoogleCredentials.fromStream(is);

            //Set credentials and get translate service:
            TranslateOptions translateOptions = TranslateOptions.newBuilder().setCredentials(myCredentials).build();
            translate = translateOptions.getService();

        } catch (IOException ioe) {
            ioe.printStackTrace();

        }


    }

    public String  translate(Context ct ,String text) {

        //Get input text to be translated:
        //ar-KW


        if (prefrence.getValue("lang").equals("us")) {

          //  Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("ar"), Translate.TranslateOption.model("base"));
            translatedText = text;

        } else {
            Translation translation = translate.translate(text, Translate.TranslateOption.targetLanguage("ar"), Translate.TranslateOption.model("base"));
            translatedText = translation.getTranslatedText();


        }


      //  Toast.makeText(ct,translatedText,Toast.LENGTH_SHORT).show();

        //Translated text and original text are set to TextViews:
//        translatedTv.setText(translatedText);

        return translatedText;

    }

}