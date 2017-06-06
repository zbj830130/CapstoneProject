package com.bojinzhang.android.Util;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by zhangbojin on 25/05/17.
 */

public class DisplayUtil {
    public static int dipToPix(Context context, int dip) {
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
        return size;
    }
}
