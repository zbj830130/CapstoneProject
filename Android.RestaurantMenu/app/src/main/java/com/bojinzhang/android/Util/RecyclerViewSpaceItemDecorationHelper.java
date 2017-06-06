package com.bojinzhang.android.Util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhangbojin on 29/05/17.
 */

public class RecyclerViewSpaceItemDecorationHelper extends RecyclerView.ItemDecoration {
    private int space;
    private int spanCount;

    public RecyclerViewSpaceItemDecorationHelper(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.bottom = space;

        if (parent.getChildLayoutPosition(view) % spanCount == 0) {
            outRect.left = 0;
        }
    }
}
