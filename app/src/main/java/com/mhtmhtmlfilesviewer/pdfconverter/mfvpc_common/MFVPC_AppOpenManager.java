package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.mhtmhtmlfilesviewer.pdfconverter.R;

import java.util.Date;

public class MFVPC_AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static String AD_UNIT_ID = "abc";
    private static final String LOG_TAG = "BSMC_AppOpenManager";
    private static boolean isShowingAd = false;
    private AppOpenAd appOpenAd = null;
    private Activity currentActivity;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;
    private long loadTime = 0;
    private final MFVPC_App myApplication;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public MFVPC_AppOpenManager(MFVPC_App myApplication2) {
        this.myApplication = myApplication2;
        AD_UNIT_ID = myApplication2.getString(R.string.app_open_id);
        myApplication2.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onStart() {
        showAdIfAvailable();
        Log.d(LOG_TAG, "onStart");
    }

    public void fetchAd() {
        if (!isAdAvailable()) {
            this.loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                }

                public void onAdLoaded(AppOpenAd appOpenAd) {
                    MFVPC_AppOpenManager.this.appOpenAd = appOpenAd;
                    MFVPC_AppOpenManager.this.loadTime = new Date().getTime();
                }
            };
            AppOpenAd.load(this.myApplication, AD_UNIT_ID, getAdRequest(), 1, this.loadCallback);
        }
    }

    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }

    private boolean wasLoadTimeLessThanNHoursAgo(long j) {
        return new Date().getTime() - this.loadTime < j * 3600000;
    }

    public boolean isAdAvailable() {
        return this.appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }

    public void onActivityStarted(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityResumed(Activity activity) {
        this.currentActivity = activity;
    }

    public void onActivityDestroyed(Activity activity) {
        this.currentActivity = null;
    }

    public void showAdIfAvailable() {
        if (isShowingAd || !isAdAvailable()) {
            Log.d(LOG_TAG, "Can not show ad.");
            fetchAd();
            return;
        }
        Log.d(LOG_TAG, "Will show ad.");
        new FullScreenContentCallback() {
            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                MFVPC_AppOpenManager.this.appOpenAd = null;
                boolean unused = MFVPC_AppOpenManager.isShowingAd = false;
                MFVPC_AppOpenManager.this.fetchAd();
            }

            @Override
            public void onAdShowedFullScreenContent() {
                boolean unused = MFVPC_AppOpenManager.isShowingAd = true;
            }
        };
        this.appOpenAd.show(this.currentActivity);
    }
}
