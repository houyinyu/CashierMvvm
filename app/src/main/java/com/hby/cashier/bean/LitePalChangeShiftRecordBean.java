package com.hby.cashier.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 功能介绍 :交接班记录的bean
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/21
 */
public class LitePalChangeShiftRecordBean extends LitePalSupport {
    private long loginTimeStamp;
    private long outTimeStamp;
    private String userName;
    private String userMid;
    private String orderNoJson;
    private String printJson;
    private String userId;
    private String shopId;
    private String shopName;
    private String syDeviceId;
    private boolean isUpdate;//是否已经上传到了服务器
    private boolean isSelect;//交接班记录那里里选中使用


    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public long getLoginTimeStamp() {
        return loginTimeStamp;
    }

    public void setLoginTimeStamp(long loginTimeStamp) {
        this.loginTimeStamp = loginTimeStamp;
    }

    public long getOutTimeStamp() {
        return outTimeStamp;
    }

    public void setOutTimeStamp(long outTimeStamp) {
        this.outTimeStamp = outTimeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserMid() {
        return userMid;
    }

    public void setUserMid(String userMid) {
        this.userMid = userMid;
    }

    public String getPrintJson() {
        return printJson;
    }

    public void setPrintJson(String printJson) {
        this.printJson = printJson;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSyDeviceId() {
        return syDeviceId;
    }

    public void setSyDeviceId(String syDeviceId) {
        this.syDeviceId = syDeviceId;
    }

    public String getOrderNoJson() {
        return orderNoJson;
    }

    public void setOrderNoJson(String orderNoJson) {
        this.orderNoJson = orderNoJson;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }
}
