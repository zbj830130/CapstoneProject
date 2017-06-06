package com.bojinzhang.android.restaurantmenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.aigestudio.wheelpicker.WheelPicker;
import com.bojinzhang.android.Business.ShoppingCartBuss;
import com.bojinzhang.android.Business.WaiterCheckBuss;
import com.bojinzhang.android.Model.OrderInfoModel;
import com.bojinzhang.android.Util.NetWorkPolicyUtil;
import com.bojinzhang.android.Util.ToastUtil;

import java.util.ArrayList;

public class WaitersCheckActivity extends FragmentActivity {

    private WheelPicker mTableNum;
    private WheelPicker mTableHeadcount;
    private ImageView mClose;
    private EditText mPwd;
    private Button mSubmit;
    private Button mBack;
    private Button mCloseBtn;
    private WaiterCheckBuss buss;
    private String mSelectedTableNum;
    private String mSelectedTableHeadcount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
        getWindow().getDecorView().setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_waiters_check);
        NetWorkPolicyUtil.NewWorkOnMainThread();

        initView();
        elementEventsBinding();
    }

    private void initView() {
        buss = new WaiterCheckBuss();
        mTableNum = (WheelPicker) findViewById(R.id.table_num_wheel);
        mTableHeadcount = (WheelPicker) findViewById(R.id.table_headcount_wheel);

        mClose = (ImageView) findViewById(R.id.waiter_check_close);
        mPwd = (EditText) findViewById(R.id.waiter_check_pwd);
        mSubmit = (Button) findViewById(R.id.waiter_check_submit_button);
        mBack = (Button) findViewById(R.id.waiter_check_back_to_shoppingCart);


        mCloseBtn = (Button) findViewById(R.id.waiter_check_close_button);

        ArrayList<String> tableNums = buss.getTableNums();
        ArrayList<String> tableHeadcount = buss.getHeadcounts();

        mTableNum.setData(tableNums);
        mTableHeadcount.setData(tableHeadcount);

        if (ShoppingCartBuss.getInstance().getOrderInfo() != null) {
            OrderInfoModel orderInfo = ShoppingCartBuss.getInstance().getOrderInfo();
            for (int i = 0; i < tableNums.size(); i++) {
                if (tableNums.get(i).equals(orderInfo.TableNum)) {
                    mTableNum.setSelectedItemPosition(i);
                    break;
                }
            }

            for (int i = 0; i < tableHeadcount.size(); i++) {
                if (tableHeadcount.get(i).equals(orderInfo.TableHeadcount)) {
                    mTableHeadcount.setSelectedItemPosition(i);
                    break;
                }
            }
        }
    }

    private void elementEventsBinding() {
        mClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitersCheckActivity.this.finish();
            }
        });

        mCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitersCheckActivity.this.finish();
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mainIntent = new Intent(WaitersCheckActivity.this, ShoppingCartDetailActivity.class);
                WaitersCheckActivity.this.startActivity(mainIntent);
                WaitersCheckActivity.this.finish();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        mTableNum.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mSelectedTableNum = picker.getData().get(position).toString();
            }
        });

        mTableHeadcount.setOnItemSelectedListener(new WheelPicker.OnItemSelectedListener() {
            @Override
            public void onItemSelected(WheelPicker picker, Object data, int position) {
                mSelectedTableHeadcount = picker.getData().get(position).toString();
            }
        });


        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int waiterId = buss.getWaiterIdByPwd(mPwd.getText().toString());
                if (waiterId == 0) {
                    ToastUtil.ShowToast(getApplicationContext(), "Password is Wrong");
                    return;
                }

                if (mSelectedTableHeadcount == null || mSelectedTableHeadcount.length() == 0) {
                    mSelectedTableHeadcount = mTableHeadcount.getData()
                            .get(mTableHeadcount.getCurrentItemPosition())
                            .toString();

                }

                if (mSelectedTableNum == null || mSelectedTableNum.length() == 0) {
                    mSelectedTableNum = mTableNum.getData()
                            .get(mTableNum.getCurrentItemPosition())
                            .toString();

                }

                boolean result = buss.createOrder(mSelectedTableNum, mSelectedTableHeadcount);
                if (result == true) {
                    ShoppingCartBuss.getInstance().reNewShoppingCart();
                    WaitersCheckActivity.this.finish();
                }

            }
        });
    }
}