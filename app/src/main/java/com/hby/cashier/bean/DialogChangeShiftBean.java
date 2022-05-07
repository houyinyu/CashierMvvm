package com.hby.cashier.bean;

/**
 * 功能介绍 :交接班弹框的Bean类
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/21
 */
public class DialogChangeShiftBean {
    private String ordPaymentMethod;
    private int totalNum;
    private double totalPrice;

    public String getOrdPaymentMethod() {
        return ordPaymentMethod;
    }

    public void setOrdPaymentMethod(String ordPaymentMethod) {
        this.ordPaymentMethod = ordPaymentMethod;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
