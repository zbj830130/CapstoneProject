package com.bojinzhang.android.Model;

/**
 * Created by zhangbojin on 25/05/17.
 */

public class CategoryModel {
    private String eName;
    private String oName;
    private int id;

    public String getEName() {
        return eName;
    }

    public String getOName() {
        return oName;
    }

    public int getId() {
        return id;
    }


    public CategoryModel(String eName, String oName, int id) {
        this.eName = eName;
        this.oName = oName;
        this.id = id;
    }
}
