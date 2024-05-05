package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class MFVPC_UtilsPrefs {
    @SuppressLint({"ApplySharedPref"})
    public static void addObjectToPrefs(Object obj, String str, SharedPreferences sharedPreferences) {
        if (obj != null && sharedPreferences != null) {
            Editor edit = sharedPreferences.edit();
            if (obj instanceof String) {
                String str2 = (String) obj;
                if (!MFVPC_Utils.isNullOrEmpty(str2).booleanValue()) {
                    edit.putString(str, str2);
                }
            } else if (obj instanceof Integer) {
                edit.putInt(str, ((Integer) obj).intValue());
            } else if (obj instanceof Long) {
                edit.putLong(str, ((Long) obj).longValue());
            } else if (obj instanceof Float) {
                edit.putFloat(str, ((Float) obj).floatValue());
            } else {
                if (obj instanceof Boolean) {
                    edit.putBoolean(str, ((Boolean) obj).booleanValue());
                }
            }
            edit.commit();
        }
    }

}
