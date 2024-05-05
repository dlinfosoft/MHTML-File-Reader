package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.mhtmhtmlfilesviewer.pdfconverter.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;


public class MFVPC_UtilsFiles {

    public static String getFileExtention(File file) {
        if (file == null) {
            return null;
        }
        String[] split = file.getName().split("\\.");
        return split[split.length - 1];
    }

    public static String getFileNameWithExtention(File file) {
        return file == null ? null : file.getName();
    }

    public static String getFileNameWithoutExtention(File file) {
        String fileNameWithExtention = getFileNameWithExtention(file);
        if (MFVPC_Utils.isNullOrEmpty(fileNameWithExtention).booleanValue()) {
            return null;
        }
        int lastIndexOf = fileNameWithExtention.lastIndexOf(".");
        return lastIndexOf == -1 ? fileNameWithExtention : fileNameWithExtention.substring(0, lastIndexOf);
    }

    public static String getFilePathByURI(Activity activity, Uri uri) {
        String str = "_data";
        try {
            Cursor managedQuery = activity.managedQuery(uri, new String[]{str}, null, null, null);
            activity.startManagingCursor(managedQuery);
            int columnIndexOrThrow = managedQuery.getColumnIndexOrThrow(str);
            managedQuery.moveToFirst();
            return columnIndexOrThrow != -1 ? managedQuery.getString(columnIndexOrThrow) : null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getGsonStrFromFile(File file) {
        String str = null;
        if (!file.exists()) {
            return null;
        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)), new char[8064].length);
            StringBuilder stringBuilder = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine == null) {
                    break;
                }
                stringBuilder.append(readLine);
            }
            str = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static ArrayList<File> getLstOfFilesInDir(File file) {
        ArrayList<File> arrayList = null;
        if (file == null) {
            return null;
        }
        if (file.exists() && file.isDirectory()) {
            arrayList = new ArrayList(Arrays.asList(file.listFiles()));
        }
        return arrayList;
    }


    public static Boolean isFileOlderThanXDays(File file, int i) {
        Boolean valueOf = Boolean.valueOf(false);
        if (file == null) {
            return valueOf;
        }
        return MFVPC_UtilsCalendar.getDifferenceInDays(new Date(), new Date(file.lastModified())) > i ? Boolean.valueOf(true) : valueOf;
    }


    public static void removeDir(File file) {
        removeDir(file, -1);
    }

    public static void removeDir(File file, int i) {
        if (file != null) {
            File[] listFiles = file.listFiles();
            if (file.isDirectory() && !MFVPC_Utils.isNullOrEmpty(listFiles).booleanValue()) {
                for (File file2 : listFiles) {
                    Log.i("removeDir", file2.getAbsolutePath());
                    removeDir(file2, i);
                }
            }
            if (i == -1 || isFileOlderThanXDays(file, i).booleanValue()) {

                Log.i("removeDir:", file.getAbsolutePath());

                file.delete();
            }
        }
    }


    public static void removeDirContentsOlderThanXDays(File file, int i) {
        if (file != null && file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (MFVPC_Utils.isNullOrEmpty(listFiles).booleanValue()) {
                if (isFileOlderThanXDays(file, i).booleanValue()) {
                    removeFile(file);
                }
                return;
            }
            for (File file2 : listFiles) {
                if (file2.isDirectory()) {
                    removeDirContentsOlderThanXDays(file2, i);
                } else if (isFileOlderThanXDays(file2, i).booleanValue()) {
                    removeFile(file2);
                }
            }
        }
    }

    public static void removeFile(File file) {
        if (file != null) {
            if (file.exists()) {
                file.delete();
            }
            Log.i("removeFile file: ", file.toString());
        }
    }


    public static void copyFile(File file, File file2) {
        FileInputStream fileInputStream;
        FileOutputStream fileOutputStream = null;
        if (file != null && file.exists()) {
            FileOutputStream fileOutputStream2 = null;
            try {
                fileInputStream = new FileInputStream(file);
                try {
                    fileOutputStream = new FileOutputStream(file2);
                } catch (Exception e) {
                    e = e;
                    try {
                        e.printStackTrace();
                        fileOutputStream2.close();
                        fileInputStream.close();
                    } catch (Throwable th) {
                        th = th;
                        try {
                            fileOutputStream2.close();
                            fileInputStream.close();
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                        throw th;
                    }
                }
                try {
                    byte[] bArr = new byte[1024];
                    while (true) {
                        int read = fileInputStream.read(bArr);
                        if (read > 0) {
                            fileOutputStream.write(bArr, 0, read);
                        } else {
                            try {
                                fileOutputStream.close();
                                fileInputStream.close();
                                return;
                            } catch (Exception e3) {
                                e3.printStackTrace();
                                return;
                            }
                        }
                    }
                } catch (Exception e4) {

                    fileOutputStream2 = fileOutputStream;
                    e4.printStackTrace();
                    fileOutputStream2.close();
                    fileInputStream.close();
                } catch (Throwable th2) {

                    fileOutputStream2 = fileOutputStream;
                    fileOutputStream2.close();
                    fileInputStream.close();
                    throw th2;
                }
            } catch (Exception e5) {
                fileInputStream = null;
                e5.printStackTrace();
                try {
                    fileOutputStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Throwable th3) {
                fileInputStream = null;
                try {
                    fileOutputStream2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    throw th3;
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }
    }


    @SuppressLint("WrongConstant")
    public static void openFileByPath(Activity activity, File file) {
        String str = "";
        if (file != null) {
            try {
                MimeTypeMap singleton = MimeTypeMap.getSingleton();
                String fileExtensionFromUrl = MimeTypeMap.getFileExtensionFromUrl(file.getName());
                if (MFVPC_Utils.isNullOrEmpty(fileExtensionFromUrl).booleanValue()) {
                    fileExtensionFromUrl = getFileExtention(file);
                }
                String mimeTypeFromExtension = singleton.getMimeTypeFromExtension(fileExtensionFromUrl);
                if (mimeTypeFromExtension == null) {
                    mimeTypeFromExtension = "*/*";
                }
                if (file.exists()) {
                    Log.d(str, str);
                }
                Intent intent = new Intent("android.intent.action.VIEW");
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(activity.getApplicationContext().getPackageName());
                stringBuilder.append(".provider");
                intent.setDataAndType(FileProvider.getUriForFile(activity, stringBuilder.toString(), file), mimeTypeFromExtension);
                intent.setFlags(1);
                activity.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(activity, activity.getString(R.string.NoAppFoundForFileType), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
