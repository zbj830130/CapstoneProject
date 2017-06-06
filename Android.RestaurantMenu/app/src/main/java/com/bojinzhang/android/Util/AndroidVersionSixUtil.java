package com.bojinzhang.android.Util;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by zhangbojin on 24/05/17.
 */

public class AndroidVersionSixUtil {
    public static void SDCardOpertaion(Activity activity){
        ActivityCompat.requestPermissions(activity, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE}
                , 1);
    }
}
