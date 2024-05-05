package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.os.EnvironmentCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mhtmhtmlfilesviewer.pdfconverter.R;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_adapters.MFVPC_MhtFilesAdapter;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.FolderPathManager;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_MHTParser;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_MHTUnpack;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Sharefilepath;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UriDeserializer;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Utils;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_UtilsFiles;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_Attachment;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_MHTModel;
import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model.MFVPC_RecentFileModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.mail.MessagingException;

public class MFVPC_FilesSelection extends AppCompatActivity implements SearchView.OnQueryTextListener {
    RecyclerView mhtListrecycle;
    ImageView back, refresh_btn;
    MFVPC_MhtFilesAdapter mhtFilesAdapter;
    String TAG = "MFVPC_FilesSelection ";
    MFVPC_MHTModel pdfModel = null;
    Long uncompressedFileLength;
    String uncompressedFileSize;
    ProgressBar pBar;
    LinearLayout tv_msg;
    File file;
    Context context;
    SearchView searchView;
    ArrayList<MFVPC_MHTModel> newlist;

    RecyclerView.LayoutManager layoutManager;
    static ArrayList<MFVPC_MHTModel> mhtList;
    TextView count;


    String errorStr;
    Boolean D;
    Uri uri;
    SharedPreferences sharedPreferences;
    Boolean x;
    Boolean y;
    LinearLayout fab;
    ArrayList<String> extensionList = new ArrayList<>();




    private InterstitialAd mInterstitialAd;
    private AdView mAdView;

    void loadGoogleBannerads() {
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }



//     InterstitialAd mInterstitialAd;
//
//    void loadinterstitialads() {
//        mInterstitialAd = new InterstitialAd(this);
//        mInterstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_id));
//        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//    }
//
//    void InterstitialListener() {
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
//
//                Intent intent = new Intent("android.intent.action.GET_CONTENT");
//                intent.setType("*/*");
//                startActivityForResult(intent, 4444);
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
                        MFVPC_FilesSelection.this.mInterstitialAd = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MFVPC_FilesSelection.this.mInterstitialAd = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                        Intent intent = new Intent("android.intent.action.GET_CONTENT");
                                        intent.setType("*/*");
                                        startActivityForResult(intent, 4444);

                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        // Make sure to set your reference to null so you don't
                                        // show it a second time.
                                        MFVPC_FilesSelection.this.mInterstitialAd = null;
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
        if (mAdView != null) {
            mAdView.destroy();
        }

