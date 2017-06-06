package com.bojinzhang.android.Business;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bojinzhang.android.Dao.DataChangingLogHelper;
import com.bojinzhang.android.Util.HttpUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.Response;

/**
 * Created by zhangbojin on 23/05/17.
 */

public class ShowBannersBuss {
    private DataChangingLogHelper mHelper;
    private SQLiteDatabase mDatabase;
    private ArrayList<String> imgPath = null;
    private String lastModifyTime = null;

    public ArrayList<String> getImgPath() {
        return imgPath;
    }

    public ShowBannersBuss(Context context) {
        mHelper = new DataChangingLogHelper(context, "ElectronicMenu.db", null, 1);
        GetBannerPathsFromWebApi();
    }

    private void GetBannerPathsFromWebApi() {
        try {
            Response response = HttpUtil.sendHttpRequest("http://10.0.2.2:8081/api/Banners/GetBannerPictures");
            String data = response.body().string();
            if (imgPath != null && imgPath.size() > 0) {
                imgPath.clear();
            } else {
                imgPath = new ArrayList<String>();
            }

            JSONObject firstLevel = new JSONObject(data);
            lastModifyTime = firstLevel.getString("LastModifyTime");

            JSONArray pathUrl = firstLevel.getJSONArray("ImgPaths");
            for (int i = 0; i < pathUrl.length(); i++) {
                JSONObject path = pathUrl.getJSONObject(i);
                imgPath.add(path.get("ImgUrl").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean IsUpdateBanners() {

        if (lastModifyTime != null && lastModifyTime.length() > 0) {
            String getFromDB = null;

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            getFromDB = getLastModifyTimeFromDataBase();

            if (getFromDB == null) {
                updateDataChangingLog(getFromDB,lastModifyTime);
                return true;
            }

            try {
                Date getFromWeb = formatter.parse(lastModifyTime);
                Date dbDate = formatter.parse(getFromDB);
                long timeLong = getFromWeb.getTime() - dbDate.getTime();
                if (timeLong > 0) {
                    updateDataChangingLog(getFromDB,lastModifyTime);
                    return true;
                } else {
                    return false;
                }

            } catch (Exception ext) {

            }
        }

        return false;
    }

    private void updateDataChangingLog(String dateFromWeb, String lastModifyTime) {
        mDatabase = mHelper.getWritableDatabase();

        if (dateFromWeb == null) {
            mDatabase.execSQL("UPDATE DataChangingLog SET LastModifyTime = ? WHERE Type = ?", new String[]{lastModifyTime, "Banner"});
        } else {
            mDatabase.execSQL("INSERT INTO DataChangingLog values (?,?)", new Object[]{"Banner", lastModifyTime});
        }

        mDatabase.close();
    }

    private String getLastModifyTimeFromDataBase() {
        String getFromDB = null;
        mDatabase = mHelper.getWritableDatabase();
        Cursor cursor = mDatabase.rawQuery("SELECT LastModifyTime FROM DataChangingLog WHERE Type = 'Banner'", null);
        if (cursor.moveToFirst()) {
            getFromDB = cursor.getString(0);
        }

        mDatabase.close();
        return getFromDB;
    }


}
