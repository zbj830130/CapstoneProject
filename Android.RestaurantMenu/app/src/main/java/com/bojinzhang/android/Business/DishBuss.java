package com.bojinzhang.android.Business;

import com.bojinzhang.android.Model.DishModel;
import com.bojinzhang.android.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.Response;

/**
 * Created by zhangbojin on 29/05/17.
 */

public class DishBuss {

    public ArrayList<DishModel> getDishesByCategoryId(int categoryId) {
        ArrayList<DishModel> dishesFromWeb = getDishesFromWeb(categoryId);
        return dishesFromWeb;
    }

    private ArrayList<DishModel> getDishesFromWeb(int categoryId) {
        ArrayList<DishModel> dishesFromWeb = new ArrayList<>();
        try {
            Response response = HttpUtil.sendHttpRequest("http://10.0.2.2:8081/api/Dish/GetDishesByCategoryId?id=" + categoryId);
            String data = response.body().string();

            JSONArray dishes = new JSONObject(data).getJSONArray("Dishes");
            for (int i = 0; i < dishes.length(); i++) {
                JSONObject item = dishes.getJSONObject(i);

                DishModel model = new DishModel();

                model.setId(item.getInt("Id"));
                model.setCategoryId(item.getInt("CategoryId"));

                model.setEName(item.getString("EName"));
                model.setOName(item.getString("OtherName"));

                model.setPrice(item.getDouble("Price"));

                model.setEComment(item.getString("EComment"));
                model.setOComment(item.getString("OtherComment"));

                model.setCEName(item.getString("CEName"));
                model.setCOName(item.getString("COName"));

                model.setModifyTime(item.getString("ModifyTime"));
                model.setImgUrl(item.getString("ImgUrl"));

                dishesFromWeb.add(model);
            }
        } catch (Exception e) {

        }

        return dishesFromWeb;
    }

//    private ArrayList<DishModel> getDishesFromDB() {
//        ArrayList<DishModel> models = new ArrayList<>();
//
//        mDatabase = mHelper.getWritableDatabase();
//        Cursor cursor = mDatabase.rawQuery("SELECT * FROM Dishes", null);
//        while (cursor.moveToNext()) {
//            DishModel item = new DishModel();
//            item.setId(cursor.getInt(0));
//            item.setCategoryId(cursor.getInt(1));
//            item.setEName(cursor.getString(2));
//            item.setOName(cursor.getString(3));
//            item.setPrice(cursor.getDouble(4));
//            item.setEComment(cursor.getString(5));
//            item.setOtherComment(cursor.getString(6));
//            item.setModifyTime(cursor.getString(7));
//            models.add(item);
//        }
//
//        return models;
//    }
//
//    private String getLastModifyTime() {
//
//        String getFromWeb = null;
//
//        try {
//            Response response = HttpUtil.sendHttpRequest("http://10.0.2.2:8081/api/Dish/GetLastModifyTime");
//            String data = response.body().string();
//            JSONObject object = new JSONObject(data);
//            getFromWeb = object.getString("LastModifyTime");
//        } catch (Exception e) {
//        }
//
//        return getFromWeb;
//    }

//    private boolean isUpdateDishes(String lastModifyTime) {
//        if (lastModifyTime != null && lastModifyTime.length() > 0) {
//            String getFromDB = null;
//
//            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            getFromDB = getLastModifyTimeFromDataBase();
//
//            if (getFromDB == null) {
//                updateDataChangingLog(getFromDB, lastModifyTime);
//                return true;
//            }
//
//            try {
//                Date getFromWeb = formatter.parse(lastModifyTime);
//                Date dbDate = formatter.parse(getFromDB);
//                long timeLong = getFromWeb.getTime() - dbDate.getTime();
//                if (timeLong > 0) {
//                    updateDataChangingLog(getFromDB, lastModifyTime);
//                    return true;
//                } else {
//                    return false;
//                }
//
//            } catch (Exception ext) {
//
//            }
//        }
//
//        return false;
//    }

//    private void updateDataChangingLog(String dateFromWeb, String lastModifyTime) {
//        mDatabase = mHelper.getWritableDatabase();
//
//        if (dateFromWeb == null) {
//            mDatabase.execSQL("UPDATE DataChangingLog SET LastModifyTime = ? WHERE Type = ?", new String[]{lastModifyTime, "Dishes"});
//        } else {
//            mDatabase.execSQL("INSERT INTO DataChangingLog values (?,?)", new Object[]{"Dishes", lastModifyTime});
//        }
//
//        mDatabase.close();
//    }
//
//    private String getLastModifyTimeFromDataBase() {
//        String getFromDB = null;
//        mDatabase = mHelper.getWritableDatabase();
//        Cursor cursor = mDatabase.rawQuery("SELECT LastModifyTime FROM DataChangingLog WHERE Type = 'Dishes'", null);
//        if (cursor.moveToFirst()) {
//            getFromDB = cursor.getString(0);
//        }
//
//        mDatabase.close();
//        return getFromDB;
//    }
}
