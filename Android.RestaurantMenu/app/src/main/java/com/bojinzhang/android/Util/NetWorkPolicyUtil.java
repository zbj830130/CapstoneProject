package com.bojinzhang.android.Util;

import android.os.StrictMode;

/**
 * Created by zhangbojin on 24/05/17.
 */

public class NetWorkPolicyUtil {

    public static void NewWorkOnMainThread() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
