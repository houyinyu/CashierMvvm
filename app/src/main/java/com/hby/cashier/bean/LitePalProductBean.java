package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :商品基础实体类
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/13
 */
public class LitePalProductBean extends LitePalSupport {

    @SerializedName("bar_code")
    private String barCode;
    @SerializedName("grade_price")
    private String gradePrice;
    @SerializedName("group_id")
    private String groupId;
    @SerializedName("group_online_rebate")
    private double groupOnlineRebate;
    @SerializedName("group_online_rebate_unit")
    private double groupOnlineRebateUnit;
    @SerializedName("img_url")
    private String imgUrl;
    @SerializedName("is_in_bulk")
    private int isInBulk;
    @SerializedName("is_min_unit")
    private String isMinUnit;
    @SerializedName("is_standard_product")
    private String isStandardProduct;
    @SerializedName("mid")
    private String mid;
    @SerializedName("pro_brand_id")
    private String proBrandId;
    @SerializedName("pro_brand_name")
    private String proBrandName;
    @SerializedName("pro_brand_name_spell")
    private String proBrandNameSpell;
    @SerializedName("pro_id")
    private String proId;
    @SerializedName("pro_name")
    private String proName;
    @SerializedName("pro_name_spell")
    private String proNameSpell;
    @SerializedName("pro_specification_unit_id")
    private String proSpecificationUnitId;
    @SerializedName("shop_id")
    private String shopId;
    @SerializedName("pro_manufacturers_name")
    private String proManufacturersName;
    @SerializedName("specification")
    private String specification;
    @SerializedName("specification_id")
    private String specificationId;
    @SerializedName("unit_name")
    private String unitName;
    @SerializedName("unit_id")
    private String unitId;

    private double specialPrice;//特价

    private long eventID;//活动ID
    private int eventType;

    private int saveID;

    public double getSpecialPrice() {
        return specialPrice;
    }

    public void setSpecialPrice(double specialPrice) {
        this.specialPrice = specialPrice;
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

    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGradePrice() {
        return gradePrice;
    }

    public void setGradePrice(String gradePrice) {
        this.gradePrice = gradePrice;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public double getGroupOnlineRebate() {
        return groupOnlineRebate;
    }

    public void setGroupOnlineRebate(double groupOnlineRebate) {
        this.groupOnlineRebate = groupOnlineRebate;
    }

    public double getGroupOnlineRebateUnit() {
        return groupOnlineRebateUnit;
    }

    public void setGroupOnlineRebateUnit(double groupOnlineRebateUnit) {
        this.groupOnlineRebateUnit = groupOnlineRebateUnit;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
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

    public String getIsStandardProduct() {
        return isStandardProduct;
    }

    public void setIsStandardProduct(String isStandardProduct) {
        this.isStandardProduct = isStandardProduct;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getProBrandId() {
        return proBrandId;
    }

    public void setProBrandId(String proBrandId) {
        this.proBrandId = proBrandId;
    }

    public String getProBrandName() {
        return proBrandName;
    }

    public void setProBrandName(String proBrandName) {
        this.proBrandName = proBrandName;
    }

    public String getProBrandNameSpell() {
        return proBrandNameSpell;
    }

    public void setProBrandNameSpell(String proBrandNameSpell) {
        this.proBrandNameSpell = proBrandNameSpell;
    }

    public String getProId() {
        return proId;
    }

    public void setProId(String proId) {
        this.proId = proId;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProNameSpell() {
        return proNameSpell;
    }

    public void setProNameSpell(String proNameSpell) {
        this.proNameSpell = proNameSpell;
    }

    public String getProSpecificationUnitId() {
        return proSpecificationUnitId;
    }

    public void setProSpecificationUnitId(String proSpecificationUnitId) {
        this.proSpecificationUnitId = proSpecificationUnitId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getProManufacturersName() {
        return proManufacturersName;
    }

    public void setProManufacturersName(String proManufacturersName) {
        this.proManufacturersName = proManufacturersName;
    }

    public String getSpecification() {
        return specification;
    }

    public void setSpecification(String specification) {
        this.specification = specification;
    }

    public String getSpecificationId() {
        return specificationId;
    }

    public void setSpecificationId(String specificationId) {
        this.specificationId = specificationId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }
}
