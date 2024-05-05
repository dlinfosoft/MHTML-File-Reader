package com.mhtmhtmlfilesviewer.pdfconverter.mfvpc_common;

import android.net.Uri;
import android.os.Build.VERSION;

import androidx.core.content.FileProvider;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.io.File;
import java.lang.reflect.Type;

public class MFVPC_UriDeserializer implements JsonDeserializer<Uri> {
    public Uri deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) {
        if (VERSION.SDK_INT <= 19) {
            return Uri.parse(jsonElement.getAsString());
        }
        MFVPC_App GetApp = MFVPC_App.GetApp();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(MFVPC_App.GetApp().getPackageName());
        stringBuilder.append(".provider");
        return FileProvider.getUriForFile(GetApp, stringBuilder.toString(), new File(jsonElement.getAsString()));
    }
}
