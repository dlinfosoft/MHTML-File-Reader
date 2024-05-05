package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;

import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mhtmhtmlfilesviewer.pdfconverter.R;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_adapters.MFVPC_RecentAdapter;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Sharefilepath;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UriDeserializer;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Utils;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UtilsFiles;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_RecentFileModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MFVPC_RecentActivity extends AppCompatActivity {
    String TAG = "MFVPC_RecentActivity ";
    RecyclerView listview;
    ImageView iv_back;
    MFVPC_RecentAdapter adapter;
    ProgressBar progressBar;
    Context context;
    LinearLayout emptyState;

    String errorStr;
    Boolean D;
    Uri uri;
    SharedPreferences sharedPreferences;
    Boolean x;
    Boolean y;
    ImageView delete;

    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    void loadGoogleBannerads() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



//    private InterstitialAd mInterstitialAd;
//    void loadinterstitialads(){
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_id));
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//    }
//    void InterstitialListener(){
//        mInterstitialAd.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(LoadAdError adError) {
//                // Code to be executed when an ad request fails.
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when the ad is displayed.
//            }
//
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the interstitial ad is closed.
//                Intent intent = new Intent(MFVPC_RecentActivity.this, MFVPC_MainActivity.class);
//                startActivity(intent);
//                finish();
//                try {
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//                }catch ( Exception e){
//
//                }
//                mInterstitialAd.loadAd(new AdRequest.Builder().build());
//            }
//        });
//    }


    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(
                this,
                getResources().getString(R.string.interstitial_id),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MFVPC_RecentActivity.this.mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MFVPC_RecentActivity.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");

                                        Intent intent = new Intent( MFVPC_RecentActivity.this, MFVPC_MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        try {
                                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                        }catch ( Exception e){

                                        }
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MFVPC_RecentActivity.this.mInterstitialAd = null;
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




    /** Called when leaving the activity */
    @Override
    public void onPause() {
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        new LoadRecentFilesData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        new RemoveOldFiles().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
        super.onResume();

    }


    @Override
    protected void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mfvpc_recent);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarcl));
        }

        context = this;

        findbyId();
        eventClick();
        initData();


        loadGoogleBannerads();
        loadAd();

    }



    private class RemoveOldFiles extends AsyncTask<String, String, String> {

        protected String doInBackground(String... strArr) {
            MFVPC_UtilsFiles.removeDirContentsOlderThanXDays(new File(getMhtFileDir()), 30);
            MFVPC_UtilsFiles.removeDirContentsOlderThanXDays(new File(getMhtRecentFilesDir()), 30);
            return null;
        }
    }


    private class LoadRecentFilesData extends AsyncTask<String, String, ArrayList<MFVPC_RecentFileModel>> {

        protected ArrayList<MFVPC_RecentFileModel> doInBackground(String... strArr) {
            Log.i("MFVPC_RecentAdapter", " doInBackground ");

            ArrayList arrayList = new ArrayList();
            GsonBuilder registerTypeAdapter = new GsonBuilder().registerTypeAdapter(Uri.class, new MFVPC_UriDeserializer());
            File[] b = getFilesFromRecentsDir();


            if (!MFVPC_Utils.isNullOrEmpty(b).booleanValue()) {
                ArrayList arrayList2 = new ArrayList(Arrays.asList(b));
                Collections.sort(arrayList2, new Comparator<File>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public int compare(File file, File file2) {
                        return Long.compare(file.lastModified(), file2.lastModified());
                    }
                });
                Iterator it = arrayList2.iterator();
                while (it.hasNext()) {
                    File file = (File) it.next();
                    if (file.exists()) {
                        Object obj = null;
                        String gsonStrFromFile = MFVPC_UtilsFiles.getGsonStrFromFile(file);
                        if (!MFVPC_Utils.isNullOrEmpty(gsonStrFromFile).booleanValue()) {
                            obj = (MFVPC_RecentFileModel) registerTypeAdapter.create().fromJson(gsonStrFromFile, new TypeToken<MFVPC_RecentFileModel>() {
                            }.getType());
                        }

                        if (obj != null) {
                            Log.i("LoadFilesActivity123", " doInBackground obj  " + obj);
                            arrayList.add(obj);
                        }
                    }
                }
            }
            if (!MFVPC_Utils.isNullOrEmpty(arrayList).booleanValue()) {
                Collections.reverse(arrayList);
            }
            return arrayList;
        }

        /* renamed from: progressDialog */
        protected void onPostExecute(ArrayList<MFVPC_RecentFileModel> arrayList) {
            super.onPostExecute(arrayList);

            if (arrayList.size() == 0) {
                emptyState.setVisibility(View.VISIBLE);
                listview.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                delete.setVisibility(View.INVISIBLE);
            } else {
                try {
                    emptyState.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
//                    delete.setVisibility(View.VISIBLE);
                    setRecentsAdapter(arrayList);
                } catch (Exception e) {
                    Log.i("MFVPC_RecentAdapter", " e " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("MFVPC_RecentAdapter", " onPreExecute ");

            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void setRecentsAdapter(ArrayList<MFVPC_RecentFileModel> arrayList) {
        if (MFVPC_Utils.isNullOrEmpty(arrayList).booleanValue()) {
            arrayList = new ArrayList();
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        listview.setLayoutManager(layoutManager);
        adapter = new MFVPC_RecentAdapter(context, arrayList, sharefilepath);
        listview.setAdapter(adapter);
    }

    MFVPC_Sharefilepath sharefilepath = new MFVPC_Sharefilepath() {
        @Override
        public void shareItem(File path) {
            deletefile(MFVPC_RecentActivity.this, path);
        }

        @Override
        public void RecentFilemodel(MFVPC_RecentFileModel path) {
            if (path != null) {
                recentfileUpdate(path);
            }
        }

        @Override
        public void shareItem(String name, String date, String size, File path) {
            try {
                MFVPC_WebViewActivityNew.filestr=name;

            }catch ( Exception e){

            }
        }


    };


    private File[] getFilesFromRecentsDir() {
        return new File(getMhtRecentFilesDir()).listFiles();
    }

    private String getMhtRecentFilesDir() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getFilesDir());
        stringBuilder.append(File.separator);
        stringBuilder.append("MHTRecentsDir");
        stringBuilder.append(File.separator);
        String stringBuilder2 = stringBuilder.toString();
        File file = new File(stringBuilder2);


        if (!file.exists()) {
            file.mkdir();
        }
        return stringBuilder2;
    }


    public void deletefile(final Context context, final File file) {
        class deletedata extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(Boolean aVoid) {
                new LoadRecentFilesData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                super.onPostExecute(aVoid);
            }
            @Override
            protected Boolean doInBackground(Void... voids) {

             try {

                 File[] files = getFilesFromRecentsDir();
                 for (int i = 0; i < files.length; i++) {
                     String newname = MFVPC_UtilsFiles.getFileNameWithoutExtention(files[i].getAbsoluteFile());
                     if (file.getAbsolutePath().contains(newname)) {

                         if (files[i].exists()) {
                             if (files[i].delete()) {
                                 Log.d("Files123456", " delete 2 " );
                             } else {
                                 Log.d("Files123456", " delete not 2 " );
                             }
                         } else {
                             Log.d("Files123456", "exist not:");
                         }


                     }
                 }



                 if (file.exists()) {
                     if (file.delete()) {
                         Log.d("Files123456", " delete 1 " );
                     } else {
                         Log.d("Files123456", " delete not 1 " );
                     }
                 } else {
                     Log.d("Files123456", "exist not:");
                 }

             }catch ( Exception e){

             }


                return null;
            }
        }
        new deletedata().execute();
    }


    public void findbyId() {
        listview = (RecyclerView) findViewById(R.id.recentlist);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);
        emptyState = (LinearLayout) findViewById(R.id.tv_msg);
        delete = (ImageView) findViewById(R.id.delete_all);
    }

    public void eventClick() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDailogeMsg(MFVPC_RecentActivity.this);
            }
        });
    }


    private void ShowDailogeMsg(Context context) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.mfvpc_dialog_view);

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
                MFVPC_UtilsFiles.removeDir(new File(getMhtFileDir()));
                MFVPC_UtilsFiles.removeDir(new File(getMhtRecentFilesDir()));
                setRecentsAdapter(null);
                new LoadRecentFilesData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);


            }
        });

    }





    @Override
    public void onBackPressed() {

        if (mInterstitialAd != null) {
            mInterstitialAd.show(MFVPC_RecentActivity.this);
        } else {
            Intent intent = new Intent(this, MFVPC_MainActivity.class);
            startActivity(intent);
            finish();
            try {
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }catch ( Exception e){

            }
        }


    }
    private String getMhtFileDir() {
        return getMhtFileDir(Boolean.valueOf(false));
    }


    public MFVPC_RecentActivity() {
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

    private void loadFileToWebView(MFVPC_RecentFileModel recentFileModel) {
        if (MFVPC_Utils.isNullOrEmpty(recentFileModel.mhtFilePath).booleanValue() || !new File(recentFileModel.mhtFilePath).exists()) {
            AlertDialog create = new AlertDialog.Builder(this).create();
            create.setTitle(getString(R.string.Error));
            create.setMessage(getString(R.string.FileNotFoundOrFileTypeNotSupported));
            create.setIcon((int) R.drawable.error_white);
            create.setButton(-3, getString(R.string.Confirm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            create.show();
            return;
        }

        Intent intent = new Intent(this, MFVPC_WebViewActivityNew.class);
        intent.putExtra("recentFileModel", recentFileModel);
        intent.putExtra("KEYWORD", "MFVPC_RecentActivity");
        startActivityForResult(intent, 555);


    }

    public void recentfileUpdate(final MFVPC_RecentFileModel recentFileModel){
        class recentfileUpdate extends AsyncTask<String, String, MFVPC_RecentFileModel> {
            ProgressDialog progressDialog;


            @Override
            protected MFVPC_RecentFileModel doInBackground(String... strings) {
                long currentTimeMillis = System.currentTimeMillis();
                String valueOf = String.valueOf(currentTimeMillis);
                MFVPC_RecentFileModel recentFileModel3 = new MFVPC_RecentFileModel();

                recentFileModel3.mhtFileName = recentFileModel.mhtFileName;

                recentFileModel3.timeStamp = currentTimeMillis;
                recentFileModel3.dirPath = getMhtContentsDir(valueOf);
                recentFileModel3.origUriStr = recentFileModel.origUriStr;
                recentFileModel3.mhtFilePath = recentFileModel.mhtFilePath;




                GsonBuilder registerTypeAdapter = new GsonBuilder().registerTypeAdapter(Uri.class, new MFVPC_UriDeserializer());

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


                    if(new File(recentFileModel.dirPath).exists()) {
                        File file = new File(recentFileModel.dirPath);


                        File[] files = getFilesFromRecentsDir();
                        for (int i = 0; i < files.length; i++) {
                            String newname = MFVPC_UtilsFiles.getFileNameWithoutExtention(files[i].getAbsoluteFile());
                            if (file.getAbsolutePath().contains(newname)) {

                                if (files[i].exists()) {
                                    if (files[i].delete()) {
                                        Log.d("recentfileUpdate", " delete json " + files[i].getAbsolutePath());
                                    } else {
                                        Log.d("recentfileUpdate", " delete not 2 ");
                                    }
                                } else {
                                    Log.d("recentfileUpdate", "exist not:");
                                }


                            }
                        }
                        if (file.exists()) {
                            if (file.delete()) {
                                Log.d("recentfileUpdate", " delete dirPath " + file.getAbsolutePath());
                            } else {
                                Log.d("recentfileUpdate", " delete not 1 ");
                            }
                        } else {
                            Log.d("recentfileUpdate", "exist not:");
                        }
                    }

                }catch ( Exception e){

                }

                return recentFileModel3;
            }

            @Override
            protected void onPreExecute() {
                progressDialog = new ProgressDialog(MFVPC_RecentActivity.this);
                progressDialog.setTitle(getString(R.string.LoadingFile));
                progressDialog.setMessage(getString(R.string.Working));
                progressDialog.setCancelable(false);
                progressDialog.setIndeterminate(true);
                try {
                    progressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(MFVPC_RecentFileModel aVoid) {

                loadFileToWebView(aVoid);

                ProgressDialog progressDialog = this.progressDialog;
                if (progressDialog != null) {
                    try {
                        progressDialog.dismiss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                super.onPostExecute(aVoid);
            }

        }

        new recentfileUpdate().execute();

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





}
