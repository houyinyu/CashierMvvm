package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :活动赠品库
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/15
 */
public class LitePalEventGiftBean extends LitePalSupport {

    @SerializedName("event_id")
    private String eventId;
    @SerializedName("gift_pro_name")
    private String giftProName;
    @SerializedName("gift_pro_spe_unit_id")
    private String giftProSpeUnitId;


    private  int saveID;

    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getGiftProName() {
        return giftProName;
    }

    public void setGiftProName(String giftProName) {
        this.giftProName = giftProName;
    }

    public String getGiftProSpeUnitId() {
        return giftProSpeUnitId;
    }

    public void setGiftProSpeUnitId(String giftProSpeUnitId) {
        this.giftProSpeUnitId = giftProSpeUnitId;
    }
}
