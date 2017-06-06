package com.bojinzhang.android.adapter;

import android.graphics.Bitmap;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bojinzhang.android.Util.FileUtil;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zhangbojin on 23/05/17.
 */

public class RollPageViewAdapter extends StaticPagerAdapter {
    private ArrayList<Bitmap> imgs = new ArrayList<Bitmap>();

    public RollPageViewAdapter() {
        File bannersRootDir = new File(Environment.getExternalStorageDirectory() + "/EMenu/Banners/");
        File[] files = bannersRootDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            Bitmap bitmap = FileUtil.getPircure(files[i].getAbsolutePath());
            imgs.add(bitmap);
        }
    }

    @Override
    public View getView(ViewGroup container, int position) {
        ImageView view = new ImageView(container.getContext());
        view.setImageBitmap(imgs.get(position));
        view.setScaleType(ImageView.ScaleType.FIT_XY);
        view.setLayoutParams(
                new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                        , ViewGroup.LayoutParams.MATCH_PARENT));
        return view;
    }


    @Override
    public int getCount() {
        return imgs.size();
    }
}
