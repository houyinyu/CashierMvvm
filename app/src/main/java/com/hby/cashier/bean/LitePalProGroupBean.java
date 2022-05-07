package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :分类和商品的中间表
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/13
 */
public class LitePalProGroupBean extends LitePalSupport {

    @SerializedName("create_time")
    private String createTime;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("id")
    private long id;
    @SerializedName("pro_id")
    private String proId;
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

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
