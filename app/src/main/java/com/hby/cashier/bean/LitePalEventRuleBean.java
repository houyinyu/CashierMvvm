package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :活动规则库
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/15
 */
public class LitePalEventRuleBean extends LitePalSupport {

    @SerializedName("create_time")
    private String createTime;
    @SerializedName("event_id")
    private String eventId;
    @SerializedName("id")
    private Long id;
    @SerializedName("rule_event_id")
    private String ruleEventId;
    @SerializedName("rule_gift_value")
    private Long ruleGiftValue;
    @SerializedName("rule_gift_value_id")
    private String ruleGiftValueId;
    @SerializedName("rule_reach_value")
    private Long ruleReachValue;
    @SerializedName("update_time")
    private String updateTime;


    private  int saveID;
    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleEventId() {
        return ruleEventId;
    }

    public void setRuleEventId(String ruleEventId) {
        this.ruleEventId = ruleEventId;
    }

    public Long getRuleGiftValue() {
        return ruleGiftValue;
    }

    public void setRuleGiftValue(Long ruleGiftValue) {
        this.ruleGiftValue = ruleGiftValue;
    }

    public String getRuleGiftValueId() {
        return ruleGiftValueId;
    }

    public void setRuleGiftValueId(String ruleGiftValueId) {
        this.ruleGiftValueId = ruleGiftValueId;
    }

    public Long getRuleReachValue() {
        return ruleReachValue;
    }

    public void setRuleReachValue(Long ruleReachValue) {
        this.ruleReachValue = ruleReachValue;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
