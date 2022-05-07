package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 功能介绍 :上传交接班记录所传的Bean
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/21
 */
public class UpdateChangeShiftsBean {

    @SerializedName("changeShifts")
    private ChangeShiftsDTO changeShifts;
    @SerializedName("ordNos")
    private List<String> ordNos;

    public ChangeShiftsDTO getChangeShifts() {
        return changeShifts;
    }

    public void setChangeShifts(ChangeShiftsDTO changeShifts) {
        this.changeShifts = changeShifts;
    }

    public List<String> getOrdNos() {
        return ordNos;
    }

    public void setOrdNos(List<String> ordNos) {
        this.ordNos = ordNos;
    }

    public static class ChangeShiftsDTO {
        @SerializedName("aliOrderAmt")
        private double aliOrderAmt;
        @SerializedName("aliOrderNum")
        private Integer aliOrderNum;
        @SerializedName("cashOrderAmt")
        private double cashOrderAmt;
        @SerializedName("cashOrderNum")
        private Integer cashOrderNum;
        @SerializedName("freeOrderAmt")
        private double freeOrderAmt;
        @SerializedName("freeOrderNum")
        private Integer freeOrderNum;
        @SerializedName("loginTime")
        private String loginTime;
        @SerializedName("logoutTime")
        private String logoutTime;
        @SerializedName("mid")
        private String mid;
        @SerializedName("repayOrderAmt")
        private double repayOrderAmt;
        @SerializedName("repayOrderNum")
        private Integer repayOrderNum;
        @SerializedName("shopId")
        private String shopId;
        @SerializedName("shopName")
        private String shopName;
        @SerializedName("syDeviceId")
        private String syDeviceId;
        @SerializedName("userId")
        private String userId;
        @SerializedName("userName")
        private String userName;
        @SerializedName("wxOrderAmt")
        private double wxOrderAmt;
        @SerializedName("wxOrderNum")
        private Integer wxOrderNum;

        public double getAliOrderAmt() {
            return aliOrderAmt;
        }

        public void setAliOrderAmt(double aliOrderAmt) {
            this.aliOrderAmt = aliOrderAmt;
        }

        public Integer getAliOrderNum() {
            return aliOrderNum;
        }

        public void setAliOrderNum(Integer aliOrderNum) {
            this.aliOrderNum = aliOrderNum;
        }

        public double getCashOrderAmt() {
            return cashOrderAmt;
        }

        public void setCashOrderAmt(double cashOrderAmt) {
            this.cashOrderAmt = cashOrderAmt;
        }

        public Integer getCashOrderNum() {
            return cashOrderNum;
        }

        public void setCashOrderNum(Integer cashOrderNum) {
            this.cashOrderNum = cashOrderNum;
        }


        public double getFreeOrderAmt() {
            return freeOrderAmt;
        }

        public void setFreeOrderAmt(double freeOrderAmt) {
            this.freeOrderAmt = freeOrderAmt;
        }

        public Integer getFreeOrderNum() {
            return freeOrderNum;
        }

        public void setFreeOrderNum(Integer freeOrderNum) {
            this.freeOrderNum = freeOrderNum;
        }


        public String getLoginTime() {
            return loginTime;
        }

        public void setLoginTime(String loginTime) {
            this.loginTime = loginTime;
        }

        public String getLogoutTime() {
            return logoutTime;
        }

        public void setLogoutTime(String logoutTime) {
            this.logoutTime = logoutTime;
        }

        public String getMid() {
            return mid;
        }

        public void setMid(String mid) {
            this.mid = mid;
        }

        public double getRepayOrderAmt() {
            return repayOrderAmt;
        }

        public void setRepayOrderAmt(double repayOrderAmt) {
            this.repayOrderAmt = repayOrderAmt;
        }

        public Integer getRepayOrderNum() {
            return repayOrderNum;
        }

        public void setRepayOrderNum(Integer repayOrderNum) {
            this.repayOrderNum = repayOrderNum;
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


        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public double getWxOrderAmt() {
            return wxOrderAmt;
        }

        public void setWxOrderAmt(double wxOrderAmt) {
            this.wxOrderAmt = wxOrderAmt;
        }

        public Integer getWxOrderNum() {
            return wxOrderNum;
        }

        public void setWxOrderNum(Integer wxOrderNum) {
            this.wxOrderNum = wxOrderNum;
        }
    }
}
