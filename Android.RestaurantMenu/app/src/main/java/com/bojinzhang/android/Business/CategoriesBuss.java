package com.bojinzhang.android.Business;

import com.bojinzhang.android.Model.CategoryModel;
import com.bojinzhang.android.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by zhangbojin on 29/05/17.
 */

public class CategoriesBuss {
    public ArrayList<CategoryModel> getCategories() {
        ArrayList<CategoryModel> categoryModels = new ArrayList<CategoryModel>();

        try {
            Response response = HttpUtil.sendHttpRequest("http://10.0.2.2:8081/api/Category/GetAllCategories");
            String data = response.body().string();

            JSONArray categories = new JSONObject(data).getJSONArray("Categories");
            for (int i = 0; i < categories.length(); i++) {
                JSONObject item = categories.getJSONObject(i);
                int id = item.getInt("Id");
                String eName = item.getString("EName");
                String oName = item.getString("OtherName");

                CategoryModel model = new CategoryModel(eName, oName, id);
                categoryModels.add(model);
            }
        } catch (Exception e) {

        }
        return categoryModels;
    }
}
