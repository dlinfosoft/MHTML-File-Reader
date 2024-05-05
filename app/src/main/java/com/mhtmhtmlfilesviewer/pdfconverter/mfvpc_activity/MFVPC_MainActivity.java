package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_activity;

import static com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UtilsFiles.getFileNameWithoutExtention;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;


import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Gravity;

import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.material.navigation.NavigationView;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mhtmhtmlfilesviewer.pdfconverter.R;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_MHTParser;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_MHTUnpack;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UriDeserializer;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Utils;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UtilsFiles;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.RealPathUtil;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_Attachment;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_RecentFileModel;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.mail.MessagingException;

public class MFVPC_MainActivity extends AppCompatActivity {
    String TAG = "MFVPC_MainActivity";
    String rationale;
    Permissions.Options options;
    String[] permissions;
    LinearLayout openFile;
    ImageView drawerbtn,more;
    LinearLayout recentll, rateUs, sharell, privavcy;
    DrawerLayout drawer;
    NavigationView navigationView;
    Context context;


    String errorStr;
    Boolean D;
    Uri uri;
    SharedPreferences sharedPreferences;
    Boolean x;
    Boolean y;
    LinearLayout fab;
    ArrayList<String> extensionList = new ArrayList<>();
    private NativeAd nativeAd;
    TextView tvNavAds;

    private AdView mAdView;

