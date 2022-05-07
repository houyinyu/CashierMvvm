package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 功能介绍 :创建订单接口返回的数据
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/20
 */
public class CreateOrderReturnBean {

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
        @SerializedName("message")
        private String message;
        @SerializedName("checkoutPaymentResult")
        private CheckoutPaymentResultDTO checkoutPaymentResult;
        @SerializedName("isSuccess")
        private Integer isSuccess;
        @SerializedName("orderCreateTime")
        private String orderCreateTime;
        @SerializedName("orderNo")
        private List<String> orderNo;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public CheckoutPaymentResultDTO getCheckoutPaymentResult() {
            return checkoutPaymentResult;
        }

        public void setCheckoutPaymentResult(CheckoutPaymentResultDTO checkoutPaymentResult) {
            this.checkoutPaymentResult = checkoutPaymentResult;
        }

        public Integer getIsSuccess() {
            return isSuccess;
        }

        public void setIsSuccess(Integer isSuccess) {
            this.isSuccess = isSuccess;
        }

        public String getOrderCreateTime() {
            return orderCreateTime;
        }

        public void setOrderCreateTime(String orderCreateTime) {
            this.orderCreateTime = orderCreateTime;
        }

        public List<String> getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(List<String> orderNo) {
            this.orderNo = orderNo;
        }

        public static class CheckoutPaymentResultDTO {
            @SerializedName("status")
            private Integer status;
            @SerializedName("message")
            private Object message;
            @SerializedName("paymentChannl")
            private Object paymentChannl;
            @SerializedName("paymentTypeEnum")
            private Object paymentTypeEnum;
            @SerializedName("orderOutNum")
            private Object orderOutNum;
            @SerializedName("data")
            private String data;
            @SerializedName("price")
            private Double price;
            @SerializedName("rebatePrice")
            private Double rebatePrice;
            @SerializedName("orderNo")
            private String orderNo;

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public Object getMessage() {
                return message;
            }

            public void setMessage(Object message) {
                this.message = message;
            }

            public Object getPaymentChannl() {
                return paymentChannl;
            }

            public void setPaymentChannl(Object paymentChannl) {
                this.paymentChannl = paymentChannl;
            }

            public Object getPaymentTypeEnum() {
                return paymentTypeEnum;
            }

            public void setPaymentTypeEnum(Object paymentTypeEnum) {
                this.paymentTypeEnum = paymentTypeEnum;
            }

            public Object getOrderOutNum() {
                return orderOutNum;
            }

            public void setOrderOutNum(Object orderOutNum) {
                this.orderOutNum = orderOutNum;
            }

            public String getData() {
                return data;
            }

            public void setData(String data) {
                this.data = data;
            }

            public Double getPrice() {
                return price;
            }

            public void setPrice(Double price) {
                this.price = price;
            }

            public Double getRebatePrice() {
                return rebatePrice;
            }

            public void setRebatePrice(Double rebatePrice) {
                this.rebatePrice = rebatePrice;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }
        }
    }
}
