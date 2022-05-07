package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :活动商品库
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/15
 */
public class LitePalEventProductBean extends LitePalSupport {

    @SerializedName("event_id")
    private long eventId;
    @SerializedName("product_pro_spe_unit_id")
    private String productProSpeUnitId;
    @SerializedName("product_special_price")
    private long productSpecialPrice;


    private  int saveID;

    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getProductProSpeUnitId() {
        return productProSpeUnitId;
    }

    public void setProductProSpeUnitId(String productProSpeUnitId) {
        this.productProSpeUnitId = productProSpeUnitId;
    }


    public long getProductSpecialPrice() {
        return productSpecialPrice;
    }

    public void setProductSpecialPrice(long productSpecialPrice) {
        this.productSpecialPrice = productSpecialPrice;
    }


}