    void loadGoogleBannerads() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mfvpc_activity_main);
        context = this;

        loadGoogleBannerads();

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarc2));
        }


        rationale = "Please provide storage permission so that you can ...";
        options = new Permissions.Options()
                .setRationaleDialogTitle("Permissions")
                .setSettingsDialogTitle("Warning");

        permissions = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};

        findbyID();
        eventClick();
        initData();

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenuExample();
            }
        });


        openFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (Build.VERSION.SDK_INT > 29) {
                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                        intent.setType("*/*");
                        startActivityForResult(intent, 4444);
                    }else {
                        // do your task.
                        Intent intent01 = new Intent(MFVPC_MainActivity.this, MFVPC_FilesSelection.class);
                        intent01.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(intent01);
                        try {
                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                        } catch (Exception e) {

                        }
                    }
                    return;
                }
                Permissions.check(MFVPC_MainActivity.this/*context*/, permissions, rationale/*rationale*/, options/*options*/, new PermissionHandler() {
                    @Override
                    public void onGranted() {

                        if (Build.VERSION.SDK_INT > 29) {
                            Intent intent = new Intent("android.intent.action.GET_CONTENT");
                            intent.setType("*/*");
                             startActivityForResult(intent, 4444);
                        }else {
                            // do your task.
                            Intent intent01 = new Intent(MFVPC_MainActivity.this, MFVPC_FilesSelection.class);
                            intent01.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                            startActivity(intent01);
                            try {
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                        super.onDenied(context, deniedPermissions);

                        Log.i("MFVPC_MainActivity", " onDenied " + deniedPermissions.size());
                    }
                });


            }
        });
    }

    private void popupMenuExample() {
        PopupMenu p = new PopupMenu(MFVPC_MainActivity.this, more);
        p.getMenuInflater().inflate(R.menu.popup_menu, p .getMenu());
        p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.recent:

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            Intent intent = new Intent(MFVPC_MainActivity.this, MFVPC_RecentActivity.class);
                            startActivity(intent);
                            try {
                                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                            } catch (Exception e) {
                            }
                        }else{
                            Permissions.check(MFVPC_MainActivity.this/*context*/, permissions, rationale/*rationale*/, options/*options*/, new PermissionHandler() {
                                @Override
                                public void onGranted() {
                                    Intent intent = new Intent(MFVPC_MainActivity.this, MFVPC_RecentActivity.class);
                                    startActivity(intent);
                                    try {
                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                    } catch (Exception e) {
                                    }

                                }
                                @Override
                                public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                                    super.onDenied(context, deniedPermissions);
                                    Log.i("MFVPC_MainActivity", " onDenied " + deniedPermissions.size());
                                }
                            });
                        }

                        break;
                    case R.id.share:
                        try {
                            Intent ix = new Intent(Intent.ACTION_SEND);
                            ix.setType("text/plain");
                            ix.putExtra(Intent.EXTRA_SUBJECT, getResources().getString(R.string.app_name));
                            String link = "Download " + getResources().getString(R.string.app_name) + " app from   - https://play.google.com/store/apps/details?id="
                                    + getPackageName();
                            ix.putExtra(Intent.EXTRA_TEXT, link);
                            startActivity(Intent.createChooser(ix, "Share Application"));
                        } catch (Exception e) {
                        }
                        break;
                    case R.id.rateus:
                        rateusDailogeMsg(context);
                        break;
                    case R.id.privacy:
                        Intent intent = new Intent(MFVPC_MainActivity.this, MFVPC_Privacy_policy.class);
                        startActivity(intent);
                        finish();
                        break;
                }
                return true;
            }
        });
        p.show();
    }

    public void eventClick() {
        drawerbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
                if (!drawer.isDrawerOpen(GravityCompat.START)) drawer.openDrawer(Gravity.START);
                else drawer.closeDrawer(Gravity.END);
            }
        });

        recentll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        sharell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        rateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        privavcy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void rateusDailogeMsg(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.mfvpc_rate_us_layout2);

        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        dialog.show();

        TextView no_btn = dialog.findViewById(R.id.never);
        TextView yes_btn = dialog.findViewById(R.id.rate_now);

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(goToMarket);
                dialog.dismiss();


            }
        });

    }


    public void findbyID() {
        tvNavAds = (TextView) findViewById(R.id.tv_navads);
        more = (ImageView) findViewById(R.id.more);

        refreshAd();

        this.extensionList.add("jpg");
        this.extensionList.add("webp");
        this.extensionList.add("png");
        this.extensionList.add("avif");
        this.extensionList.add("gif");
        this.extensionList.add("mp4");
        this.extensionList.add("3gp");
        this.extensionList.add("webm");
        this.extensionList.add("mp3");
        this.extensionList.add("m4a");
        this.extensionList.add("aac");
        this.extensionList.add("flac");
        this.extensionList.add("ogg");
        this.extensionList.add("txt");
        this.extensionList.add("log");
        this.extensionList.add("xml");
        this.extensionList.add("json");
        this.extensionList.add("yaml");
        this.extensionList.add("yml");
        this.extensionList.add("vb");
        this.extensionList.add("swift");
        this.extensionList.add("sh");
        this.extensionList.add("py");
        this.extensionList.add("pl");
        this.extensionList.add("php");
        this.extensionList.add("kt");
        this.extensionList.add("js");
        this.extensionList.add("java");
        this.extensionList.add("ini");
        this.extensionList.add("htaccess");
        this.extensionList.add("h");
        this.extensionList.add("groovy");
        this.extensionList.add("gradle");
        this.extensionList.add("dart");
        this.extensionList.add("css");
        this.extensionList.add("cs");
        this.extensionList.add("cpp");
        this.extensionList.add("c");
        this.extensionList.add("html");
        this.extensionList.add("htm");
        this.extensionList.add("mht");
        this.extensionList.add("mhtml");
        this.extensionList.add("xhtml");


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        openFile = (LinearLayout) findViewById(R.id.open_file);
        drawerbtn = (ImageView) findViewById(R.id.back_button);
        recentll = (LinearLayout) navigationView.findViewById(R.id.recent_files);
        rateUs = (LinearLayout) navigationView.findViewById(R.id.rates_us);
        sharell = (LinearLayout) navigationView.findViewById(R.id.share_files);
        privavcy = (LinearLayout) navigationView.findViewById(R.id.privacy_policy);
    }


    @Override
    public void onBackPressed() {

        try {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                ShowDailogeMsg(MFVPC_MainActivity.this);

            }
        } catch (Exception e) {
            ShowDailogeMsg(MFVPC_MainActivity.this);
        }

    }

    @SuppressLint("MissingPermission")
    private void refreshAd() {
        AdLoader.Builder builder = new AdLoader.Builder(this, getString(R.string.native_advanced));
        builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {


            @Override
            public final void onNativeAdLoaded(NativeAd nativeAd) {
              lambda$refreshAd$0$CommonSettingActivity(nativeAd);
            }
        });
        builder.withNativeAdOptions(new NativeAdOptions.Builder().setVideoOptions(new VideoOptions.Builder().setStartMuted(true).build()).build());
        builder.withAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                String.format("domain: %s, code: %d, message: %s", loadAdError.getDomain(), Integer.valueOf(loadAdError.getCode()), loadAdError.getMessage());
            }
        }).build().loadAd(new AdRequest.Builder().build());
    }

    public void lambda$refreshAd$0$CommonSettingActivity(NativeAd nativeAd2) {
        this.tvNavAds.setVisibility(View.GONE);
        if ((Build.VERSION.SDK_INT >= 17 ? isDestroyed() : false) || isFinishing() || isChangingConfigurations()) {
            nativeAd2.destroy();
            return;
        }
        NativeAd nativeAd3 = this.nativeAd;
        if (nativeAd3 != null) {
            nativeAd3.destroy();
        }
        this.nativeAd = nativeAd2;
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.fl_adplaceholder);
        NativeAdView nativeAdView = (NativeAdView) getLayoutInflater().inflate(R.layout.sbga_ad_unified, (ViewGroup) null);
        populateUnifiedNativeAdView(nativeAd2, nativeAdView);
        frameLayout.removeAllViews();
        frameLayout.addView(nativeAdView);
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd2, NativeAdView nativeAdView) {
        nativeAdView.setMediaView((MediaView) nativeAdView.findViewById(R.id.ad_media));
        nativeAdView.setHeadlineView(nativeAdView.findViewById(R.id.ad_headline));
        nativeAdView.setBodyView(nativeAdView.findViewById(R.id.ad_body));
        nativeAdView.setCallToActionView(nativeAdView.findViewById(R.id.ad_call_to_action));
        nativeAdView.setIconView(nativeAdView.findViewById(R.id.ad_app_icon));
        nativeAdView.setPriceView(nativeAdView.findViewById(R.id.ad_price));
        nativeAdView.setStarRatingView(nativeAdView.findViewById(R.id.ad_stars));
        nativeAdView.setStoreView(nativeAdView.findViewById(R.id.ad_store));
        nativeAdView.setAdvertiserView(nativeAdView.findViewById(R.id.ad_advertiser));
        ((TextView) nativeAdView.getHeadlineView()).setText(nativeAd2.getHeadline());
        nativeAdView.getMediaView().setMediaContent(nativeAd2.getMediaContent());
        if (nativeAd2.getBody() == null) {
            nativeAdView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getBodyView()).setText(nativeAd2.getBody());
        }
        if (nativeAd2.getCallToAction() == null) {
            nativeAdView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) nativeAdView.getCallToActionView()).setText(nativeAd2.getCallToAction());
        }
        if (nativeAd2.getIcon() == null) {
            nativeAdView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) nativeAdView.getIconView()).setImageDrawable(nativeAd2.getIcon().getDrawable());
            nativeAdView.getIconView().setVisibility(View.VISIBLE);
        }
        if (nativeAd2.getPrice() == null) {
            nativeAdView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getPriceView()).setText(nativeAd2.getPrice());
        }
        if (nativeAd2.getStore() == null) {
            nativeAdView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            nativeAdView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) nativeAdView.getStoreView()).setText(nativeAd2.getStore());
        }
        if (nativeAd2.getStarRating() == null) {
            nativeAdView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) nativeAdView.getStarRatingView()).setRating(nativeAd2.getStarRating().floatValue());
            nativeAdView.getStarRatingView().setVisibility(View.VISIBLE);
        }
        if (nativeAd2.getAdvertiser() == null) {
            nativeAdView.getAdvertiserView().setVisibility(View.INVISIBLE);
        } else {
            ((TextView) nativeAdView.getAdvertiserView()).setText(nativeAd2.getAdvertiser());
            nativeAdView.getAdvertiserView().setVisibility(View.VISIBLE);
        }
        nativeAdView.setNativeAd(nativeAd2);
    }



    private void ShowDailogeMsg(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.mfvpc_exit_dialog_view);

        dialog.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setBackgroundDrawableResource(
                android.R.color.transparent);
        Window window = dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = (int) (Resources.getSystem().getDisplayMetrics().widthPixels / 1.2);
        lp.dimAmount = 0.7f;
        lp.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        TextView no_btn = dialog.findViewById(R.id.No_btn);
        TextView yes_btn = dialog.findViewById(R.id.Yes_btn);

        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    dialog.dismiss();
                    finishAffinity();

                } else {
                    dialog.dismiss();
                    finish();
                }

            }
        });

    }


    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == Activity.RESULT_OK && i == 4444) {
            uri = intent.getData();
            if (uri != null) {
                String path = null;
                String name=".mht";

                try {
                    path = MFVPC_UtilsFiles.getFilePathByURI(this, uri);
                    name = MFVPC_UtilsFiles.getFileNameWithoutExtention(new File(path));
                    if(name!=null){
                        MFVPC_WebViewActivityNew.filestr = name;
                    }else {
                        MFVPC_WebViewActivityNew.filestr = "file " + System.currentTimeMillis();
                    }
                }catch ( Exception e){
                    MFVPC_WebViewActivityNew.filestr = "file " + System.currentTimeMillis();
                }
                new OpenMhtFile(path).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);


                return;

            }
        }

    }


    private File[] getFilesFromRecentsDir() {
        return new File(getMhtRecentFilesDir()).listFiles();
    }

    private class OpenMhtFile extends AsyncTask<String, String, MFVPC_RecentFileModel> {
        ProgressDialog progressDialog;
        String fname;

        private OpenMhtFile(String name) {
            fname = name;
        }


        public MFVPC_RecentFileModel doInBackground(String... strArr) {
            GsonBuilder registerTypeAdapter = new GsonBuilder().registerTypeAdapter(Uri.class, new MFVPC_UriDeserializer());
            File[] b2 = getFilesFromRecentsDir();
            if (!MFVPC_Utils.isNullOrEmpty(b2).booleanValue()) {
                for (File file : b2) {
                    if (file.exists()) {


                        MFVPC_RecentFileModel recentFileModel = null;
                        String gsonStrFromFile = MFVPC_UtilsFiles.getGsonStrFromFile(file);
                        if (!MFVPC_Utils.isNullOrEmpty(gsonStrFromFile).booleanValue()) {
                            recentFileModel = (MFVPC_RecentFileModel) registerTypeAdapter.create().fromJson(gsonStrFromFile, new TypeToken<MFVPC_RecentFileModel>() {

                            }.getType());
                        }
                        if (recentFileModel != null && !MFVPC_Utils.isNullOrEmpty(recentFileModel.origUriStr).booleanValue() && recentFileModel.origUriStr.equals(uri.toString())) {
                            try {
                                if (file.delete()) {
                                    Log.d("Files123456", " file delete  ");
                                } else {
                                    Log.d("Files123456", " delete not  ");
                                }
                                File file1 = new File(recentFileModel.dirPath);
                                if (file1.exists()) {
                                    if (file1.delete()) {
                                        Log.d("Files123456", " file 1 delete  ");
                                    } else {
                                        Log.d("Files123456", " file 1 delete not  ");
                                    }
                                }
                            } catch (Exception e) {
                                return recentFileModel;
                            }

                        }
                    }
                }
            }


            String a2 = getPathFromUri(uri, y, fname);


            Log.i("Name123", " a2 " + a2);


            new MFVPC_RecentFileModel();
            if (MFVPC_Utils.isNullOrEmpty(a2).booleanValue()) {
                MFVPC_RecentFileModel recentFileModel2 = new MFVPC_RecentFileModel();
                recentFileModel2.origUriStr = uri.toString();
                Log.i("LoadRecentFilesData", " return 2 ");
                return recentFileModel2;
            }
            File file2 = new File(a2);


            Log.i("LoadRecentFilesData", "getPathFromUri" + file2.getAbsolutePath());

            long currentTimeMillis = System.currentTimeMillis();
            String valueOf = String.valueOf(currentTimeMillis);
            MFVPC_RecentFileModel recentFileModel3 = new MFVPC_RecentFileModel();


            recentFileModel3.mhtFileName = MFVPC_UtilsFiles.getFileNameWithoutExtention(file2);

            recentFileModel3.timeStamp = currentTimeMillis;
            recentFileModel3.dirPath = getMhtContentsDir(valueOf);
            recentFileModel3.origUriStr = uri.toString();
            recentFileModel3.mhtFilePath = file2.getAbsolutePath();


            if (!file2.exists()) {
                Log.i("LoadRecentFilesData", " return 3 ");
                return recentFileModel3;
            }
            File file3 = new File(recentFileModel3.dirPath);
            long j = 0;
            try {
                j = (file2.length() / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID) / PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (j <= 30) {
                if (j > 10) {
                    try {
                        new MFVPC_MHTParser(file2, new File(recentFileModel3.dirPath)).decompress();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        e2.getStackTrace()[0].getLineNumber();
                    }
                } else {
                    openMhtFile(uri, recentFileModel3.dirPath);
                }
            }
            ArrayList<File> lstOfFilesInDir = MFVPC_UtilsFiles.getLstOfFilesInDir(file3);
            if (MFVPC_Utils.isNullOrEmpty((ArrayList) lstOfFilesInDir).booleanValue()) {
                recentFileModel3.loadMhtToWebView = true;
            }
            if (!recentFileModel3.loadMhtToWebView.booleanValue()) {
                Iterator<File> it = lstOfFilesInDir.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    File next = it.next();
                    String fileNameWithExtention = MFVPC_UtilsFiles.getFileNameWithExtention(next);
                    if (!MFVPC_Utils.isNullOrEmpty(fileNameWithExtention).booleanValue() && fileNameWithExtention.toLowerCase().contains("index") && fileNameWithExtention.toLowerCase().contains("htm")) {
                        recentFileModel3.pathOfHtmlFileToLoad = next.getAbsolutePath();
                        break;
                    }
                }
                if (MFVPC_Utils.isNullOrEmpty(recentFileModel3.pathOfHtmlFileToLoad).booleanValue()) {
                    Iterator<File> it2 = lstOfFilesInDir.iterator();
                    while (true) {
                        if (!it2.hasNext()) {
                            break;
                        }
                        File next2 = it2.next();
                        String fileNameWithExtention2 = MFVPC_UtilsFiles.getFileNameWithExtention(next2);
                        if (!MFVPC_Utils.isNullOrEmpty(fileNameWithExtention2).booleanValue() && (fileNameWithExtention2.toLowerCase().endsWith("htm") || fileNameWithExtention2.toLowerCase().endsWith("html"))) {
                            recentFileModel3.pathOfHtmlFileToLoad = next2.getAbsolutePath();
                        }
                    }
                }
            }
            if (MFVPC_Utils.isNullOrEmpty(recentFileModel3.pathOfHtmlFileToLoad).booleanValue()) {
                recentFileModel3.loadMhtToWebView = true;
            }
            String json = registerTypeAdapter.create().toJson(recentFileModel3, new TypeToken<MFVPC_RecentFileModel>() {
            }.getType());
            String str = getMhtRecentFilesDir() + valueOf + ".json";
            new File(getMhtRecentFilesDir(), valueOf + ".json");
            if (!MFVPC_Utils.isNullOrEmpty(json).booleanValue()) {
                try {
                    fileWrite(str, json);
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }

            try {
                Thread.sleep(400);
            } catch (InterruptedException e4) {
                e4.printStackTrace();
            }
            Log.i("LoadRecentFilesData", " return 4 ");
            return recentFileModel3;
        }


        public void onPostExecute(MFVPC_RecentFileModel recentFileModel) {
            super.onPostExecute(recentFileModel);
            ProgressDialog progressDialog = this.progressDialog;
            if (progressDialog != null) {
                try {
                    progressDialog.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (recentFileModel == null && MFVPC_Utils.isNullOrEmpty(errorStr).booleanValue()) {
                errorStr = getString(R.string.FileNotFoundOrFileTypeNotSupported);
            }
            if (recentFileModel != null && !recentFileModel.loadMhtToWebView.booleanValue() && !MFVPC_Utils.isNullOrEmpty(recentFileModel.errorStr).booleanValue()) {
                errorStr = recentFileModel.errorStr;
            }
            if (!MFVPC_Utils.isNullOrEmpty(errorStr).booleanValue()) {
                AlertDialog create = new AlertDialog.Builder(context).create();
                create.setTitle(getString(R.string.Error));
                create.setMessage(errorStr);
                create.setIcon((int) R.drawable.error_white);
                create.setButton(-1, getString(R.string.Confirm), new DialogInterface.OnClickListener() {
                    /* class gil.apps.mhtandroid.activities.LoadFilesActivity.OpenMhtFile.AnonymousClass3 */

                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                create.show();
                errorStr = null;
                return;
            }
            loadFileToWebView(recentFileModel);
            if (x.booleanValue()) {
                finish();
            }
        }

        public void onCancelled() {
            super.onCancelled();
            ProgressDialog progressDialog = this.progressDialog;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MFVPC_MainActivity.this);
            progressDialog.setTitle(getString(R.string.LoadingFile));
            progressDialog.setMessage(getString(R.string.Working));
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            try {
                progressDialog.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void openMhtFile(Uri uri, String str) {
        InputStream inputStream;
        String str2;
        StringBuilder sb;
        int i = 0;
        Collection<MFVPC_Attachment> collection = null;
        try {
            inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) {
                i = 0;
            } else {
                try {
                    i = inputStream.available();
                } catch (Exception e) {
                    e = e;
                    try {
                        e.printStackTrace();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (collection != null) {
                        }
                    } catch (Throwable th) {
                        th = th;
                        if (inputStream != null) {
                        }
                        throw th;
                    }
                }
            }
            if (i <= 0 || ((long) i) / PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED <= 30) {
                collection = MFVPC_MHTUnpack.unpack(inputStream);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (collection != null) {
                    try {
                        for (MFVPC_Attachment attachment : collection) {
                            if (attachment != null) {
                                String fileName = attachment.getFileName();
                                if (MFVPC_Utils.isNullOrEmpty(fileName).booleanValue()) {
                                    if (attachment.getMimeType().equals("application/octet-stream")) {
                                        sb = new StringBuilder();
                                        sb.append(str);
                                        sb.append("bogus.html");
                                    } else {
                                        sb = new StringBuilder();
                                        sb.append(str);
                                        sb.append("index.html");
                                    }
                                    str2 = sb.toString();
                                } else {
                                    str2 = str + fileName;
                                }
                                try {
                                    attachment.saveFile(str2);
                                } catch (Exception e3) {
                                    e3.printStackTrace();
                                }
                            }
                        }
                    } catch (MessagingException e4) {
                        e4.printStackTrace();
                    }
                }
            } else {
                errorStr = getString(R.string.mhtSizeError);
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                }
            }
        } catch (Exception e6) {
            inputStream = null;
            e6.printStackTrace();
            if (inputStream != null) {
            }
            if (collection != null) {
            }
        } catch (Throwable th2) {
            inputStream = null;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
            try {
                throw th2;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public static void fileWrite(String str, String str2) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(str);
        fileOutputStream.write(str2.getBytes());
        fileOutputStream.close();
    }

    public String getMhtContentsDir(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getMhtFileDir());
        stringBuilder.append(str);
        stringBuilder.append(File.separator);
        str = stringBuilder.toString();
        File file = new File(str);
        if (!file.exists()) {
            file.mkdir();
        }
        return str;
    }

    private String getMhtFileDir() {
        return getMhtFileDir(Boolean.valueOf(false));
    }

    public MFVPC_MainActivity() {
        Boolean valueOf = Boolean.valueOf(false);
        this.x = valueOf;
        this.y = valueOf;
        this.D = valueOf;
    }

    private void initData() {
        sharedPreferences = getSharedPreferences("LoadFilesActivity123", 0);
        y = Boolean.valueOf(this.sharedPreferences.getBoolean("useOldMethodForOpenMHT", false));

        try {
            startManagingCursor(new MatrixCursor(new String[]{"_id"}));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void startManagingCursor(Cursor cursor) {
        if (cursor == null) {
            try {
                cursor = new MatrixCursor(new String[]{"_id"});
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.startManagingCursor(cursor);
    }

    private String getMhtRecentFilesDir() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getFilesDir());
        stringBuilder.append(File.separator);
        stringBuilder.append("MHTRecentsDir");
        stringBuilder.append(File.separator);
        String stringBuilder2 = stringBuilder.toString();
        File file = new File(stringBuilder2);

        Log.i("LoadFilesActivity123", " getMhtRecentFilesDir 123 " + file.exists());

        if (!file.exists()) {
            file.mkdir();
        }
        return stringBuilder2;
    }

    private String getMhtFileDir(Boolean bool) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getFilesDir());
        stringBuilder.append(File.separator);
        stringBuilder.append(bool.booleanValue() ? "MHTDroidLegacy" : "MHTDroid");
        stringBuilder.append(File.separator);
        String stringBuilder2 = stringBuilder.toString();
        File file = new File(stringBuilder2);
        if (!file.exists()) {
            file.mkdir();
        }
        return stringBuilder2;
    }

    public void loadFileToWebView(MFVPC_RecentFileModel recentFileModel) {
        if (MFVPC_Utils.isNullOrEmpty(recentFileModel.mhtFilePath).booleanValue() || !new File(recentFileModel.mhtFilePath).exists()) {
            AlertDialog create = new AlertDialog.Builder(this).create();
            create.setTitle(getString(R.string.Error));
            create.setMessage(getString(R.string.FileNotFoundOrFileTypeNotSupported));
            create.setIcon(R.drawable.error_white);
            create.setButton(-3, getString(R.string.Confirm), new DialogInterface.OnClickListener() { // from class: com.mhtmhtmlviewer.mhtmhtmlpdfexport.mpv_activity.MPV_FilesSelection.4
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            create.show();
            return;
        }
        String substring = recentFileModel.mhtFilePath.substring(recentFileModel.mhtFilePath.lastIndexOf(".") + 1);
        if (substring.equals("")) {
            Toast.makeText(this.context, "Something went wrong", Toast.LENGTH_SHORT).show();
        } else if (this.extensionList.contains(substring.toLowerCase().toString())) {
            Intent intent = new Intent(this, MFVPC_WebViewActivityNew.class);
            intent.putExtra("recentFileModel", recentFileModel);
            intent.putExtra("KEYWORD", "MPV_HOME");
            startActivityForResult(intent, 555);
            try {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            } catch (Exception unused) {
            }
        } else {
            Toast.makeText(this.context, "File format is not supported", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    public String getPathFromUri(Uri uri, Boolean bool, String name) {
        String str;
        String str2;
        File file = null;
        File file2;
        InputStream inputStream;
        FileOutputStream fileOutputStream;
        String str3;
        String str4 = null;
        int lastIndexOf;
        Cursor cursor;
        String filePathByURI = MFVPC_UtilsFiles.getFilePathByURI(this, uri);
        InputStream inputStream2 = null;

        FileOutputStream fileOutputStream2 = null;
        if (MFVPC_Utils.isNullOrEmpty(filePathByURI).booleanValue()) {
            try {
                if (uri.getScheme().equals("content")) {
                    try {
                        cursor = getContentResolver().query(uri, null, null, null, null);
                        if (cursor != null) {
                            try {
                                if (cursor.moveToFirst()) {
                                    str4 = cursor.getString(cursor.getColumnIndex("_display_name"));
                                }
                            } catch (Throwable th) {

                                if (cursor != null) {
                                }
                                throw th;
                            }
                        }

                    } catch (Throwable th2) {

                        cursor = null;
                        if (cursor != null) {
                            cursor.close();
                        }
                        throw th2;
                    }
                } else {
                    str4 = null;

                }
                if (str4 == null && (lastIndexOf = (str4 = uri.getPath()).lastIndexOf(47)) != -1) {
                    str4 = str4.substring(lastIndexOf + 1);
                }
                str2 = !MFVPC_Utils.isNullOrEmpty(str4).booleanValue() ? MFVPC_UtilsFiles.getFileExtention(new File(str4)) : null;


                try {
                    if (MFVPC_Utils.isNullOrEmpty(str4).booleanValue()) {

                        str = MFVPC_UtilsFiles.getFileNameWithoutExtention(new File(str4));
                    }
                } catch (Exception e) {
                    str3 = str2;
                    e.printStackTrace();
                    str2 = str3;
                    str = null;
                    if (!MFVPC_Utils.isNullOrEmpty(filePathByURI).booleanValue()) {
                    }
                    try {
                        if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
                        }
                    } catch (Exception e2) {
                        file2 = file;
                        e2.printStackTrace();
                        file = file2;
                        if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
                        }
                    }
                    if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
                    }
                }
            } catch (Throwable e3) {
                str3 = null;
                e3.printStackTrace();
                str2 = str3;
                str = null;
                if (!MFVPC_Utils.isNullOrEmpty(filePathByURI).booleanValue()) {
                }
                if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
                }
                if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
                }
            }
            str = null;
        } else {
            str2 = null;
            str = null;
        }
        try {
            file = !MFVPC_Utils.isNullOrEmpty(filePathByURI).booleanValue() ? new File(filePathByURI) : null;
            if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
                str2 = MFVPC_UtilsFiles.getFileExtention(file);
            }
        } catch (Exception e4) {

            file2 = null;
            e4.printStackTrace();
            file = file2;
            if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
            }
        }
        if (MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
            return null;
        }

        String str5;

        try {
            String newname = MFVPC_UtilsFiles.getFileNameWithoutExtention(new File(name));
            Log.e("MyFileNAME"," newname " + newname);
            str5 = getMhtFileDir(bool) + newname + "." + str2;
        } catch (Exception e) {
            Log.e("MyFileNAME"," str " + str);
            str5 = getMhtFileDir(bool) + "file " + System.currentTimeMillis() + "." + str2;
        }


        if (file == null || !file.exists()) {
            try {
                inputStream = getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    try {
                        fileOutputStream = new FileOutputStream(new File(str5));
                    } catch (Exception e5) {

                        fileOutputStream = null;
                        inputStream2 = inputStream;
                        try {
                            e5.printStackTrace();
                            if (inputStream2 != null) {
                            }
                            if (fileOutputStream != null) {
                            }
                            return str5;
                        } catch (Throwable th3) {

                            inputStream = inputStream2;
                            fileOutputStream2 = fileOutputStream;
                            if (inputStream != null) {
                            }
                            if (fileOutputStream2 != null) {
                            }
                            throw th3;
                        }
                    } catch (Throwable th4) {

                        if (inputStream != null) {
                        }
                        if (fileOutputStream2 != null) {
                        }
                        throw th4;
                    }
                    try {
                        byte[] bArr = new byte[4096];
                        while (true) {
                            int read = inputStream.read(bArr);
                            if (read == -1) {
                                break;
                            }
                            fileOutputStream.write(bArr, 0, read);
                        }
                        fileOutputStream.flush();
                    } catch (Exception e6) {

                        inputStream2 = inputStream;
                        e6.printStackTrace();
                        if (inputStream2 != null) {
                        }
                        if (fileOutputStream != null) {
                        }
                        return str5;
                    } catch (Throwable th5) {

                        fileOutputStream2 = fileOutputStream;
                        if (inputStream != null) {
                        }
                        if (fileOutputStream2 != null) {
                        }
                        throw th5;
                    }
                } else {
                    fileOutputStream = null;
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e8) {

                fileOutputStream = null;
                e8.printStackTrace();
                if (inputStream2 != null) {
                    try {
                        inputStream2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return str5;
            } catch (Throwable th6) {

                inputStream = null;
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e9) {
                        e9.printStackTrace();
                        throw th6;
                    }
                }
                if (fileOutputStream2 != null) {
                    try {
                        fileOutputStream2.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                throw th6;
            }
        } else {
            MFVPC_UtilsFiles.copyFile(new File(filePathByURI), new File(str5));
        }

        return str5;
    }

}


