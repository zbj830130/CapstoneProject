package com.bojinzhang.android.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by zhangbojin on 29/05/17.
 */

public class DishDaoHelper extends SQLiteOpenHelper {
    public static final String CREATE_Table = "CREATE TABLE Dishes (" +
            "  id int(11) NOT NULL AUTO_INCREMENT," +
            "  categoryId int(11) NOT NULL," +
            "  eName varchar(200) NOT NULL," +
            "  otherName varchar(200) DEFAULT NULL," +
            "  price decimal(10,0) NOT NULL," +
            "  eComment varchar(300) DEFAULT NULL," +
            "  otherComment varchar(300) DEFAULT NULL," +
            "  modifyTime datetime NULL" +
            ") ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=gb2312;";

    private Context mContext;

    public DishDaoHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_Table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
