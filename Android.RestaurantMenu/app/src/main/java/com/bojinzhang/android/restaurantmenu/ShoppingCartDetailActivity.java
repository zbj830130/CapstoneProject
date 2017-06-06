package com.bojinzhang.android.restaurantmenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bojinzhang.android.Business.ShoppingCartBuss;
import com.bojinzhang.android.Interface.AlertDialogReturnInterface;
import com.bojinzhang.android.Util.AlertDialogUtil;
import com.bojinzhang.android.Util.DividerItemDecorationHelper;
import com.bojinzhang.android.Util.ToastUtil;
import com.bojinzhang.android.adapter.ShoppingCartItemsAdapter;

public class ShoppingCartDetailActivity extends FragmentActivity {

    private TextView itemAmount;
    private TextView amountPrice;
    private ImageView mClose;
    private Button mConfirm;
    private Button mCloseBtn;
    private Button mAllOrders;
    private Button mAllClear;
    private RecyclerView shoppingCartRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_shopping_cart_detail);

        initView();

        shoppingCartRecyclerView = (RecyclerView) findViewById(R.id.shopping_cart_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        shoppingCartRecyclerView.setLayoutManager(layoutManager);
        shoppingCartRecyclerView.addItemDecoration(new DividerItemDecorationHelper(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                Color.GRAY,
                1));

        ShoppingCartItemsAdapter adapter = new ShoppingCartItemsAdapter(itemAmount, amountPrice);
        shoppingCartRecyclerView.setAdapter(adapter);
    }

    private void initView() {

        itemAmount = (TextView) findViewById(R.id.shopping_cart_dish_total_amount);
        amountPrice = (TextView) findViewById(R.id.shopping_cart_dish_total_price);
        mClose = (ImageView) findViewById(R.id.shopping_cart_close);
        mConfirm = (Button) findViewById(R.id.shopping_cart_confirm_order_button);
        mCloseBtn = (Button) findViewById(R.id.shopping_cart_confirm_close_button);
        mAllOrders = (Button) findViewById(R.id.all_orders_button);
        mAllClear = (Button) findViewById(R.id.shopping_cart_clearItems);

        itemAmount.setText(String.valueOf(ShoppingCartBuss.getInstance().getItemsCount()));
        amountPrice.setText(String.valueOf(ShoppingCartBuss.getInstance().getTotalPrice()));

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartDetailActivity.this.finish();
            }
        });

        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mainIntent = new Intent(ShoppingCartDetailActivity.this, WaitersCheckActivity.class);
                ShoppingCartDetailActivity.this.startActivity(mainIntent);
                ShoppingCartDetailActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartDetailActivity.this.finish();
            }
        });

        mAllOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mainIntent = new Intent(ShoppingCartDetailActivity.this, OrderListActivity.class);
                ShoppingCartDetailActivity.this.startActivity(mainIntent);
                ShoppingCartDetailActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mAllClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialogUtil.getClearAllAlertDialog(ShoppingCartDetailActivity.this,
                        new AlertDialogReturnInterface() {
                            @Override
                            public void RefreshActivity() {
                                ShoppingCartItemsAdapter adapter =
                                        new ShoppingCartItemsAdapter(itemAmount, amountPrice);
                                shoppingCartRecyclerView.setAdapter(adapter);
                                ToastUtil.ShowToast(ShoppingCartDetailActivity.this, "Shopping Cart Is Cleared");
                            }
                        }, "Confirm", "Are you sure to clear all items？");
            }
        });

    }
}
