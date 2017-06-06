package com.bojinzhang.android.Util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by zhangbojin on 30/05/17.
 */

public class ToastUtil {
    public static void ShowToast(View v, String text) {
        Toast t = Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }

    public static void ShowToast(Context context, String text) {
        Toast t = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        t.setGravity(Gravity.CENTER, 0, 0);
        t.show();
    }
}
