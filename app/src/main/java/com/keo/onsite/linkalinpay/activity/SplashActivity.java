package com.keo.onsite.linkalinpay.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.keo.onsite.linkalinpay.R;
import com.keo.onsite.linkalinpay.activity.shared.UserShared;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);*/

    mythread();

    }


        public void mythread(){
            Thread th=new Thread(){

                @Override
                public void run(){
                    try {
                        Thread.sleep(2000);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(new UserShared(SplashActivity.this).getLoggedInStatus_seller()){
                                    Intent intent = new Intent(SplashActivity.this,DashBoardActivity.class);
                                    startActivity(intent);
                                }else if(new UserShared(SplashActivity.this).getLoggedInStatus_customer()){
                                Intent i=new Intent(SplashActivity.this,MainDashboardActivity.class);
                                 startActivity(i);

                                }
                                else {

                                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                    startActivity(intent);
                                }

                            }
                        });
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }; //End of thread class

            th.start();
        }//End of myThread()



    }
