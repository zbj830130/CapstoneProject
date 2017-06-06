package com.bojinzhang.android.Business;

import com.bojinzhang.android.Model.DishModel;
import com.bojinzhang.android.Model.OrderDishInfo;
import com.bojinzhang.android.Model.OrderInfoFromAndroidModel;
import com.bojinzhang.android.Util.HttpUtil;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by zhangbojin on 1/06/17.
 */

public class WaiterCheckBuss {
    public ArrayList<String> getTableNums() {
        ArrayList<String> numbers = new ArrayList<>();

        try {
            Response response = HttpUtil.sendHttpRequest("http://10.0.2.2:8081/api/Table/GetTableNumList");
            String data = response.body().string();
            JSONArray tables = new JSONObject(data).getJSONArray("TableNums");
            for (int i = 0; i < tables.length(); i++) {
                JSONObject item = tables.getJSONObject(i);
                numbers.add(item.getString("TableNum"));
            }
        } catch (Exception e) {

        }

        return numbers;
    }

    public ArrayList<String> getHeadcounts() {
        ArrayList<String> headcounts = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            headcounts.add(String.valueOf(i));
        }

        return headcounts;
    }

    public int getWaiterIdByPwd(String pwd) {

        int waiterId = 0;

        try {
            Response response = HttpUtil.sendHttpRequest("http://10.0.2.2:8081/api/waiter/GetUWaiterByPwd?pwd=" + pwd);
            String data = response.body().string();

            JSONObject jsonObject = new JSONObject(data);
            waiterId = jsonObject.getInt("userId");

        } catch (Exception e) {

        }

        return waiterId;
    }

    public boolean createOrder(String tableNum, String tableHeadcount) {
        OrderInfoFromAndroidModel model = new OrderInfoFromAndroidModel();

        if (ShoppingCartBuss.getInstance().getOrderInfo() != null) {
            model.OrderId = ShoppingCartBuss.getInstance().getOrderInfo().OrderId;
        }

        model.WaiterId = 1;
        model.TableNum = tableNum;
        model.TableHeadcount = tableHeadcount;
        model.Dishes = new ArrayList<OrderDishInfo>();

        ArrayList<DishModel> orderDish = ShoppingCartBuss.getInstance().getShoppingCartItems();
        for (int i = 0; i < orderDish.size(); i++) {
            OrderDishInfo item = new OrderDishInfo();
            DishModel dishModel = orderDish.get(i);

            item.DishId = dishModel.getId();
            item.UnitPrice = dishModel.getPrice();
            item.EName = dishModel.getEName();
            item.OName = dishModel.getOName();
            item.Qty = dishModel.getQty();

            model.Dishes.add(item);
        }

        Gson gson = new Gson();
        String json = gson.toJson(model);

        if (ShoppingCartBuss.getInstance().getOrderInfo().OrderId != null) {
            return HttpUtil.postJson(json, "http://10.0.2.2:8081/api/order/ModifyOrder");
        } else {
            return HttpUtil.postJson(json, "http://10.0.2.2:8081/api/order/CreateOrder");
        }


    }
}