        super.onDestroy();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mfvpc_activity_mhtlisting);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.statusbarcl));
        }
        context = this;
        loadGoogleBannerads();
        loadAd();



        //////////////////pdf default//////
        Intent intent=getIntent();
        try {
            if(intent!=null) {
                String action = intent.getAction();
                if (Intent.ACTION_VIEW.equals(action)) {
                    // Get the file from the intent object
                    uri = intent.getData();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());
                    new OpenMhtFile("File"+currentDateandTime).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);
                }
            }
        }catch ( Exception e){
            Log.i(TAG, "Exception ::::::::::" + e.getMessage());
        }

        find();
        initData();

        new FetchMHTdata().execute();

    }


    public String[] getExternalStorageDirectories() {

        List<String> results = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { //Method 1 for KitKat & above
            File[] externalDirs = getExternalFilesDirs(null);
            String internalRoot = Environment.getExternalStorageDirectory().getAbsolutePath().toLowerCase();

            for (File file : externalDirs) {
                if (file == null) //solved NPE on some Lollipop devices
                    continue;
                String path = file.getPath().split("/Android")[0];

                if (path.toLowerCase().startsWith(internalRoot))
                    continue;

                boolean addPath = false;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    addPath = Environment.isExternalStorageRemovable(file);
                } else {
                    addPath = Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(file));
                }

                if (addPath) {
                    results.add(path);
                }
            }
        }

        if (results.isEmpty()) { //Method 2 for all versions
            // better variation of: http://stackoverflow.com/a/40123073/5002496
            String output = "";
            try {
                final Process process = new ProcessBuilder().command("mount | grep /dev/block/vold")
                        .redirectErrorStream(true).start();
                process.waitFor();
                final InputStream is = process.getInputStream();
                final byte[] buffer = new byte[1024];
                while (is.read(buffer) != -1) {
                    output = output + new String(buffer);
                }
                is.close();
            } catch (final Exception e) {
                e.printStackTrace();
            }
            if (!output.trim().isEmpty()) {
                String devicePoints[] = output.split("\n");
                for (String voldPoint : devicePoints) {
                    results.add(voldPoint.split(" ")[2]);
                }
            }
        }

        //Below few lines is to remove paths which may not be external memory card, like OTG (feel free to comment them out)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().matches(".*[0-9a-f]{4}[-][0-9a-f]{4}")) {
                    Log.d("123456", results.get(i) + " might not be extSDcard");
                    results.remove(i--);
                }
            }
        } else {
            for (int i = 0; i < results.size(); i++) {
                if (!results.get(i).toLowerCase().contains("ext") && !results.get(i).toLowerCase().contains("sdcard")) {
                    Log.d("123456", results.get(i) + " might not be extSDcard");
                    results.remove(i--);
                }
            }
        }

        String[] storageDirectories = new String[results.size()];
        for (int i = 0; i < results.size(); ++i) storageDirectories[i] = results.get(i);

        return storageDirectories;
    }


    public void find() {
        file = new File( FolderPathManager.getFolderPath(this, getResources().getString(R.string.foldername)));
        mhtListrecycle = (RecyclerView) findViewById(R.id.listview);
        back = (ImageView) findViewById(R.id.back_button);

        refresh_btn = (ImageView) findViewById(R.id.refresh_button);

        fab = (LinearLayout) findViewById(R.id.fab);

        tv_msg = (LinearLayout) findViewById(R.id.tv_msg);
        pBar = (ProgressBar) findViewById(R.id.progressbar);
        searchView = (SearchView) findViewById(R.id.search);
        count = (TextView) findViewById(R.id.count);
        searchView.setIconified(false);
        searchView.setIconifiedByDefault(false);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(this);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        refresh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new FetchMHTdata().execute();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mInterstitialAd != null) {
                    mInterstitialAd.show(MFVPC_FilesSelection.this);
                } else {
                    Intent intent = new Intent("android.intent.action.GET_CONTENT");
                    intent.setType("*/*");
                    startActivityForResult(intent, 4444);
                }


                }
        });

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

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        newText = newText.toLowerCase();
        newlist = new ArrayList<>();
        for (MFVPC_MHTModel pdfModel : mhtList) {
            String filename = pdfModel.getPdfname().toLowerCase();
            String fileSize = pdfModel.getPdfname().toLowerCase();
            String filedate = pdfModel.getPdfname().toLowerCase();

            if (filename.contains(newText) || fileSize.contains(newText) || filedate.contains(newText)) {
                newlist.add(pdfModel);
            }
        }
        if (newlist.size() == 0) {
            tv_msg.setVisibility(View.VISIBLE);

            mhtListrecycle.setVisibility(View.GONE);
        } else {
            tv_msg.setVisibility(View.GONE);
            mhtListrecycle.setVisibility(View.VISIBLE);
        }
        mhtFilesAdapter.filterList(newlist);
        return true;
    }

    public class FetchMHTdata extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            mhtList = new ArrayList<>();
            searchView.setVisibility(View.GONE);
            pBar.setVisibility(View.VISIBLE);
            mhtListrecycle.setVisibility(View.GONE);
            tv_msg.setVisibility(View.GONE);

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {

            pBar.setVisibility(View.GONE);
//          searchView.setVisibility(View.VISIBLE);

            if (mhtList.size() == 0) {
                tv_msg.setVisibility(View.VISIBLE);
                mhtListrecycle.setVisibility(View.GONE);

            } else {
                tv_msg.setVisibility(View.GONE);
                mhtListrecycle.setVisibility(View.VISIBLE);
                layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                mhtListrecycle.setLayoutManager(layoutManager);

                mhtFilesAdapter = new MFVPC_MhtFilesAdapter(context, mhtList, new MFVPC_Sharefilepath() {
                    @Override
                    public void shareItem(File path) {

                    }

                    @Override
                    public void RecentFilemodel(MFVPC_RecentFileModel path) {

                    }

                    @Override
                    public void shareItem(String name, String date, String size, File path) {
                        uri = Uri.fromFile(path);


                        try {
                            String newname = MFVPC_UtilsFiles.getFileNameWithoutExtention(new File(name));
                            MFVPC_WebViewActivityNew.filestr = newname;
                        } catch (Exception e) {

                        }


                        new OpenMhtFile(name).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new String[0]);


                    }

                });
                mhtListrecycle.setAdapter(mhtFilesAdapter);

            }


            super.onPostExecute(aVoid);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            try {
                File directory;
                String[] allPath;
                allPath = getExternalStorageDirectories();
                for (String path : allPath) {
                    directory = new File(path);
                    getfile(directory);
                }
                getfile(file);
            } catch (Exception e) {
                getfile(file);
            }


            return null;
        }
    }




    public ArrayList<MFVPC_MHTModel> getfile(File dir) {
        File listFile[] = dir.listFiles();
        Log.e("ExternalStorage", "dir ::" + dir.length());

        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i]);

                } else {
                    pdfModel = new MFVPC_MHTModel();
                    if (listFile[i].getName().endsWith(".mht") || listFile[i].getName().endsWith(".mhtml")|| listFile[i].getName().endsWith(".eml")) {

                        Log.i(TAG, "listFile[i].getName()" + listFile[i].getName());
                        Log.i(TAG, "listFile[i] else" + listFile[i]);
                        pdfModel.setPdffile(listFile[i]);
                        pdfModel.setPdfname(listFile[i].getName());
                        Date lastModDate = new Date(listFile[i].lastModified());
                        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
                        String formattedDateString = formatter.format(lastModDate);
                        Log.e(TAG, "formattedDateString" + formattedDateString);
                        pdfModel.setDate(formattedDateString);
                        pdfModel.setDatemodify(lastModDate);
                        uncompressedFileLength = Long.valueOf(listFile[i].length());
                        uncompressedFileSize = Formatter.formatShortFileSize(context, uncompressedFileLength.longValue());
                        pdfModel.setSize(uncompressedFileSize);
                        mhtList.add(pdfModel);
                    }
                }
            }
        }
        return mhtList;
    }





    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MFVPC_MainActivity.class);
//        startActivity(new Intent(this, MFVPC_MainActivity.class));

        startActivity(intent);
        try {
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        } catch (Exception e) {

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
            progressDialog = new ProgressDialog(MFVPC_FilesSelection.this);
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

    public MFVPC_FilesSelection() {
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
