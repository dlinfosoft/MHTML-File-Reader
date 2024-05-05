package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;


import android.content.Context;
import android.os.Build;
import android.os.Environment;

import java.io.File;

public class FolderPathManager {

    public static String getFolderPath(Context context, String folderName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return getFolderPathForQAndAbove(context, folderName);
        } else {
            return getFolderPathForLegacy(folderName);
        }
    }

    private static String getFolderPathForQAndAbove(Context context, String folderName) {
        File folder = new File(context.getExternalFilesDir(null), folderName);
        if (!folder.exists()) {
            if (folder.mkdirs()) {
                // Folder created successfully
            } else {
                // Failed to create the folder
            }
        }

        return folder.getAbsolutePath();
    }

    private static String getFolderPathForLegacy(String folderName) {
        File externalDir = Environment.getExternalStorageDirectory();
        File folder = new File(externalDir, folderName);

        if (!folder.exists()) {
            if (folder.mkdirs()) {
                // Folder created successfully
            } else {
                // Failed to create the folder
            }
        }

        return folder.getAbsolutePath();
    }
}
