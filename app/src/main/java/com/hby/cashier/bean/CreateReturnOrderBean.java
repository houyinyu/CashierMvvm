package com.hby.cashier.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能介绍:创建退单使用的实体类
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2021/1/15 20:12
 * 最后编辑: 2021/1/15 - Hyy
 *
 * @author HouYinYu
 */
public class CreateReturnOrderBean {

    private String ordBackComment;
    private String ordNo;
    private List<OrderBackProductListBean> orderBackProductList = new ArrayList<>();

    public String getOrdBackComment() {
        return ordBackComment;
    }

    public void setOrdBackComment(String ordBackComment) {
        this.ordBackComment = ordBackComment;
    }

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    public List<OrderBackProductListBean> getOrderBackProductList() {
        return orderBackProductList;
    }

    public void setOrderBackProductList(List<OrderBackProductListBean> orderBackProductList) {
        this.orderBackProductList = orderBackProductList;
    }

    public static class OrderBackProductListBean {
        private String id;
        private String ordBackNo;
        private String ordBackProductBrandId;
        private String ordBackProductBrandName;
        private String ordBackProductId;
        private String ordBackProductImage;
        private String ordBackProductManufacturer;
        private String ordBackProductName;
        private double ordBackProductNum;
        private double ordBackProductPriceBack;
        private double ordBackProductPriceOnline;
        private double ordBackProductPricePay;
        private String ordBackProductProSpeUnitId;
        private String ordBackProductSpecId;
        private String ordBackProductSpecName;
        private String ordBackProductUnitId;
        private String ordBackProductUnitName;
        private String ordBackShopId;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrdBackNo() {
            return ordBackNo;
        }

        public void setOrdBackNo(String ordBackNo) {
            this.ordBackNo = ordBackNo;
        }

        public String getOrdBackProductBrandId() {
            return ordBackProductBrandId;
        }

        public void setOrdBackProductBrandId(String ordBackProductBrandId) {
            this.ordBackProductBrandId = ordBackProductBrandId;
        }

        public String getOrdBackProductBrandName() {
            return ordBackProductBrandName;
        }

        public void setOrdBackProductBrandName(String ordBackProductBrandName) {
            this.ordBackProductBrandName = ordBackProductBrandName;
        }

        public String getOrdBackProductId() {
            return ordBackProductId;
        }

        public void setOrdBackProductId(String ordBackProductId) {
            this.ordBackProductId = ordBackProductId;
        }

        public String getOrdBackProductImage() {
            return ordBackProductImage;
        }

        public void setOrdBackProductImage(String ordBackProductImage) {
            this.ordBackProductImage = ordBackProductImage;
        }

        public String getOrdBackProductManufacturer() {
            return ordBackProductManufacturer;
        }

        public void setOrdBackProductManufacturer(String ordBackProductManufacturer) {
            this.ordBackProductManufacturer = ordBackProductManufacturer;
        }

        public String getOrdBackProductName() {
            return ordBackProductName;
        }

        public void setOrdBackProductName(String ordBackProductName) {
            this.ordBackProductName = ordBackProductName;
        }

        public double getOrdBackProductNum() {
            return ordBackProductNum;
        }

        public void setOrdBackProductNum(double ordBackProductNum) {
            this.ordBackProductNum = ordBackProductNum;
        }

        public double getOrdBackProductPriceBack() {
            return ordBackProductPriceBack;
        }

        public void setOrdBackProductPriceBack(double ordBackProductPriceBack) {
            this.ordBackProductPriceBack = ordBackProductPriceBack;
        }

        public double getOrdBackProductPriceOnline() {
            return ordBackProductPriceOnline;
        }

        public void setOrdBackProductPriceOnline(double ordBackProductPriceOnline) {
            this.ordBackProductPriceOnline = ordBackProductPriceOnline;
        }

        public double getOrdBackProductPricePay() {
            return ordBackProductPricePay;
        }

        public void setOrdBackProductPricePay(double ordBackProductPricePay) {
            this.ordBackProductPricePay = ordBackProductPricePay;
        }

        public String getOrdBackProductProSpeUnitId() {
            return ordBackProductProSpeUnitId;
        }

        public void setOrdBackProductProSpeUnitId(String ordBackProductProSpeUnitId) {
            this.ordBackProductProSpeUnitId = ordBackProductProSpeUnitId;
        }

        public String getOrdBackProductSpecId() {
            return ordBackProductSpecId;
        }

        public void setOrdBackProductSpecId(String ordBackProductSpecId) {
            this.ordBackProductSpecId = ordBackProductSpecId;
        }

        public String getOrdBackProductSpecName() {
            return ordBackProductSpecName;
        }

        public void setOrdBackProductSpecName(String ordBackProductSpecName) {
            this.ordBackProductSpecName = ordBackProductSpecName;
        }

        public String getOrdBackProductUnitId() {
            return ordBackProductUnitId;
        }

        public void setOrdBackProductUnitId(String ordBackProductUnitId) {
            this.ordBackProductUnitId = ordBackProductUnitId;
        }

        public String getOrdBackProductUnitName() {
            return ordBackProductUnitName;
        }

        public void setOrdBackProductUnitName(String ordBackProductUnitName) {
            this.ordBackProductUnitName = ordBackProductUnitName;
        }

        public String getOrdBackShopId() {
            return ordBackShopId;
        }

        public void setOrdBackShopId(String ordBackShopId) {
            this.ordBackShopId = ordBackShopId;
        }
    }
}
