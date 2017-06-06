package com.bojinzhang.android.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.bojinzhang.android.Model.CategoryModel;
import com.bojinzhang.android.Util.DisplayUtil;
import com.bojinzhang.android.restaurantmenu.R;
import com.bojinzhang.android.restaurantmenu.DishesFragment;
import com.shizhefei.view.indicator.IndicatorViewPager;

import java.util.ArrayList;

/**
 * Created by zhangbojin on 25/05/17.
 */

public class CategoryAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private ArrayList<CategoryModel> mCategories;
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private CounterFab mCounterFab;
    private CounterFab mFabMove;

    public CategoryAdapter(ArrayList<CategoryModel> categories, FragmentManager fragmentManager
            , LayoutInflater layoutInflater, Context context, CounterFab counterFab, CounterFab fabMove) {
        super(fragmentManager);
        mCategories = categories;
        mLayoutInflater = layoutInflater;
        mContext = context;
        mCounterFab = counterFab;
        mFabMove = fabMove;
    }

    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.tab_top, container, false);
        }

        int padding = DisplayUtil.dipToPix(mContext, 12);

        TextView eName = (TextView) convertView;
        eName.setPadding(padding, 0, padding, 0);
        eName.setText(String.valueOf(mCategories.get(position).getEName()
                + "(" + mCategories.get(position).getOName() + ")"));

        return convertView;
    }

    @Override
    public Fragment getFragmentForPage(int position) {
        int categoryId = mCategories.get(position).getId();

        DishesFragment dishesFragment = new DishesFragment();
        Bundle a = new Bundle();
        a.putInt(DishesFragment.CATEGORY_ID, categoryId);
        dishesFragment.setArguments(a);
        dishesFragment.setCounterFab(mCounterFab, mFabMove);
        return dishesFragment;
    }

    @Override
    public int getCount() {
        return mCategories.size();
    }
}
