package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/22
 */
public class SalesStatisticsBean {

    @SerializedName("result")
    private Integer result;
    @SerializedName("action")
    private String action;
    @SerializedName("message")
    private String message;
    @SerializedName("detail")
    private String detail;
    @SerializedName("resultObject")
    private ResultObjectDTO resultObject;

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public ResultObjectDTO getResultObject() {
        return resultObject;
    }

    public void setResultObject(ResultObjectDTO resultObject) {
        this.resultObject = resultObject;
    }

    public static class ResultObjectDTO {
        @SerializedName("ordPrice")
        private double ordPrice;
        @SerializedName("ordBackPrice")
        private double ordBackPrice;
        @SerializedName("ordNum")
        private Integer ordNum;
        @SerializedName("ali")
        private double ali;
        @SerializedName("wx")
        private double wx;
        @SerializedName("cash")
        private double cash;
        @SerializedName("free")
        private double free;

        public double getOrdPrice() {
            return ordPrice;
        }

        public void setOrdPrice(double ordPrice) {
            this.ordPrice = ordPrice;
        }

        public double getOrdBackPrice() {
            return ordBackPrice;
        }

        public void setOrdBackPrice(double ordBackPrice) {
            this.ordBackPrice = ordBackPrice;
        }

        public Integer getOrdNum() {
            return ordNum;
        }

        public void setOrdNum(Integer ordNum) {
            this.ordNum = ordNum;
        }

        public double getAli() {
            return ali;
        }

        public void setAli(double ali) {
            this.ali = ali;
        }

        public double getWx() {
            return wx;
        }

        public void setWx(double wx) {
            this.wx = wx;
        }

        public double getCash() {
            return cash;
        }

        public void setCash(double cash) {
            this.cash = cash;
        }

        public double getFree() {
            return free;
        }

        public void setFree(double free) {
            this.free = free;
        }
    }
}
