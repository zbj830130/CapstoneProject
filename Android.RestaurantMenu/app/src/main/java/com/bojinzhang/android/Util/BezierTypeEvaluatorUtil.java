package com.bojinzhang.android.Util;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * Created by zhangbojin on 31/05/17.
 */

public class BezierTypeEvaluatorUtil implements TypeEvaluator<PointF> {
    private PointF mControllPoint;

    public BezierTypeEvaluatorUtil(PointF mControllPoint) {
        this.mControllPoint = mControllPoint;
    }

    @Override
    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
        PointF pointCur = new PointF();
        pointCur.x = (1 - fraction) * (1 - fraction) * startValue.x + 2 * fraction * (1 - fraction) * mControllPoint.x + fraction * fraction * endValue.x;
        pointCur.y = (1 - fraction) * (1 - fraction) * startValue.y + 2 * fraction * (1 - fraction) * mControllPoint.y + fraction * fraction * endValue.y;
        return pointCur;
    }
}
