package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :分类实体=包含一级和二级
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/13
 */
public class LitePalCategoryBean extends LitePalSupport {
    @SerializedName("classid")
    private String classid;
    @SerializedName("dptclass_name")
    private String dptclassName;
    @SerializedName("dptclass_status")
    private String dptclassStatus;
    @SerializedName("categoryId")
    private String categoryId;
    @SerializedName("mid")
    private String mid;
    @SerializedName("pid")
    private long pid;
    @SerializedName("quote_status")
    private String quoteStatus;
    @SerializedName("sequence")
    private String sequence;
    @SerializedName("shop_id")
    private String shopId;

    private  int saveID;
    private  boolean isSelect;

    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getDptclassName() {
        return dptclassName;
    }

    public void setDptclassName(String dptclassName) {
        this.dptclassName = dptclassName;
    }

    public String getDptclassStatus() {
        return dptclassStatus;
    }

    public void setDptclassStatus(String dptclassStatus) {
        this.dptclassStatus = dptclassStatus;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getQuoteStatus() {
        return quoteStatus;
    }

    public void setQuoteStatus(String quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }
}
