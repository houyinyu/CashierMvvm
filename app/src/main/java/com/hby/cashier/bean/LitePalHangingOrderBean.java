package com.hby.cashier.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 功能介绍 :挂单的实体类
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/14
 */
public class LitePalHangingOrderBean extends LitePalSupport {
    private String orderNo;
    private String createTime;
    private long createTimeStamp;
    private String totalNum;
    private double totalPrice;
    private String shopCartJson;
    private boolean select;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public String getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(String totalNum) {
        this.totalNum = totalNum;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getShopCartJson() {
        return shopCartJson;
    }

    public void setShopCartJson(String shopCartJson) {
        this.shopCartJson = shopCartJson;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
