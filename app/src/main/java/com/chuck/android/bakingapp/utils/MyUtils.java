package com.chuck.android.bakingapp.utils;

import android.webkit.MimeTypeMap;

public class MyUtils {
    // check mimetype from stack overflow - https://stackoverflow.com/questions/8589645/how-to-determine-mime-type-of-file-in-android
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }
}
