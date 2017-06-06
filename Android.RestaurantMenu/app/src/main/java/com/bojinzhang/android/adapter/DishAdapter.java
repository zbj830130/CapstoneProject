package com.bojinzhang.android.adapter;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andremion.counterfab.CounterFab;
import com.bojinzhang.android.Business.ShoppingCartBuss;
import com.bojinzhang.android.Model.DishModel;
import com.bojinzhang.android.Util.BezierTypeEvaluatorUtil;
import com.bojinzhang.android.Util.HttpUtil;
import com.bojinzhang.android.Util.ToastUtil;
import com.bojinzhang.android.restaurantmenu.R;

import java.io.InputStream;
import java.util.List;

import okhttp3.Response;

/**
 * Created by zhangbojin on 29/05/17.
 */

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    private List<DishModel> mDishList;
    private CounterFab mCounterFab;
    private CounterFab mFabMove;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView dishImage;
        TextView eName;
        TextView oName;
        TextView eCommon;
        TextView oCommon;
        TextView price;
        Button mBuy;
        Button mCancel;
        Button mAdd;
        Button mSub;
        TextView mQtyNum;
        LinearLayout mQtyLayout;

        public ViewHolder(View mView) {
            super(mView);

            eName = (TextView) mView.findViewById(R.id.dish_e_name);
            oName = (TextView) mView.findViewById(R.id.dish_o_name);
            eCommon = (TextView) mView.findViewById(R.id.dish_e_common);
            oCommon = (TextView) mView.findViewById(R.id.dish_o_common);
            price = (TextView) mView.findViewById(R.id.dish_price);
            dishImage = (ImageView) mView.findViewById(R.id.dish_img);
            mBuy = (Button) mView.findViewById(R.id.dish_buy);
            mCancel = (Button) mView.findViewById(R.id.dish_cancel);
            mAdd = (Button) mView.findViewById(R.id.qty_add);
            mSub = (Button) mView.findViewById(R.id.qty_sub);
            mQtyNum = (TextView) mView.findViewById(R.id.qty_num);
            mQtyLayout = (LinearLayout) mView.findViewById(R.id.qty_layout);
        }
    }

    public DishAdapter(List<DishModel> dishList, CounterFab counterFab, CounterFab fabMove) {
        mDishList = dishList;
        mCounterFab = counterFab;
        mFabMove = fabMove;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dish_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DishModel dish = mDishList.get(position);
        holder.eName.setText(dish.getEName());
        holder.oName.setText(dish.getOName());
        holder.eCommon.setText(dish.getEComment());
        holder.oCommon.setText(dish.getOComment());
        holder.price.setText("$ " + String.valueOf(dish.getPrice()));

        elementsEventBinding(holder, dish);

        if (ShoppingCartBuss.getInstance().getItemsCount() > 0) {
            DishModel dishModel = ShoppingCartBuss.getInstance().getSingleItem(dish.getId());
            if (dishModel != null) {
                holder.mQtyNum.setText(String.valueOf(dishModel.getQty()));
                holder.mBuy.setVisibility(View.GONE);
                holder.mCancel.setVisibility(View.VISIBLE);
                holder.mQtyLayout.setVisibility(View.VISIBLE);
            }
        }


        try {
            Response response = HttpUtil.sendHttpRequest(dish.getImgUrl());
            InputStream is = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            holder.dishImage.setImageBitmap(bitmap);
            is.close();
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return mDishList.size();
    }

    private void elementsEventBinding(final ViewHolder holder, final DishModel dish) {
        //Buy button click
        holder.mBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mQtyNum.setText("1");
                runBezier(holder.mBuy);

                addDish(dish, 1);
                holder.mBuy.setVisibility(View.GONE);
                holder.mCancel.setVisibility(View.VISIBLE);
                holder.mQtyLayout.setVisibility(View.VISIBLE);

            }
        });

        //Cancel button click
        holder.mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDish(dish, 0);

                holder.mBuy.setVisibility(View.VISIBLE);
                holder.mCancel.setVisibility(View.GONE);
                holder.mQtyLayout.setVisibility(View.GONE);
                holder.mQtyNum.setText("");
            }
        });

        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.mQtyNum.getText().toString());
                if (qty >= 99) {

                    ToastUtil.ShowToast(v,
                            "Quantity Cannot more than 99");

                    return;
                } else {
                    qty++;
                    holder.mQtyNum.setText(String.valueOf(qty));
                    runBezier(holder.mAdd);
                    addDish(dish, qty);
                }
            }
        });

        holder.mSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.mQtyNum.getText().toString());
                if (qty <= 1) {
                    ToastUtil.ShowToast(v,
                            "Quantity Cannot less than 1");
                    return;
                } else {
                    qty--;
                    holder.mQtyNum.setText(String.valueOf(qty));
                    removeDish(dish, qty);
                    mCounterFab.setCount(qty);
                }
            }
        });
    }

    private void runBezier(final View startView) {
        int[] startPosition = new int[2];
        int[] endPosition = new int[2];

        startView.getLocationInWindow(startPosition);
        mCounterFab.getLocationInWindow(endPosition);

        PointF startF = new PointF();
        PointF endF = new PointF();
        PointF controllF = new PointF();


        startF.x = startPosition[0];
        startF.y = startPosition[1];
        endF.x = endPosition[0];
        endF.y = endPosition[1];
        controllF.x = endF.x;
        controllF.y = startF.y;

        mFabMove.setX(startF.x);
        mFabMove.setY(startF.y);
        mFabMove.setVisibility(View.VISIBLE);

        final ValueAnimator valueAnimator = ValueAnimator.ofObject(new BezierTypeEvaluatorUtil(controllF), startF, endF);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                PointF pointF = (PointF) animation.getAnimatedValue();
                mFabMove.setX(pointF.x);
                mFabMove.setY(pointF.y);
                if (valueAnimator.getCurrentPlayTime() >= 800) {
                    mFabMove.setVisibility(View.GONE);
                }
            }
        });

        valueAnimator.setDuration(800);
        valueAnimator.start();
    }

    private void addDish(DishModel dish, int qty) {
        dish.setQty(qty);
        ShoppingCartBuss.getInstance().addItem(dish);
        setCounterFab();
    }

    private void removeDish(DishModel dish, int qty) {
        dish.setQty(qty);
        ShoppingCartBuss.getInstance().removeItem(dish);
        setCounterFab();
    }

    private void setCounterFab() {
        mCounterFab.setCount(ShoppingCartBuss.getInstance().getItemsCount());
    }
}
