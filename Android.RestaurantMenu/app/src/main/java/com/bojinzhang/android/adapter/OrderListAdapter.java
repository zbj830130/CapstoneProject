package com.bojinzhang.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bojinzhang.android.Business.ShoppingCartBuss;
import com.bojinzhang.android.Model.OrderInfoModel;
import com.bojinzhang.android.Util.HttpUtil;
import com.bojinzhang.android.restaurantmenu.OrderListActivity;
import com.bojinzhang.android.restaurantmenu.R;
import com.bojinzhang.android.restaurantmenu.ShoppingCartDetailActivity;

import java.util.ArrayList;

/**
 * Created by zhangbojin on 2/06/17.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private ArrayList<OrderInfoModel> mOrderList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mOrderId;
        TextView mGenderAndName;
        TextView mTableNum;
        Button mCheckIn;
        Button mDetail;

        public ViewHolder(View view) {
            super(view);
            mOrderId = (TextView) view.findViewById(R.id.order_item_orderId);
            mGenderAndName = (TextView) view.findViewById(R.id.order_item_gender_lastName);
            mTableNum = (TextView) view.findViewById(R.id.order_item_tableNum);
            mCheckIn = (Button) view.findViewById(R.id.order_item_checkIn);
            mDetail = (Button) view.findViewById(R.id.order_item_detail);
        }
    }

    public OrderListAdapter(ArrayList<OrderInfoModel> orderInfoList, Context context) {
        mOrderList = orderInfoList;
        mContext = context;
    }

    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list_item, parent, false);
        OrderListAdapter.ViewHolder holder = new OrderListAdapter.ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(final OrderListAdapter.ViewHolder holder, final int position) {
        final OrderInfoModel model = mOrderList.get(position);
        holder.mOrderId.setText(model.OrderId);
        holder.mGenderAndName.setText(model.Gender + " " + model.LastName);

        if (model.TableNum == "0" || model.TableNum.length() == 0) {
            holder.mTableNum.setText("");
        } else {
            holder.mTableNum.setText(model.TableNum);
        }

        if (model.Status == 1) {
            holder.mCheckIn.setVisibility(View.VISIBLE);
        } else {
            holder.mCheckIn.setVisibility(View.INVISIBLE);
        }

        holder.mCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String json = "{orderId:" + holder.mOrderId.getText().toString() + ",status:" + 3 + "}";
                HttpUtil.postJson(json, "http://10.0.2.2:8081/api/Order/UpdateOrderStatus");
                model.Status = 3;
                notifyItemChanged(position);
            }
        });

        holder.mDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OrderListActivity orderListActivity = (OrderListActivity) mContext;

                ShoppingCartBuss.getInstance()
                        .fillUpShoppingCartItemsByOrderId(model.OrderId, model);


                final Intent mainIntent = new Intent(orderListActivity, ShoppingCartDetailActivity.class);
                orderListActivity.startActivity(mainIntent);
                orderListActivity.finish();
                orderListActivity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }
}
