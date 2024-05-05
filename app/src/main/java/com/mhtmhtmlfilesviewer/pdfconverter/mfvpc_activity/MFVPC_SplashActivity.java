package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.mhtmhtmlfilesviewer.pdfconverter.R;

public class MFVPC_SplashActivity extends AppCompatActivity {
    String TAG="MFVPC_SplashActivity";
    Handler handler;
    Runnable myRunnable;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.mfvpc_splash);


        try {
            handler =  new Handler();
            myRunnable = new Runnable() {
                public void run() {
                    // do something

                        Intent mainIntent = new Intent(MFVPC_SplashActivity.this, MFVPC_MainActivity.class);
                        startActivity(mainIntent);


                }
            };
        }catch ( Exception e){

        }



    }


    @Override
    protected void onPause() {
        try {
            handler.removeCallbacks(myRunnable);
        }catch ( Exception e){

        }
        super.onPause();
    }


    @Override
    protected void onResume() {

        try {
            handler.postDelayed(myRunnable,3000);
        }catch ( Exception e){
        }
        super.onResume();
    }


}
