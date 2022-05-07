package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :活动信息库
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/15
 */
public class LitePalEventInfoBean extends LitePalSupport {

    @SerializedName("end_early_time")
    private String endEarlyTime;
    @SerializedName("end_time")
    private String endTime;
    @SerializedName("event_id")
    private long eventId;
    @SerializedName("event_name")
    private String eventName;
    @SerializedName("event_type")
    private int eventType;
    @SerializedName("start_time")
    private String startTime;


    private  int saveID;

    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }

    public String getEndEarlyTime() {
        return endEarlyTime;
    }

    public void setEndEarlyTime(String endEarlyTime) {
        this.endEarlyTime = endEarlyTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
