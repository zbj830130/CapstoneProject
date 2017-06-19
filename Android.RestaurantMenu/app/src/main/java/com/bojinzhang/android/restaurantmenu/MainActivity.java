package com.bojinzhang.android.restaurantmenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.andremion.counterfab.CounterFab;
import com.bojinzhang.android.Business.CategoriesBuss;
import com.bojinzhang.android.Business.ShoppingCartBuss;
import com.bojinzhang.android.Interface.AlertDialogReturnInterface;
import com.bojinzhang.android.Model.CategoryModel;
import com.bojinzhang.android.Util.AlertDialogUtil;
import com.bojinzhang.android.Util.AndroidVersionSixUtil;
import com.bojinzhang.android.Util.NetWorkPolicyUtil;
import com.bojinzhang.android.Util.ToastUtil;
import com.bojinzhang.android.adapter.CategoryAdapter;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import java.util.ArrayList;

public class MainActivity extends FragmentActivity {

    private IndicatorViewPager indicatorViewPager;
    private CounterFab fab;
    private CounterFab fabMove;
    private boolean isOnPause = false;
    private ViewPager viewPager;
    private ScrollIndicatorView scrollIndicatorView;
    private double preTotalPrice;
    private FloatingActionMenu fabMenu;

    public void setFABCount(int count) {
        fab.setCount(count);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NetWorkPolicyUtil.NewWorkOnMainThread();
        AndroidVersionSixUtil.SDCardOpertaion(this);

        fab = (CounterFab) findViewById(R.id.fab);
        fabMove = (CounterFab) findViewById(R.id.fab_move);
        fabEventBinding(fab);
        fabCircleMenu(fab);

        viewPager = (ViewPager) findViewById(R.id.dishes_viewPager);
        scrollIndicatorView = (ScrollIndicatorView) findViewById(R.id.categories_indicator);

        float unSelectSize = 18;
        float selectSize = 22;
        scrollIndicatorView.setOnTransitionListener(new OnTransitionTextListener().setColor(0xFF2196F3, Color.GRAY).setSize(selectSize, unSelectSize));
        scrollIndicatorView.setScrollBar(new ColorBar(this, 0xFF2196F3, 4));


        viewPager.setOffscreenPageLimit(4);
        indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
        indicatorViewPager.setAdapter(new CategoryAdapter(
                getCategoriesNames(), getSupportFragmentManager(),
                getLayoutInflater(), getApplicationContext(), fab, fabMove
        ));


    }


    @Override
    protected void onResume() {
        super.onResume();
        preTotalPrice = ShoppingCartBuss.getInstance().getTotalPrice();
        refresh();

    }

    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
        refresh();
    }

    private ArrayList<CategoryModel> getCategoriesNames() {
        CategoriesBuss buss = new CategoriesBuss();
        ArrayList<CategoryModel> categories = buss.getCategories();
        return categories;
    }


    private void fabEventBinding(final CounterFab fab) {
        fab.setOnTouchListener(new View.OnTouchListener() {
            float dX;
            float dY;
            int lastAction;

            @Override
            public boolean onTouch(View view, MotionEvent event) {

                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        dX = view.getX() - event.getRawX();
                        dY = view.getY() - event.getRawY();
                        lastAction = MotionEvent.ACTION_DOWN;
                        break;

                    case MotionEvent.ACTION_MOVE:
                        view.setY(event.getRawY() + dY);
                        view.setX(event.getRawX() + dX);
                        lastAction = MotionEvent.ACTION_MOVE;
                        break;

                    case MotionEvent.ACTION_UP:
                        return lastAction != MotionEvent.ACTION_DOWN;
                }


                return false;
            }
        });
    }

    private void fabCircleMenu(final CounterFab fab) {
        SubActionButton.Builder rLSubBuilder = new SubActionButton.Builder(this);

        ImageView shoppingCart_new = new ImageView(this);
        ImageView shoppingCart_details = new ImageView(this);
        ImageView shoppingCart_removeItems = new ImageView(this);
        ImageView shoppingCart_allOrders = new ImageView(this);
        shoppingCart_new.setImageDrawable(getResources().getDrawable(R.drawable.ic_shopping_cart_new_black_72dp));
        shoppingCart_details.setImageDrawable(getResources().getDrawable(R.drawable.ic_shopping_cart_details_black_72dp));
        shoppingCart_removeItems.setImageDrawable(getResources().getDrawable(R.drawable.ic_shopping_cart_remove_black_72dp));
        shoppingCart_allOrders.setImageDrawable(getResources().getDrawable(R.drawable.ic_restaurant_menu_black_72dp));


        fabMenu = new FloatingActionMenu.Builder(this).setStartAngle(150).setEndAngle(270)
                .addSubActionView(rLSubBuilder.setContentView(shoppingCart_new).build(), 96, 96)
                .addSubActionView(rLSubBuilder.setContentView(shoppingCart_details).build(), 96, 96)
                .addSubActionView(rLSubBuilder.setContentView(shoppingCart_removeItems).build(), 96, 96)
                .addSubActionView(rLSubBuilder.setContentView(shoppingCart_allOrders).build(), 96, 96)
                .attachTo(fab).build();


        shoppingCart_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtil.getClearAllAlertDialog(MainActivity.this,
                        new AlertDialogReturnInterface() {
                            @Override
                            public void RefreshActivity() {
                                refresh();
                                ToastUtil.ShowToast(MainActivity.this, "Shopping Cart Is Renewed");
                            }
                        }, "Confirm", "Are you sure to refresh shopping cart？");
            }
        });

        shoppingCart_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fab.getCount() <= 0) {
                    ToastUtil.ShowToast(MainActivity.this, "Shopping Cart is Empty");
                    return;
                }

                preTotalPrice = ShoppingCartBuss.getInstance().getTotalPrice();
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ShoppingCartDetailActivity.class);
                startActivity(intent);
            }
        });

        shoppingCart_removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtil.getClearAllAlertDialog(MainActivity.this,
                        new AlertDialogReturnInterface() {
                            @Override
                            public void RefreshActivity() {
                                refresh();
                                ToastUtil.ShowToast(MainActivity.this, "Shopping Cart Is Cleared");
                            }
                        }, "Confirm", "Are you sure to clear all items？");
            }
        });

        shoppingCart_allOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
    }

    private void refresh(){
        int preCount = fab.getCount();
        if (preCount != ShoppingCartBuss.getInstance().getItemsCount()
                || preTotalPrice != ShoppingCartBuss.getInstance().getTotalPrice()) {

            indicatorViewPager = new IndicatorViewPager(scrollIndicatorView, viewPager);
            indicatorViewPager.setAdapter(new CategoryAdapter(
                    getCategoriesNames(), getSupportFragmentManager(),
                    getLayoutInflater(), getApplicationContext(), fab, fabMove
            ));

            indicatorViewPager.setCurrentItem(0, false);
        }

        fab.setCount(ShoppingCartBuss.getInstance().getItemsCount());
    }
}
