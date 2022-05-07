package com.hby.cashier.bean;

import java.util.List;

/**
 * 功能介绍 :购物车商品数据
 * 调用方式 :新创建地点-、搜索/扫码加入购物车、直接点击加入购物车、点击称重商品加入购物车、无码商品加入购物车
 * （全局搜索：获取购物车数据-三个地方：ShopCartDataView（搜索、扫码、点击加入购物车）、WeighProductDialog、CodelessProductDialog）
 * 作   者 :Hyy
 * 创建时间 :2022/4/2
 */
public class ShopCartBean {
    private long eventID;
    private int eventType;
    private List<ProductListBean> productList;

    public long getEventID() {
        return eventID;
    }

    public void setEventID(long eventID) {
        this.eventID = eventID;
    }

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public List<ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListBean> productList) {
        this.productList = productList;
    }

    public static class ProductListBean {
        private String productName;
        private double productNum;
        private double productPrice;
        private String productID;
        private String productImage;
        private String specificationUnitID;//三合一ID
        private String shopID;
        private String specName;
        private String specID;
        private String unitName;
        private String unitID;
        private String barCode;
        private String brandID;
        private String brandName;
        private String manufacturer;
        private long eventID;//活动ID
        private int eventType;
        private int isInBulk;//是否称重商品
        private String isMinUnit;;//是否最小计量单位
        private String isStandard;//是否标品
        private double onlineRebate;//返利
        private double onlineRebateUnit;

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public double getProductNum() {
            return productNum;
        }

        public void setProductNum(double productNum) {
            this.productNum = productNum;
        }

        public double getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(double productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductID() {
            return productID;
        }

        public void setProductID(String productID) {
            this.productID = productID;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getSpecificationUnitID() {
            return specificationUnitID;
        }

        public void setSpecificationUnitID(String specificationUnitID) {
            this.specificationUnitID = specificationUnitID;
        }

        public String getShopID() {
            return shopID;
        }

        public void setShopID(String shopID) {
            this.shopID = shopID;
        }

        public String getSpecName() {
            return specName;
        }

        public void setSpecName(String specName) {
            this.specName = specName;
        }

        public String getSpecID() {
            return specID;
        }

        public void setSpecID(String specID) {
            this.specID = specID;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getUnitID() {
            return unitID;
        }

        public void setUnitID(String unitID) {
            this.unitID = unitID;
        }

        public String getBarCode() {
            return barCode;
        }

        public void setBarCode(String barCode) {
            this.barCode = barCode;
        }

        public String getBrandID() {
            return brandID;
        }

        public void setBrandID(String brandID) {
            this.brandID = brandID;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getManufacturer() {
            return manufacturer;
        }

        public void setManufacturer(String manufacturer) {
            this.manufacturer = manufacturer;
        }

        public long getEventID() {
            return eventID;
        }

        public void setEventID(long eventID) {
            this.eventID = eventID;
        }

        public int getEventType() {
            return eventType;
        }

        public void setEventType(int eventType) {
            this.eventType = eventType;
        }

        public int getIsInBulk() {
            return isInBulk;
        }

        public void setIsInBulk(int isInBulk) {
            this.isInBulk = isInBulk;
        }

        public String getIsMinUnit() {
            return isMinUnit;
        }

        public void setIsMinUnit(String isMinUnit) {
            this.isMinUnit = isMinUnit;
        }

        public String getIsStandard() {
            return isStandard;
        }

        public void setIsStandard(String isStandard) {
            this.isStandard = isStandard;
        }

        public double getOnlineRebate() {
            return onlineRebate;
        }

        public void setOnlineRebate(double onlineRebate) {
            this.onlineRebate = onlineRebate;
        }

        public double getOnlineRebateUnit() {
            return onlineRebateUnit;
        }

        public void setOnlineRebateUnit(double onlineRebateUnit) {
            this.onlineRebateUnit = onlineRebateUnit;
        }
    }

}
