package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 功能介绍 :商品里面的层级
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/13
 */
public class ProductGroupBean {

    @SerializedName("groupId")
    private String groupId;
    @SerializedName("groupName")
    private String groupName;
    @SerializedName("onlinePrice")
    private Long onlinePrice;
    @SerializedName("marketPrice")
    private Long marketPrice;
    @SerializedName("IsDefaultGrade")
    private String IsDefaultGrade;
    @SerializedName("groupIsRetail")
    private String groupIsRetail;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getOnlinePrice() {
        return onlinePrice;
    }

    public void setOnlinePrice(Long onlinePrice) {
        this.onlinePrice = onlinePrice;
    }

    public Long getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(Long marketPrice) {
        this.marketPrice = marketPrice;
    }

    public String getIsDefaultGrade() {
        return IsDefaultGrade;
    }

    public void setIsDefaultGrade(String IsDefaultGrade) {
        this.IsDefaultGrade = IsDefaultGrade;
    }

    public String getGroupIsRetail() {
        return groupIsRetail;
    }

    public void setGroupIsRetail(String groupIsRetail) {
        this.groupIsRetail = groupIsRetail;
    }
}
