package com.bojinzhang.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bojinzhang.android.Business.ShoppingCartBuss;
import com.bojinzhang.android.Model.DishModel;
import com.bojinzhang.android.Util.ToastUtil;
import com.bojinzhang.android.restaurantmenu.R;

import java.util.List;

/**
 * Created by zhangbojin on 31/05/17.
 */

public class ShoppingCartItemsAdapter extends RecyclerView.Adapter<ShoppingCartItemsAdapter.ViewHolder> {
    private List<DishModel> mDishModels;
    TextView mTotalAmount;
    TextView mTotalPrice;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dishEName;
        TextView dishCName;
        TextView dishUnitPrice;
        TextView dishQty;
        TextView dishAmountPrice;
        Button dishQtyAdd;
        Button dishQtySub;
        Button dishCancel;

        public ViewHolder(View view) {
            super(view);
            dishEName = (TextView) view.findViewById(R.id.shopping_cart_dish_e_name);
            dishCName = (TextView) view.findViewById(R.id.shopping_cart_dish_o_name);
            dishUnitPrice = (TextView) view.findViewById(R.id.shopping_cart_dish_unit_price);
            dishQty = (TextView) view.findViewById(R.id.shopping_cart_dish_qty_num);
            dishAmountPrice = (TextView) view.findViewById(R.id.shopping_cart_dish_amount_price);

            dishQtyAdd = (Button) view.findViewById(R.id.shopping_cart_dish_qty_add);
            dishQtySub = (Button) view.findViewById(R.id.shopping_cart_dish_qty_sub);
            dishCancel = (Button) view.findViewById(R.id.shopping_cart_dish_cancel);
        }
    }

    public ShoppingCartItemsAdapter(TextView totalAmount, TextView totalPrice) {
        mDishModels = ShoppingCartBuss.getInstance().getShoppingCartItems();
        mTotalAmount = totalAmount;
        mTotalPrice = totalPrice;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.shopping_cart_dish_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final DishModel model = mDishModels.get(position);
        holder.dishEName.setText(model.getEName());
        holder.dishCName.setText(model.getOName());
        holder.dishUnitPrice.setText(String.valueOf(model.getPrice()));
        holder.dishQty.setText(String.valueOf(model.getQty()));
        holder.dishAmountPrice.setText(String.valueOf(model.getQty() * model.getPrice()));

        //Cancel button click
        holder.dishCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeDish(model, 0);
                updateTotal();
                notifyDataSetChanged();
            }
        });

        holder.dishQtyAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.dishQty.getText().toString());
                if (qty >= 99) {

                    ToastUtil.ShowToast(v,
                            "Quantity Cannot more than 99");

                    return;
                } else {
                    qty++;
                    holder.dishQty.setText(String.valueOf(qty));
                    holder.dishAmountPrice.setText(String.valueOf(qty * model.getPrice()));
                    addDish(model, qty);
                }

                updateTotal();
            }
        });

        holder.dishQtySub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = Integer.parseInt(holder.dishQty.getText().toString());
                if (qty <= 1) {
                    ToastUtil.ShowToast(v,
                            "Quantity Cannot less than 1");
                    return;
                } else {
                    qty--;
                    holder.dishQty.setText(String.valueOf(qty));
                    holder.dishAmountPrice.setText(String.valueOf(qty * model.getPrice()));
                    removeDish(model, qty);
                }

                updateTotal();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDishModels.size();
    }

    private void addDish(DishModel dish, int qty) {
        dish.setQty(qty);
        ShoppingCartBuss.getInstance().addItem(dish);
    }

    private void removeDish(DishModel dish, int qty) {
        dish.setQty(qty);
        ShoppingCartBuss.getInstance().removeItem(dish);
    }

    private void updateTotal() {
        mTotalAmount.setText(String.valueOf(ShoppingCartBuss.getInstance().getItemsCount()));
        mTotalPrice.setText(String.valueOf(ShoppingCartBuss.getInstance().getTotalPrice()));

    }

}
