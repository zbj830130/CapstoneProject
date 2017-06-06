package com.bojinzhang.android.restaurantmenu;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.andremion.counterfab.CounterFab;
import com.bojinzhang.android.Business.DishBuss;
import com.bojinzhang.android.Model.DishModel;
import com.bojinzhang.android.adapter.DishAdapter;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;

/**
 * Created by zhangbojin on 25/05/17.
 */

public class DishesFragment extends LazyFragment {
    public static final String CATEGORY_ID = "category_id";
    private CounterFab mCounterFab;
    private CounterFab mFabMove;
    private ArrayList<DishModel> mDishModels = new ArrayList<DishModel>();
    public RecyclerView dishListRecyclerView;

    public void setCounterFab(CounterFab counterFab, CounterFab fabMove) {
        mCounterFab = counterFab;
        mFabMove = fabMove;
    }

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_dishes);

        int categoryId = getArguments().getInt(CATEGORY_ID);
        iniDishes(categoryId);
        dishListRecyclerView = (RecyclerView) findViewById(R.id.dishes_recycler_view);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);

        dishListRecyclerView.setLayoutManager(gridLayoutManager);
//        dishListRecyclerView.addItemDecoration(new DividerItemDecorationHelper(getContext(),
//                LinearLayoutManager.VERTICAL,
//                Color.GRAY,
//                1));

        DishAdapter adapter = new DishAdapter(mDishModels, mCounterFab, mFabMove);
        dishListRecyclerView.setAdapter(adapter);
    }

//    @Override
//    protected void onResumeLazy() {
//        super.onResumeLazy();
//        dishListRecyclerView.notifyAll();
//    }

    private void iniDishes(int categoryId) {
        DishBuss buss = new DishBuss();
        mDishModels = buss.getDishesByCategoryId(categoryId);
    }
}
