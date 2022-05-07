package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/20
 */
public class PollingOrderStatusBean {

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
        @SerializedName("paymentMethod")
        private String paymentMethod;
        @SerializedName("isPayed")
        private Integer isPayed;

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public Integer getIsPayed() {
            return isPayed;
        }

        public void setIsPayed(Integer isPayed) {
            this.isPayed = isPayed;
        }
    }
}
