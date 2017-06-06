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

import com.bojinzhang.android.Business.OrderInfoBuss;
import com.bojinzhang.android.Util.DividerItemDecorationHelper;
import com.bojinzhang.android.adapter.OrderListAdapter;

public class OrderListActivity extends FragmentActivity {

    private Button mShoppingCart;
    private Button mCloseBtn;
    private ImageView mClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);
        setContentView(R.layout.activity_order_list);

        initView();

        RecyclerView orderListRecyclerView = (RecyclerView) findViewById(R.id.order_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());

        orderListRecyclerView.setLayoutManager(layoutManager);
        orderListRecyclerView.addItemDecoration(new DividerItemDecorationHelper(getApplicationContext(),
                LinearLayoutManager.VERTICAL,
                Color.GRAY,
                1));

        OrderListAdapter adapter = new OrderListAdapter(new OrderInfoBuss().getOrderList(), this);
        orderListRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        mShoppingCart = (Button) findViewById(R.id.order_list_shoppingCart);
        mCloseBtn = (Button) findViewById(R.id.order_list_close_button);
        mClose = (ImageView) findViewById(R.id.order_list_close);

        mShoppingCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mainIntent = new Intent(OrderListActivity.this, ShoppingCartDetailActivity.class);
                OrderListActivity.this.startActivity(mainIntent);
                OrderListActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListActivity.this.finish();
            }
        });

        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderListActivity.this.finish();
            }
        });
    }
}
