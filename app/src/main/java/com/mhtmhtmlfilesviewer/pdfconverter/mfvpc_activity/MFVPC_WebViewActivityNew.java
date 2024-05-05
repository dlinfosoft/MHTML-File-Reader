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
    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        MFVPC_WebViewActivityNew.this.mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        if (adflag) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                                createWebPagePrint(webview);
                                            }
                                        } else {
                                            rate_us();
                                        }
                                        MFVPC_WebViewActivityNew.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MFVPC_WebViewActivityNew.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;


                    }
                });
    }


    @Override
    protected void onDestroy() {


        super.onDestroy();
    }


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

        loadAd();
        initMembers();
        eventClick();
        onLayoutFinishedLoad();
    }

    public class WebViewJavaScriptInterface {
        public WebViewJavaScriptInterface(Context context) {
        }

        @JavascriptInterface
        public void print(final String str) {
            runOnUiThread(new Runnable() {
                public void run() {

                    Log.e("initWebView123", " WebViewJavaScriptInterface ::: " + str);
                    doWebViewPrint(str);
                }
            });
        }
    }

    private void doWebViewPrint(String str) {
        webview.setWebViewClient(new WebViewClient() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            public void onPageFinished(WebView webView, String str) {
                createWebPagePrint(webView);
//                createWebPagePrint(recentFileModel.mhtFilePath);
                super.onPageFinished(webView, str);
            }

            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                return false;
            }
        });
        Log.e("initWebView123", " loadUrl ::: 2");
        webview.loadDataWithBaseURL(null, str, "text/html", HTTP.UTF_8, null);
    }



    private void initMembers() {
        webview = (WebView) findViewById(R.id.webview);
        filename = (TextView) findViewById(R.id.file_name);
        urltv = (TextView) findViewById(R.id.url);
        unsupported = (TextView) findViewById(R.id.unsupported_txt);
        iv_back = (ImageView) findViewById(R.id.back_iv);
        printll = (LinearLayout) findViewById(R.id.printll);
        spinKitView = (SpinKitView) findViewById(R.id.spin_kit);

    }

    @Override
    public void onBackPressed() {
        adflag = false;
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(MFVPC_WebViewActivityNew.this);
            } else {
                rate_us();
            }

        }


    }

    public void eventClick() {

        printll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adflag = true;
                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MFVPC_WebViewActivityNew.this);
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        createWebPagePrint(webview);
                    }
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private class Browser_home extends WebViewClient {

        Browser_home() {
        }

        @SuppressWarnings("deprecation")
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

            return AdBlockerWebView.blockAds(view, url) ? AdBlocker.createEmptyResource() :
                    super.shouldInterceptRequest(view, url);

        }

    }

    private void initWebView() {
        webview.setWebViewClient(new Browser_home());
        webview.setWebChromeClient(new WebChromeClient());
        webview.clearHistory();
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                return false;
            }
        });
        webview.clearCache(true);
        webview.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView webView, String str) {
                if (MFVPC_Utils.isNullOrEmpty(str).booleanValue()) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                String fileNameWithExtention = MFVPC_UtilsFiles.getFileNameWithExtention(new File(str));
                if (MFVPC_Utils.isNullOrEmpty(fileNameWithExtention).booleanValue()) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                File file = null;
                File[] listFiles = MFVPC_Utils.isNullOrEmpty(recentFileModel.dirPath).booleanValue() ? null : new File(recentFileModel.dirPath).listFiles();
                if (MFVPC_Utils.isNullOrEmpty(listFiles).booleanValue()) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                for (File file2 : listFiles) {
                    String fileNameWithExtention2 = MFVPC_UtilsFiles.getFileNameWithExtention(file2);
                    if (!MFVPC_Utils.isNullOrEmpty(listFiles).booleanValue() && fileNameWithExtention.equals(fileNameWithExtention2)) {
                        file = file2;
                    }
                }
                if (file == null) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                if (!file.exists()) {
                    return super.shouldOverrideUrlLoading(webView, str);
                }
                MFVPC_UtilsFiles.openFileByPath(MFVPC_WebViewActivityNew.this, file);
                return true;
            }
        });
        webview.addJavascriptInterface(new WebViewJavaScriptInterface(this), "androidapp");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createWebPagePrint(WebView webView) {
        PrintManager printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
        PrintDocumentAdapter createPrintDocumentAdapter = webView.createPrintDocumentAdapter();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.app_name));
        stringBuilder.append(" Document");
        printManager.print(stringBuilder.toString(), createPrintDocumentAdapter, new PrintAttributes.Builder().build());
    }


    public void onLayoutFinishedLoad() {
        initWebView();
        if (!MFVPC_Utils.isNullOrEmpty(recentFileModel.mhtFilePath).booleanValue()) {
            WebView webView = webview;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("file://");
            stringBuilder.append(recentFileModel.mhtFilePath.replace("#", ""));
            startWebView(stringBuilder.toString(), webView);
        }
    }


    private void startWebView(String url, WebView webView) {
        final ProgressDialog progressDialog;
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new MyCustomWebViewClient());
        webView.setInitialScale(1);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        spinKitView.setVisibility(View.VISIBLE);
        unsupported.setVisibility(View.GONE);


        webView.loadUrl(url);
    }

    private class MyCustomWebViewClient extends WebViewClient {
        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
        }

        private MyCustomWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(final WebView webView, String str) {
            MFVPC_WebViewActivityNew.this.runOnUiThread(new Runnable() {
                public void run() {
                    filename.setText(webView.getTitle().toString());
                    urltv.setText(webView.getUrl().toString());
                }
            });
            if (str.startsWith("http") || str.startsWith("https")) {
                return false;
            }
            if (str.startsWith("intent")) {
                try {
                    String stringExtra = Intent.parseUri(str, Intent.URI_INTENT_SCHEME).getStringExtra("browser_fallback_url");
                    if (stringExtra != null) {
                        webView.loadUrl(stringExtra);
                    }
                } catch (URISyntaxException unused) {
                }
            }
            return true;
        }

        public void onLoadResource(WebView webView, String str) {
            super.onLoadResource(webView, str);
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            spinKitView.setVisibility(View.GONE);
            Log.i(TAG, "GOT Page error : code : " + i + " Desc : " + str);
            super.onReceivedError(webView, i, str, str2);
        }

        public void onPageFinished(final WebView webView, String str) {
            spinKitView.setVisibility(View.GONE);
            MFVPC_WebViewActivityNew.this.runOnUiThread(new Runnable() {
                public void run() {
                    filename.setText(webView.getTitle().toString());
                    urltv.setText(webView.getUrl().toString());
                }
            });
        }
    }


    protected void onPause() {
        super.onPause();

        WebView webView = webview;
        if (webView != null) {
            webView.onPause();
        }
    }

    protected void onResume() {
        super.onResume();
        WebView webView = webview;
        if (webView != null) {
            webView.onResume();
        }

    }


    public void rate_us() {
        Intent in;
        if (backState.equalsIgnoreCase("MFVPC_FilesSelection")) {
            if (Build.VERSION.SDK_INT > 29) {
                in = new Intent(this, MFVPC_MainActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(in);
                try {
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                } catch (Exception e) {

                }
                return;
            }

            in = new Intent(this, MFVPC_FilesSelection.class);

        } else {
            in = new Intent(this, MFVPC_RecentActivity.class);

        }
        in.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(in);
        try {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } catch (Exception e) {

        }
    }


}
