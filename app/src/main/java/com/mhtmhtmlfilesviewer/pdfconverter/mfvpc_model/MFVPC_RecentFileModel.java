package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;


import com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common.MFVPC_Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class MFVPC_RecentFileModel implements Parcelable {
    public static final Creator<MFVPC_RecentFileModel> CREATOR = new Creator<MFVPC_RecentFileModel>() {
        public MFVPC_RecentFileModel createFromParcel(Parcel parcel) {
            MFVPC_RecentFileModel recentFileModel = new MFVPC_RecentFileModel();
            Bundle readBundle = parcel.readBundle();
            recentFileModel.pathOfHtmlFileToLoad = readBundle.getString("pathOfHtmlFileToLoad");
            recentFileModel.dirPath = readBundle.getString("dirPath");
            recentFileModel.mhtFileName = readBundle.getString("mhtFileName");
            recentFileModel.mhtFilePath = readBundle.getString("mhtFilePath");
            recentFileModel.origUriStr = readBundle.getString("origUriStr");
            recentFileModel.errorStr = readBundle.getString("errorStr");
            recentFileModel.fileName = readBundle.getString("fileName");
            recentFileModel.todayStr = readBundle.getString("todayStr");
            recentFileModel.loadMhtToWebView = Boolean.valueOf(readBundle.getBoolean("loadMhtToWebView"));
            recentFileModel.timeStamp = readBundle.getLong("timeStamp");
            return recentFileModel;
        }

        public MFVPC_RecentFileModel[] newArray(int i) {
            return new MFVPC_RecentFileModel[i];
        }
    };
    public String dirPath;
    public String errorStr;
    public String fileName;
    public Boolean loadMhtToWebView = Boolean.valueOf(false);
    public String mhtFileName;
    public String mhtFilePath;
    public String origUriStr;
    public String pathOfHtmlFileToLoad;
    public long timeStamp;
    public String todayStr;

    public static int getInexByFileName(ArrayList<MFVPC_RecentFileModel> arrayList, String str) {
        if (!(MFVPC_Utils.isNullOrEmpty((ArrayList) arrayList).booleanValue() || MFVPC_Utils.isNullOrEmpty(str).booleanValue())) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                MFVPC_RecentFileModel recentFileModel = (MFVPC_RecentFileModel) it.next();
                if (recentFileModel.fileName.equals(str)) {
                    return arrayList.indexOf(recentFileModel);
                }
            }
        }
        return -1;
    }

    public static int getInexByFileNameAndDate(ArrayList<MFVPC_RecentFileModel> arrayList, MFVPC_RecentFileModel recentFileModel) {
        if (!(MFVPC_Utils.isNullOrEmpty((ArrayList) arrayList).booleanValue() || recentFileModel == null)) {
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                MFVPC_RecentFileModel recentFileModel2 = (MFVPC_RecentFileModel) it.next();
                if (recentFileModel2.fileName.equals(recentFileModel.fileName) && recentFileModel2.todayStr.equals(recentFileModel.todayStr)) {
                    return arrayList.indexOf(recentFileModel2);
                }
            }
        }
        return -1;
    }

    public static ArrayList<Date> getLstOfCals(ArrayList<MFVPC_RecentFileModel> arrayList) {
        ArrayList arrayList2 = new ArrayList();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            MFVPC_RecentFileModel recentFileModel = (MFVPC_RecentFileModel) it.next();
            if (!MFVPC_Utils.isNullOrEmpty(recentFileModel.todayStr).booleanValue()) {
                Date date = new Date(recentFileModel.timeStamp);
                Boolean valueOf = Boolean.valueOf(true);
                Iterator it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    if (simpleDateFormat.format((Date) it2.next()).equals(simpleDateFormat.format(date))) {
                        valueOf = Boolean.valueOf(false);
                        break;
                    }
                }
                if (valueOf.booleanValue()) {
                    arrayList2.add(date);
                }
            }
        }
        return arrayList2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("pathOfHtmlFileToLoad", this.pathOfHtmlFileToLoad);
        bundle.putString("dirPath", this.dirPath);
        bundle.putString("mhtFileName", this.mhtFileName);
        bundle.putString("mhtFilePath", this.mhtFilePath);
        bundle.putString("origUriStr", this.origUriStr);
        bundle.putString("errorStr", this.errorStr);
        bundle.putString("fileName", this.fileName);
        bundle.putString("todayStr", this.todayStr);
        bundle.putBoolean("loadMhtToWebView", this.loadMhtToWebView.booleanValue());
        bundle.putLong("timeStamp", this.timeStamp);
        parcel.writeBundle(bundle);
    }
}
