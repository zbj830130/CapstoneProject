package com.bojinzhang.android.Model;

/**
 * Created by zhangbojin on 29/05/17.
 */

public class DishModel {
    private int id;
    private int categoryId;
    private String eName;
    private String oName;
    private double price;
    private String eComment;
    private String otherComment;
    private String cEName;
    private String cOName;
    private String modifyTime;
    private String imgUrl;
    private int qty;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setEName(String eName) {
        this.eName = eName;
    }

    public String getEName() {
        return eName;
    }

    public void setOName(String otherName) {
        this.oName = otherName;
    }

    public String getOName() {
        return oName;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public void setEComment(String eComment) {
        this.eComment = eComment;
    }

    public String getEComment() {
        return eComment;
    }

    public void setOComment(String otherComment) {
        this.otherComment = otherComment;
    }

    public String getOComment() {
        return otherComment;
    }

    public void setCEName(String cEName) {
        this.cEName = cEName;
    }

    public String getCEName() {
        return cEName;
    }

    public void setCOName(String cOName) {
        this.cOName = cOName;
    }

    public String getCOName() {
        return cOName;
    }

    public void setModifyTime(String modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getModifyTime() {
        return modifyTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
