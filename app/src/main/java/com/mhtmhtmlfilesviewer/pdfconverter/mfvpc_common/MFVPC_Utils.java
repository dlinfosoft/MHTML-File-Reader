package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import android.database.Cursor;
import android.util.Base64;
import android.widget.EditText;

import com.google.gson.Gson;

import org.apache.http.protocol.HTTP;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class MFVPC_Utils {

    public static <T> T cloneThroughJson(T t) {
        if (t == null) {
            return null;
        }
        Gson gson = new Gson();
        return (T) gson.fromJson(gson.toJson((Object) t), t.getClass());
    }

    public static String decode(String str) {
        char[] cArr = new char[]{'_', '@', '~', '!'};
        char[] cArr2 = new char[]{'A', 'b', 'C', 'V'};
        String str2 = (String) cloneThroughJson(str);
        for (int i = 0; i < cArr2.length; i++) {
            str2 = str2.replace(cArr[i], cArr2[i]);
        }
        return new String(Base64.decode(new StringBuilder(str2).reverse().toString().getBytes(Charset.forName(HTTP.UTF_8)), 0));
    }

    public static Object deepClone(Object obj) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ObjectOutputStream(byteArrayOutputStream).writeObject(obj);
            return new ObjectInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray())).readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Boolean isNullOrEmpty(Cursor cursor) {
        boolean z = true;
        if (cursor == null) {
            return Boolean.valueOf(true);
        }
        if (cursor.getCount() != 0) {
            z = false;
        }
        return Boolean.valueOf(z);
    }

    public static Boolean isNullOrEmpty(EditText editText) {
        Boolean valueOf = Boolean.valueOf(true);
        return (editText == null || isNullOrEmpty(editText.getText().toString()).booleanValue()) ? valueOf : Boolean.valueOf(false);
    }

    public static Boolean isNullOrEmpty(CharSequence charSequence) {
        return charSequence == null ? Boolean.valueOf(true) : isNullOrEmpty(charSequence.toString());
    }

    public static Boolean isNullOrEmpty(String str) {
        Boolean valueOf = Boolean.valueOf(true);
        return (str != null && str.trim().length() >= 1) ? Boolean.valueOf(false) : valueOf;
    }

    public static <T> Boolean isNullOrEmpty(ArrayList<T> arrayList) {
        Boolean valueOf = Boolean.valueOf(true);
        return (arrayList == null || arrayList.isEmpty()) ? valueOf : Boolean.valueOf(false);
    }

    public static <T, T1> Boolean isNullOrEmpty(HashMap<T, T1> hashMap) {
        Boolean valueOf = Boolean.valueOf(true);
        return (hashMap == null || hashMap.isEmpty()) ? valueOf : Boolean.valueOf(false);
    }

    public static <T> Boolean isNullOrEmpty(HashSet<T> hashSet) {
        Boolean valueOf = Boolean.valueOf(true);
        return (hashSet == null || hashSet.isEmpty()) ? valueOf : Boolean.valueOf(false);
    }

    public static <T> Boolean isNullOrEmpty(List<T> list) {
        Boolean valueOf = Boolean.valueOf(true);
        return (list == null || list.isEmpty()) ? valueOf : Boolean.valueOf(false);
    }

    public static <T> Boolean isNullOrEmpty(T[] tArr) {
        Boolean valueOf = Boolean.valueOf(true);
        return (tArr != null && tArr.length >= 1) ? Boolean.valueOf(false) : valueOf;
    }







}
