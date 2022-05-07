package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import org.litepal.crud.LitePalSupport;

/**
 * 功能介绍 :配置表
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/15
 */
public class LitePalConfigurationBean extends LitePalSupport {

    @SerializedName("dpt_name")
    private String dptName;
    @SerializedName("id")
    private Long id;
    @SerializedName("shop_name")
    private String shopName;
    @SerializedName("sy_device_id")
    private String syDeviceId;
    @SerializedName("sy_device_model_number")
    private String syDeviceModelNumber;
    @SerializedName("sy_device_sync_freq")
    private int syDeviceSyncFreq;
    @SerializedName("sy_img_ad1")
    private String syImgAd1;
    @SerializedName("sy_img_ad2")
    private String syImgAd2;
    @SerializedName("sy_img_ad3")
    private String syImgAd3;
    @SerializedName("sy_img_ad4")
    private String syImgAd4;
    @SerializedName("sy_img_ad5")
    private String syImgAd5;
    @SerializedName("sy_is_speak")
    private int syIsSpeak;
    @SerializedName("sy_mid")
    private String syMid;
    @SerializedName("sy_mid_shopid")
    private String syMidShopid;
    @SerializedName("sy_not_product_code")
    private int syNotProductCode;
    @SerializedName("sy_second_screen_status")
    private int sySecondScreenStatus;
    @SerializedName("sy_server_id")
    private String syServerId;
    @SerializedName("sy_sign_out_is_transfer")
    private int sySignOutIsTransfer;


    private  int saveID;

    public int getSaveID() {
        return saveID;
    }

    public void setSaveID(int saveID) {
        this.saveID = saveID;
    }
    public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSyDeviceModelNumber() {
        return syDeviceModelNumber;
    }

    public void setSyDeviceModelNumber(String syDeviceModelNumber) {
        this.syDeviceModelNumber = syDeviceModelNumber;
    }

    public int getSyDeviceSyncFreq() {
        return syDeviceSyncFreq;
    }

    public void setSyDeviceSyncFreq(int syDeviceSyncFreq) {
        this.syDeviceSyncFreq = syDeviceSyncFreq;
    }

    public String getSyImgAd1() {
        return syImgAd1;
    }

    public void setSyImgAd1(String syImgAd1) {
        this.syImgAd1 = syImgAd1;
    }

    public String getSyImgAd2() {
        return syImgAd2;
    }

    public void setSyImgAd2(String syImgAd2) {
        this.syImgAd2 = syImgAd2;
    }

    public String getSyImgAd3() {
        return syImgAd3;
    }

    public void setSyImgAd3(String syImgAd3) {
        this.syImgAd3 = syImgAd3;
    }

    public String getSyImgAd4() {
        return syImgAd4;
    }

    public void setSyImgAd4(String syImgAd4) {
        this.syImgAd4 = syImgAd4;
    }

    public String getSyImgAd5() {
        return syImgAd5;
    }

    public void setSyImgAd5(String syImgAd5) {
        this.syImgAd5 = syImgAd5;
    }

    public int getSyIsSpeak() {
        return syIsSpeak;
    }

    public void setSyIsSpeak(int syIsSpeak) {
        this.syIsSpeak = syIsSpeak;
    }

    public String getSyMid() {
        return syMid;
    }

    public void setSyMid(String syMid) {
        this.syMid = syMid;
    }

    public String getSyMidShopid() {
        return syMidShopid;
    }

    public void setSyMidShopid(String syMidShopid) {
        this.syMidShopid = syMidShopid;
    }

    public int getSyNotProductCode() {
        return syNotProductCode;
    }

    public void setSyNotProductCode(int syNotProductCode) {
        this.syNotProductCode = syNotProductCode;
    }

    public int getSySecondScreenStatus() {
        return sySecondScreenStatus;
    }

    public void setSySecondScreenStatus(int sySecondScreenStatus) {
        this.sySecondScreenStatus = sySecondScreenStatus;
    }

    public String getSyServerId() {
        return syServerId;
    }

    public void setSyServerId(String syServerId) {
        this.syServerId = syServerId;
    }

    public int getSySignOutIsTransfer() {
        return sySignOutIsTransfer;
    }

    public void setSySignOutIsTransfer(int sySignOutIsTransfer) {
        this.sySignOutIsTransfer = sySignOutIsTransfer;
    }
}
