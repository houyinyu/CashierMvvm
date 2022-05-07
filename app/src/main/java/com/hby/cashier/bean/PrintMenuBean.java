package com.hby.cashier.bean;

import java.util.List;

/**
 * 功能介绍 :打印模板数据
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/2
 */
public class PrintMenuBean {
    //公用
    private int printType;//0是商品 1是交接班
    private int logoID;
    private String shopName;
    private String cashierUser;
    //商品使用
    private String printTime;

    private List<ShopCartBean.ProductListBean> productList;
    private String orderNo;
    private double totalPrice;
    private int totalNum;
    private double promotionPrice;
    private double rebatePrice;
    private double actualPrice;
    private String payType;
    private double giveChange;

    private String contactPhone;
    private String remark;

    //交接班使用
    private String startTime;
    private String endTime;
    private List<ChangeShiftListBean> changeShiftList;

    public int getPrintType() {
        return printType;
    }

    public void setPrintType(int printType) {
        this.printType = printType;
    }

    public int getLogoID() {
        return logoID;
    }

    public void setLogoID(int logoID) {
        this.logoID = logoID;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPrintTime() {
        return printTime;
    }

    public void setPrintTime(String printTime) {
        this.printTime = printTime;
    }

    public String getCashierUser() {
        return cashierUser;
    }

    public void setCashierUser(String cashierUser) {
        this.cashierUser = cashierUser;
    }

    public List<ShopCartBean.ProductListBean> getProductList() {
        return productList;
    }

    public void setProductList(List<ShopCartBean.ProductListBean> productList) {
        this.productList = productList;
    }


    //    public List<ProductListBean> getProductList() {
//        return productList;
//    }
//
//    public void setProductList(List<ProductListBean> productList) {
//        this.productList = productList;
//    }


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public double getRebatePrice() {
        return rebatePrice;
    }

    public void setRebatePrice(double rebatePrice) {
        this.rebatePrice = rebatePrice;
    }

    public double getActualPrice() {
        return actualPrice;
    }

    public void setActualPrice(double actualPrice) {
        this.actualPrice = actualPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public double getGiveChange() {
        return giveChange;
    }

    public void setGiveChange(double giveChange) {
        this.giveChange = giveChange;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<ChangeShiftListBean> getChangeShiftList() {
        return changeShiftList;
    }

    public void setChangeShiftList(List<ChangeShiftListBean> changeShiftList) {
        this.changeShiftList = changeShiftList;
    }

    public static class ProductListBean {
        private String productName;
        private double productNum;
        private double unitPrice;
        private double totalPrice;
        private String unitName;
        private String specName;
        private boolean isWight;
        private boolean isStandard;

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

        public double getUnitPrice() {
            return unitPrice;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public double getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(double totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getUnitName() {
            return unitName;
        }

        public void setUnitName(String unitName) {
            this.unitName = unitName;
        }

        public String getSpecName() {
            return specName;
        }

        public void setSpecName(String specName) {
            this.specName = specName;
        }

        public boolean isWight() {
            return isWight;
        }

        public void setWight(boolean wight) {
            isWight = wight;
        }

        public boolean isStandard() {
            return isStandard;
        }

        public void setStandard(boolean standard) {
            isStandard = standard;
        }
    }

    public static class ChangeShiftListBean {
        private String collectionType;
        private int collectionNum;
        private double collectionPrice;

        public String getCollectionType() {
            return collectionType;
        }

        public void setCollectionType(String collectionType) {
            this.collectionType = collectionType;
        }

        public int getCollectionNum() {
            return collectionNum;
        }

        public void setCollectionNum(int collectionNum) {
            this.collectionNum = collectionNum;
        }

        public double getCollectionPrice() {
            return collectionPrice;
        }

        public void setCollectionPrice(double collectionPrice) {
            this.collectionPrice = collectionPrice;
        }
    }
}
