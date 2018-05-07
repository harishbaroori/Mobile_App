package com.assessment.sdcard.scan.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by harish on 05/02/2018.
 */
public class RunTimePermissionsHandler {

    public static final int PERMISSION_REQUEST_CODE = 1;

    public static void handleReadStoragePermission(Context context){

        if (Build.VERSION.SDK_INT >= 23) {
            if (!checkPermission(context)) {
                requestPermission(context);
            }
        }

    }

    private static boolean checkPermission(Context context) {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private static void requestPermission(Context context) {
        ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                PERMISSION_REQUEST_CODE);
    }
}
