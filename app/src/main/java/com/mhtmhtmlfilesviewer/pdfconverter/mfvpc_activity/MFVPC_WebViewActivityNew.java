package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ybq.android.spinkit.SpinKitView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.mhtmhtmlfilesviewer.pdfconverter.R;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Utils;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UtilsFiles;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_RecentFileModel;
import com.monstertechno.adblocker.AdBlockerWebView;
import com.monstertechno.adblocker.util.AdBlocker;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class MFVPC_WebViewActivityNew extends AppCompatActivity {
    WebView webview;
    MFVPC_RecentFileModel recentFileModel;
    SharedPreferences preferences;
    String TAG = "MFVPC_WebViewActivityNew ";
    ImageView iv_back;
    public TextView filename, urltv, unsupported;
    public static String filestr;
    LinearLayout printll;
    String backState;
    SpinKitView spinKitView;
    boolean adflag = false;
    private InterstitialAd mInterstitialAd;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mfvpc_web_view_new);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarcl));
        }


    }


}
