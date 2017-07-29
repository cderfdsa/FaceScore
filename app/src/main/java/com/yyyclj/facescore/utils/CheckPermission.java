package com.yyyclj.facescore.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Created by 姚尧 on 2017/6/9.
 */

public class CheckPermission {
    private static String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static void verifyBLEPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                //检测是否有权限
                for (int i = 0; i < permissions.length; i++) {
                    if (ActivityCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(activity, new String[]{permissions[i]}, 1);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
