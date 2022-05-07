package com.hby.cashier.bean;

import org.litepal.crud.LitePalSupport;

import java.util.List;

/**
 * 功能介绍:本地订单存储的
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/25 9:17
 * 最后编辑: 2020/12/25 - Hyy
 *
 * @author HouYinYu
 */
public class LitePalOrderDetailsBean extends LitePalSupport {
    private String ordNo;
    private boolean isSyOrder;//是否收银订单
    private String syDeviceId;//收银设备ID
    private String ordResource;
    private String ordSellerShopId;
    private String ordPaymentMethod;
    private String ordPaymentType;
    private int ordType;
    private String ordComment;
    private List<OrderProductAoListBean> orderProductAoList;
    //
    private String orderProductListJson;
    private double orderPrice;
    private double promotionPrice;
    private double rebatePrice;
    private double actualPrice;
    private double changePrice;
    private long createTimeStamp;
    private int paymentStatus;//1是已支付，0是未支付
    private boolean isUpdate;//是否已经上传到了服务器

    public String getOrderProductListJson() {
        return orderProductListJson;
    }

    public void setOrderProductListJson(String orderProductListJson) {
        this.orderProductListJson = orderProductListJson;
    }

    public double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(double orderPrice) {
        this.orderPrice = orderPrice;
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

    public double getChangePrice() {
        return changePrice;
    }

    public void setChangePrice(double changePrice) {
        this.changePrice = changePrice;
    }

    public long getCreateTimeStamp() {
        return createTimeStamp;
    }

    public void setCreateTimeStamp(long createTimeStamp) {
        this.createTimeStamp = createTimeStamp;
    }

    public int getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public boolean isUpdate() {
        return isUpdate;
    }

    public void setUpdate(boolean update) {
        isUpdate = update;
    }

    public String getOrdNo() {
        return ordNo;
    }

    public void setOrdNo(String ordNo) {
        this.ordNo = ordNo;
    }

    public boolean isSyOrder() {
        return isSyOrder;
    }

    public void setSyOrder(boolean syOrder) {
        isSyOrder = syOrder;
    }

    public String getSyDeviceId() {
        return syDeviceId;
    }

    public void setSyDeviceId(String syDeviceId) {
        this.syDeviceId = syDeviceId;
    }

    public String getOrdResource() {
        return ordResource;
    }

    public void setOrdResource(String ordResource) {
        this.ordResource = ordResource;
    }

    public String getOrdSellerShopId() {
        return ordSellerShopId;
    }

    public void setOrdSellerShopId(String ordSellerShopId) {
        this.ordSellerShopId = ordSellerShopId;
    }

    public String getOrdPaymentMethod() {
        return ordPaymentMethod;
    }

    public void setOrdPaymentMethod(String ordPaymentMethod) {
        this.ordPaymentMethod = ordPaymentMethod;
    }

    public String getOrdPaymentType() {
        return ordPaymentType;
    }

    public void setOrdPaymentType(String ordPaymentType) {
        this.ordPaymentType = ordPaymentType;
    }

    public int getOrdType() {
        return ordType;
    }

    public void setOrdType(int ordType) {
        this.ordType = ordType;
    }

    public String getOrdComment() {
        return ordComment;
    }

    public void setOrdComment(String ordComment) {
        this.ordComment = ordComment;
    }

    public List<OrderProductAoListBean> getOrderProductAoList() {
        return orderProductAoList;
    }

    public void setOrderProductAoList(List<OrderProductAoListBean> orderProductAoList) {
        this.orderProductAoList = orderProductAoList;
    }

    public static class OrderProductAoListBean {
        private String ordProductBarCode;
        private String ordProductBrandId;
        private String ordProductBrandName;
        private long ordProductEventId;
        private String ordProductId;
        private String ordProductImage;
        private String ordProductManufacturer;
        private String ordProductName;
        private double ordProductNum;
        private double ordProductPrice;
        private String ordProductProSpeUnitId;
        private String ordProductShopId;
        private String ordProductSpecId;
        private String ordProductSpecName;
        private String ordProductUnitId;
        private String ordProductUnitName;
        private String isStandardProduct;

        private  int isInBulk;//是否称重商品

        public int getIsInBulk() {
            return isInBulk;
        }

        public void setIsInBulk(int isInBulk) {
            this.isInBulk = isInBulk;
        }

        public String getOrdProductBarCode() {
            return ordProductBarCode;
        }

        public void setOrdProductBarCode(String ordProductBarCode) {
            this.ordProductBarCode = ordProductBarCode;
        }

        public String getOrdProductBrandId() {
            return ordProductBrandId;
        }

        public void setOrdProductBrandId(String ordProductBrandId) {
            this.ordProductBrandId = ordProductBrandId;
        }

        public String getOrdProductBrandName() {
            return ordProductBrandName;
        }

        public void setOrdProductBrandName(String ordProductBrandName) {
            this.ordProductBrandName = ordProductBrandName;
        }

        public long getOrdProductEventId() {
            return ordProductEventId;
        }

        public void setOrdProductEventId(long ordProductEventId) {
            this.ordProductEventId = ordProductEventId;
        }

        public String getOrdProductId() {
            return ordProductId;
        }

        public void setOrdProductId(String ordProductId) {
            this.ordProductId = ordProductId;
        }

        public String getOrdProductImage() {
            return ordProductImage;
        }

        public void setOrdProductImage(String ordProductImage) {
            this.ordProductImage = ordProductImage;
        }

        public String getOrdProductManufacturer() {
            return ordProductManufacturer;
        }

        public void setOrdProductManufacturer(String ordProductManufacturer) {
            this.ordProductManufacturer = ordProductManufacturer;
        }

        public String getOrdProductName() {
            return ordProductName;
        }

        public void setOrdProductName(String ordProductName) {
            this.ordProductName = ordProductName;
        }

        public double getOrdProductNum() {
            return ordProductNum;
        }

        public void setOrdProductNum(double ordProductNum) {
            this.ordProductNum = ordProductNum;
        }

        public double getOrdProductPrice() {
            return ordProductPrice;
        }

        public void setOrdProductPrice(double ordProductPrice) {
            this.ordProductPrice = ordProductPrice;
        }

        public String getOrdProductProSpeUnitId() {
            return ordProductProSpeUnitId;
        }

        public void setOrdProductProSpeUnitId(String ordProductProSpeUnitId) {
            this.ordProductProSpeUnitId = ordProductProSpeUnitId;
        }

        public String getOrdProductShopId() {
            return ordProductShopId;
        }

        public void setOrdProductShopId(String ordProductShopId) {
            this.ordProductShopId = ordProductShopId;
        }

        public String getOrdProductSpecId() {
            return ordProductSpecId;
        }

        public void setOrdProductSpecId(String ordProductSpecId) {
            this.ordProductSpecId = ordProductSpecId;
        }

        public String getOrdProductSpecName() {
            return ordProductSpecName;
        }

        public void setOrdProductSpecName(String ordProductSpecName) {
            this.ordProductSpecName = ordProductSpecName;
        }

        public String getOrdProductUnitId() {
            return ordProductUnitId;
        }

        public void setOrdProductUnitId(String ordProductUnitId) {
            this.ordProductUnitId = ordProductUnitId;
        }

        public String getOrdProductUnitName() {
            return ordProductUnitName;
        }

        public void setOrdProductUnitName(String ordProductUnitName) {
            this.ordProductUnitName = ordProductUnitName;
        }

        public String getIsStandardProduct() {
            return isStandardProduct;
        }

        public void setIsStandardProduct(String isStandardProduct) {
            this.isStandardProduct = isStandardProduct;
        }
    }
}
