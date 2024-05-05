package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mhtmhtmlfilesviewer.pdfconverter.R;

public class MFVPC_Privacy_policy extends AppCompatActivity {
     ImageView iv_back;

    private AdView mAdView;

    void loadGoogleBannerads() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {

        super.onDestroy();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mfvpc_privacy_policy);
        loadGoogleBannerads();
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarcl));
        }
        WebView wbPrivacyPolicy;
        wbPrivacyPolicy = (WebView) findViewById(R.id.privacypolicyweb);
        wbPrivacyPolicy.getSettings().setJavaScriptEnabled(true);
        wbPrivacyPolicy
                .loadUrl("file:///android_asset/mfvpc__privacypolicy.html");




        iv_back=(ImageView)findViewById(R.id.back_iv);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent=new Intent(MFVPC_Privacy_policy.this, MFVPC_MainActivity.class);
        startActivity(intent);
        finish();
    }
}
