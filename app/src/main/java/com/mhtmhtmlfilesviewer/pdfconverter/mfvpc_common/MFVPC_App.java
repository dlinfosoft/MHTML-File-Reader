package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MFVPC_App extends MultiDexApplication {
    static MFVPC_App a;
    private static MFVPC_AppOpenManager appOpenManager;
    public static MFVPC_App GetApp() {
        return a;
    }

    public void onCreate() {
        super.onCreate();
        a = this;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        appOpenManager = new MFVPC_AppOpenManager(this);

    }
}
