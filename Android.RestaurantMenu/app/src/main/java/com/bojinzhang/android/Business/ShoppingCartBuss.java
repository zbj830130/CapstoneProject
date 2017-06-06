package com.bojinzhang.android.Business;

import com.bojinzhang.android.Model.DishModel;
import com.bojinzhang.android.Model.OrderInfoModel;
import com.bojinzhang.android.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by zhangbojin on 31/05/17.
 */

public class ShoppingCartBuss {
    private ArrayList<DishModel> mShoppingCartItems = new ArrayList<>();
    private OrderInfoModel mOrderInfo = new OrderInfoModel();

    private static class ShoppingCartBussHolder {
        private static ShoppingCartBuss instance = new ShoppingCartBuss();
    }

    private ShoppingCartBuss() {

    }

    public static ShoppingCartBuss getInstance() {
        return ShoppingCartBussHolder.instance;
    }

    public DishModel getSingleItem(int id) {
        DishModel result = null;
        for (int i = 0; i < mShoppingCartItems.size(); i++) {
            DishModel item = mShoppingCartItems.get(i);
            if (item.getId() == id) {
                result = item;
                break;
            }
        }

        return result;
    }

    public ArrayList<DishModel> getShoppingCartItems() {
        return mShoppingCartItems;
    }

    public void addItem(DishModel dish) {
        if (mShoppingCartItems.size() == 0) {
            mShoppingCartItems.add(dish);
        }

        boolean isMatching = false;

        for (int i = 0; i < mShoppingCartItems.size(); i++) {
            DishModel item = mShoppingCartItems.get(i);

            if (item.getId() == dish.getId()) {
                item.setQty(dish.getQty());
                isMatching = true;
                break;
            }
        }

        if (isMatching == false) {
            mShoppingCartItems.add(dish);
        }
    }

    public void removeItem(DishModel dish) {
        if (mShoppingCartItems.size() == 0) {
            return;
        }

        int deleteIndex = -1;
        for (int i = 0; i < mShoppingCartItems.size(); i++) {
            DishModel item = mShoppingCartItems.get(i);
            if (item.getId() == dish.getId()) {
                item.setQty(dish.getQty());

                if (item.getQty() <= 0) { //delete item
                    deleteIndex = i;
                }

                break;
            }
        }

        if (deleteIndex >= 0) {
            mShoppingCartItems.remove(deleteIndex);
        }
    }

    public int getItemsCount() {
        int count = 0;
        if (mShoppingCartItems.size() == 0) {
            return 0;
        }

        for (int i = 0; i < mShoppingCartItems.size(); i++) {
            count += mShoppingCartItems.get(i).getQty();
        }

        return count;
    }

    public double getTotalPrice() {
        double count = 0;
        if (mShoppingCartItems.size() == 0) {
            return 0;
        }

        for (int i = 0; i < mShoppingCartItems.size(); i++) {
            DishModel model = mShoppingCartItems.get(i);

            count += (model.getQty() * model.getPrice());
        }

        return count;
    }

    public void clearItems() {
        mShoppingCartItems = new ArrayList<>();
    }

    public void reNewShoppingCart(){
        mOrderInfo = new OrderInfoModel();
        mShoppingCartItems = new ArrayList<>();
    }

    public OrderInfoModel getOrderInfo() {
        return mOrderInfo;
    }

    public void setOrderInfo(OrderInfoModel orderInfo) {
        mOrderInfo.TableNum = orderInfo.TableNum;
        mOrderInfo.OrderId = orderInfo.OrderId;
        mOrderInfo.LastName = orderInfo.LastName;
        mOrderInfo.Status = orderInfo.Status;
        mOrderInfo.Gender = orderInfo.Gender;
        mOrderInfo.TableHeadcount = orderInfo.TableHeadcount;
        mOrderInfo.WaiterId = orderInfo.WaiterId;
    }

    public void fillUpShoppingCartItemsByOrderId(String orderId, OrderInfoModel orderInfo) {
        reNewShoppingCart();

        setOrderInfo(orderInfo);

        try {
            Response response = HttpUtil.sendHttpRequest
                    ("http://10.0.2.2:8081/api/Order/GetOrderDetails?orderId=" + orderId);

            String data = response.body().string();
            JSONArray details = new JSONObject(data).getJSONArray("Details");

            for (int i = 0; i < details.length(); i++) {
                JSONObject item = details.getJSONObject(i);
                DishModel model = new DishModel();
                model.setId(item.getInt("DishId"));
                model.setQty(item.getInt("Count"));
                model.setPrice(item.getDouble("UnitPrice"));
                model.setEName(item.getString("DishEName"));
                model.setOName(item.getString("DishOName"));

                mShoppingCartItems.add(model);
            }

        } catch (Exception e) {

        }
    }
}
