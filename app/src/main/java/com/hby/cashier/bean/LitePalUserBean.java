package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :登录信息
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/13
 */
public class LitePalUserBean extends LitePalSupport {

    @SerializedName("mid")
    private String mid;
    @SerializedName("userAccount")
    private String userAccount;
    @SerializedName("userName")
    private String userName;
    @SerializedName("userPwd")
    private String userPwd;
    @SerializedName("userId")
    private String userId;

    private  int saveID;

    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }
    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPwd() {
        return userPwd;
    }

    public void setUserPwd(String userPwd) {
        this.userPwd = userPwd;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
