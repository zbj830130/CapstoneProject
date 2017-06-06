package com.bojinzhang.android.Business;

import com.bojinzhang.android.Model.OrderInfoModel;
import com.bojinzhang.android.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by zhangbojin on 2/06/17.
 */

public class OrderInfoBuss {
    public ArrayList<OrderInfoModel> getOrderList(){

        ArrayList<OrderInfoModel> result = new ArrayList<>();

        try {
            Response response = HttpUtil.sendHttpRequest("http://10.0.2.2:8081/api/Order/GetAllAvailableOrderInfos");
            String data = response.body().string();

            JSONArray orders = new JSONObject(data).getJSONArray("Orders");
            for (int i=0;i<orders.length();i++){
                JSONObject item = orders.getJSONObject(i);
                OrderInfoModel model = new OrderInfoModel();

                model.OrderId = item.getString("OrderId");
                model.Gender = item.getString("Gender");
                model.LastName = item.getString("LastName");
                model.TableHeadcount = item.getString("MealNumber");
                model.TableNum = item.getString("TableNum");
                model.WaiterId = item.getInt("WaiterId");
                model.Status = item.getInt("Status");

                result.add(model);
            }

        } catch (Exception e) {

        }

        return result;
    }
}
