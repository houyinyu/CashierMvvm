package com.hby.cashier.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 功能介绍 :销售记录-订单详情数据
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/22
 */
public class RecordOrderDetailsBean {
    @SerializedName("action")
    private String action;
    @SerializedName("detail")
    private String detail;
    @SerializedName("message")
    private String message;
    @SerializedName("result")
    private Integer result;
    @SerializedName("resultObject")
    private ResultObjectDTO resultObject;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public ResultObjectDTO getResultObject() {
        return resultObject;
    }

    public void setResultObject(ResultObjectDTO resultObject) {
        this.resultObject = resultObject;
    }

    public static class ResultObjectDTO {
        @SerializedName("countdown")
        private Integer countdown;
        @SerializedName("isJcUser")
        private Integer isJcUser;
        @SerializedName("ordBackUnderWay")
        private String ordBackUnderWay;
        @SerializedName("ordRetailOpration")
        private OrdRetailOprationDTO ordRetailOpration;
        @SerializedName("orderDepositVo")
        private OrderDepositVoDTO orderDepositVo;
        @SerializedName("orderMainVo")
        private OrderMainVoDTO orderMainVo;
        @SerializedName("shopInfo")
        private ShopInfoDTO shopInfo;
        @SerializedName("showCountdown")
        private Integer showCountdown;
        @SerializedName("orderGiftVoList")
        private List<OrderGiftVoListDTO> orderGiftVoList;
        @SerializedName("orderProductVo")
        private List<OrderProductVoDTO> orderProductVo;
        @SerializedName("pdLogisticsTrilateralVoList")
        private List<PdLogisticsTrilateralVoListDTO> pdLogisticsTrilateralVoList;
        @SerializedName("pdLogisticsVoList")
        private List<PdLogisticsVoListDTO> pdLogisticsVoList;
        @SerializedName("stoOutNoList")
        private List<?> stoOutNoList;

        public Integer getCountdown() {
            return countdown;
        }

        public void setCountdown(Integer countdown) {
            this.countdown = countdown;
        }

        public Integer getIsJcUser() {
            return isJcUser;
        }

        public void setIsJcUser(Integer isJcUser) {
            this.isJcUser = isJcUser;
        }

        public String getOrdBackUnderWay() {
            return ordBackUnderWay;
        }

        public void setOrdBackUnderWay(String ordBackUnderWay) {
            this.ordBackUnderWay = ordBackUnderWay;
        }

        public OrdRetailOprationDTO getOrdRetailOpration() {
            return ordRetailOpration;
        }

        public void setOrdRetailOpration(OrdRetailOprationDTO ordRetailOpration) {
            this.ordRetailOpration = ordRetailOpration;
        }

        public OrderDepositVoDTO getOrderDepositVo() {
            return orderDepositVo;
        }

        public void setOrderDepositVo(OrderDepositVoDTO orderDepositVo) {
            this.orderDepositVo = orderDepositVo;
        }

        public OrderMainVoDTO getOrderMainVo() {
            return orderMainVo;
        }

        public void setOrderMainVo(OrderMainVoDTO orderMainVo) {
            this.orderMainVo = orderMainVo;
        }

        public ShopInfoDTO getShopInfo() {
            return shopInfo;
        }

        public void setShopInfo(ShopInfoDTO shopInfo) {
            this.shopInfo = shopInfo;
        }

        public Integer getShowCountdown() {
            return showCountdown;
        }

        public void setShowCountdown(Integer showCountdown) {
            this.showCountdown = showCountdown;
        }

        public List<OrderGiftVoListDTO> getOrderGiftVoList() {
            return orderGiftVoList;
        }

        public void setOrderGiftVoList(List<OrderGiftVoListDTO> orderGiftVoList) {
            this.orderGiftVoList = orderGiftVoList;
        }

        public List<OrderProductVoDTO> getOrderProductVo() {
            return orderProductVo;
        }

        public void setOrderProductVo(List<OrderProductVoDTO> orderProductVo) {
            this.orderProductVo = orderProductVo;
        }

        public List<PdLogisticsTrilateralVoListDTO> getPdLogisticsTrilateralVoList() {
            return pdLogisticsTrilateralVoList;
        }

        public void setPdLogisticsTrilateralVoList(List<PdLogisticsTrilateralVoListDTO> pdLogisticsTrilateralVoList) {
            this.pdLogisticsTrilateralVoList = pdLogisticsTrilateralVoList;
        }

        public List<PdLogisticsVoListDTO> getPdLogisticsVoList() {
            return pdLogisticsVoList;
        }

        public void setPdLogisticsVoList(List<PdLogisticsVoListDTO> pdLogisticsVoList) {
            this.pdLogisticsVoList = pdLogisticsVoList;
        }

        public List<?> getStoOutNoList() {
            return stoOutNoList;
        }

        public void setStoOutNoList(List<?> stoOutNoList) {
            this.stoOutNoList = stoOutNoList;
        }

        public static class OrdRetailOprationDTO {
            @SerializedName("createTime")
            private String createTime;
            @SerializedName("dptId")
            private Integer dptId;
            @SerializedName("id")
            private Integer id;
            @SerializedName("mid")
            private Integer mid;
            @SerializedName("mname")
            private String mname;
            @SerializedName("onlinePaymentRebate")
            private Integer onlinePaymentRebate;
            @SerializedName("outboundOperation")
            private Integer outboundOperation;
            @SerializedName("printOperation")
            private Integer printOperation;
            @SerializedName("shopId")
            private Integer shopId;
            @SerializedName("shopName")
            private String shopName;
            @SerializedName("updateTime")
            private String updateTime;

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public Integer getDptId() {
                return dptId;
            }

            public void setDptId(Integer dptId) {
                this.dptId = dptId;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getMid() {
                return mid;
            }

            public void setMid(Integer mid) {
                this.mid = mid;
            }

            public String getMname() {
                return mname;
            }

            public void setMname(String mname) {
                this.mname = mname;
            }

            public Integer getOnlinePaymentRebate() {
                return onlinePaymentRebate;
            }

            public void setOnlinePaymentRebate(Integer onlinePaymentRebate) {
                this.onlinePaymentRebate = onlinePaymentRebate;
            }

            public Integer getOutboundOperation() {
                return outboundOperation;
            }

            public void setOutboundOperation(Integer outboundOperation) {
                this.outboundOperation = outboundOperation;
            }

            public Integer getPrintOperation() {
                return printOperation;
            }

            public void setPrintOperation(Integer printOperation) {
                this.printOperation = printOperation;
            }

            public Integer getShopId() {
                return shopId;
            }

            public void setShopId(Integer shopId) {
                this.shopId = shopId;
            }

            public String getShopName() {
                return shopName;
            }

            public void setShopName(String shopName) {
                this.shopName = shopName;
            }

            public String getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(String updateTime) {
                this.updateTime = updateTime;
            }
        }

        public static class OrderDepositVoDTO {
            @SerializedName("finalPayment")
            private Integer finalPayment;
            @SerializedName("id")
            private Integer id;
            @SerializedName("ordDepositAmt")
            private Integer ordDepositAmt;
            @SerializedName("ordDepositCreateTime")
            private String ordDepositCreateTime;
            @SerializedName("ordDepositIsPayed")
            private Integer ordDepositIsPayed;
            @SerializedName("ordDepositMid")
            private Integer ordDepositMid;
            @SerializedName("ordDepositOrderNo")
            private String ordDepositOrderNo;
            @SerializedName("ordDepositPayMid")
            private Integer ordDepositPayMid;
            @SerializedName("ordDepositPayTime")
            private String ordDepositPayTime;
            @SerializedName("ordDepositPayWxUnionid")
            private String ordDepositPayWxUnionid;
            @SerializedName("ordDepositPaymentMethod")
            private String ordDepositPaymentMethod;
            @SerializedName("ordDepositUpdateTime")
            private String ordDepositUpdateTime;
            @SerializedName("ordDepositWithdrawStatus")
            private Integer ordDepositWithdrawStatus;
            @SerializedName("ordDepositWxName")
            private String ordDepositWxName;
            @SerializedName("ordNo")
            private String ordNo;
            @SerializedName("orderPrice")
            private Integer orderPrice;
            @SerializedName("payName")
            private String payName;

            public Integer getFinalPayment() {
                return finalPayment;
            }

            public void setFinalPayment(Integer finalPayment) {
                this.finalPayment = finalPayment;
            }

            public Integer getId() {
                return id;
            }

            public void setId(Integer id) {
                this.id = id;
            }

            public Integer getOrdDepositAmt() {
                return ordDepositAmt;
            }

            public void setOrdDepositAmt(Integer ordDepositAmt) {
                this.ordDepositAmt = ordDepositAmt;
            }

            public String getOrdDepositCreateTime() {
                return ordDepositCreateTime;
            }

            public void setOrdDepositCreateTime(String ordDepositCreateTime) {
                this.ordDepositCreateTime = ordDepositCreateTime;
            }

            public Integer getOrdDepositIsPayed() {
                return ordDepositIsPayed;
            }

            public void setOrdDepositIsPayed(Integer ordDepositIsPayed) {
                this.ordDepositIsPayed = ordDepositIsPayed;
            }

            public Integer getOrdDepositMid() {
                return ordDepositMid;
            }

            public void setOrdDepositMid(Integer ordDepositMid) {
                this.ordDepositMid = ordDepositMid;
            }

            public String getOrdDepositOrderNo() {
                return ordDepositOrderNo;
            }

            public void setOrdDepositOrderNo(String ordDepositOrderNo) {
                this.ordDepositOrderNo = ordDepositOrderNo;
            }

            public Integer getOrdDepositPayMid() {
                return ordDepositPayMid;
            }

            public void setOrdDepositPayMid(Integer ordDepositPayMid) {
                this.ordDepositPayMid = ordDepositPayMid;
            }

            public String getOrdDepositPayTime() {
                return ordDepositPayTime;
            }

            public void setOrdDepositPayTime(String ordDepositPayTime) {
                this.ordDepositPayTime = ordDepositPayTime;
            }

            public String getOrdDepositPayWxUnionid() {
                return ordDepositPayWxUnionid;
            }

            public void setOrdDepositPayWxUnionid(String ordDepositPayWxUnionid) {
                this.ordDepositPayWxUnionid = ordDepositPayWxUnionid;
            }

            public String getOrdDepositPaymentMethod() {
                return ordDepositPaymentMethod;
            }

            public void setOrdDepositPaymentMethod(String ordDepositPaymentMethod) {
                this.ordDepositPaymentMethod = ordDepositPaymentMethod;
            }

            public String getOrdDepositUpdateTime() {
                return ordDepositUpdateTime;
            }

            public void setOrdDepositUpdateTime(String ordDepositUpdateTime) {
                this.ordDepositUpdateTime = ordDepositUpdateTime;
            }

            public Integer getOrdDepositWithdrawStatus() {
                return ordDepositWithdrawStatus;
            }

            public void setOrdDepositWithdrawStatus(Integer ordDepositWithdrawStatus) {
                this.ordDepositWithdrawStatus = ordDepositWithdrawStatus;
            }

            public String getOrdDepositWxName() {
                return ordDepositWxName;
            }

            public void setOrdDepositWxName(String ordDepositWxName) {
                this.ordDepositWxName = ordDepositWxName;
            }

            public String getOrdNo() {
                return ordNo;
            }

            public void setOrdNo(String ordNo) {
                this.ordNo = ordNo;
            }

            public Integer getOrderPrice() {
                return orderPrice;
            }

            public void setOrderPrice(Integer orderPrice) {
                this.orderPrice = orderPrice;
            }

            public String getPayName() {
                return payName;
            }

            public void setPayName(String payName) {
                this.payName = payName;
            }
        }

        public static class OrderMainVoDTO {
            @SerializedName("backPrice")
            private double backPrice;
            @SerializedName("buyerGroupId")
            private String buyerGroupId;
            @SerializedName("createTime")
            private String createTime;
            @SerializedName("depositAmt")
            private double depositAmt;
            @SerializedName("depositIsPayed")
            private String depositIsPayed;
            @SerializedName("giveCumScore")
            private String giveCumScore;
            @SerializedName("giveCumScoreIsHas")
            private String giveCumScoreIsHas;
            @SerializedName("hasBack")
            private String hasBack;
            @SerializedName("id")
            private String id;
            @SerializedName("isJc")
            private String isJc;
            @SerializedName("ordAddressId")
            private String ordAddressId;
            @SerializedName("ordAddressName")
            private String ordAddressName;
            @SerializedName("ordAddressUserName")
            private String ordAddressUserName;
            @SerializedName("ordAddressUserPhone")
            private String ordAddressUserPhone;
            @SerializedName("ordBuyerMid")
            private String ordBuyerMid;
            @SerializedName("ordBuyerName")
            private String ordBuyerName;
            @SerializedName("ordBuyerPhone")
            private String ordBuyerPhone;
            @SerializedName("ordChangePrice")
            private double ordChangePrice;
            @SerializedName("ordClearPrice")
            private double ordClearPrice;
            @SerializedName("ordComment")
            private String ordComment;
            @SerializedName("ordCouponPrice")
            private double ordCouponPrice;
            @SerializedName("ordDeliverPrice")
            private double ordDeliverPrice;
            @SerializedName("ordDeliverUserId")
            private String ordDeliverUserId;
            @SerializedName("ordDeliverUserName")
            private String ordDeliverUserName;
            @SerializedName("ordDeliverUserPhone")
            private String ordDeliverUserPhone;
            @SerializedName("ordEventPrice")
            private double ordEventPrice;
            @SerializedName("ordIsArrvie")
            private String ordIsArrvie;
            @SerializedName("ordIsBackType")
            private int ordIsBackType;
            @SerializedName("ordIsEvent")
            private String ordIsEvent;
            @SerializedName("ordIsPayed")
            private String ordIsPayed;
            @SerializedName("ordIsPrint")
            private String ordIsPrint;
            @SerializedName("ordIsReceive")
            private String ordIsReceive;
            @SerializedName("ordLineId")
            private String ordLineId;
            @SerializedName("ordLineName")
            private String ordLineName;
            @SerializedName("ordMarketPrice")
            private double ordMarketPrice;
            @SerializedName("ordNo")
            private String ordNo;
            @SerializedName("ordNotPayAmt")
            private double ordNotPayAmt;
            @SerializedName("ordNote")
            private String ordNote;
            @SerializedName("ordOriginPrice")
            private double ordOriginPrice;
            @SerializedName("ordPaymentMethod")
            private String ordPaymentMethod;
            @SerializedName("ordPaymentType")
            private String ordPaymentType;
            @SerializedName("ordPrice")
            private double ordPrice;
            @SerializedName("ordRebatePrice")
            private double ordRebatePrice;
            @SerializedName("ordSaleUserName")
            private String ordSaleUserName;
            @SerializedName("ordSellerMid")
            private String ordSellerMid;
            @SerializedName("ordSellerName")
            private String ordSellerName;
            @SerializedName("ordSellerShopId")
            private String ordSellerShopId;
            @SerializedName("ordSellerShopName")
            private String ordSellerShopName;
            @SerializedName("ordStatus")
            private int ordStatus;
            @SerializedName("ordTimeArrive")
            private String ordTimeArrive;
            @SerializedName("ordTimeArriveUserId")
            private String ordTimeArriveUserId;
            @SerializedName("ordTimeAudit")
            private String ordTimeAudit;
            @SerializedName("ordTimeAuditUserId")
            private String ordTimeAuditUserId;
            @SerializedName("ordTimeCancel")
            private String ordTimeCancel;
            @SerializedName("ordTimeCancelUserId")
            private String ordTimeCancelUserId;
            @SerializedName("ordTimeDepart")
            private String ordTimeDepart;
            @SerializedName("ordTimeDepartUserId")
            private String ordTimeDepartUserId;
            @SerializedName("ordTimeFinish")
            private String ordTimeFinish;
            @SerializedName("ordTimeFinishUserId")
            private String ordTimeFinishUserId;
            @SerializedName("ordTimeOut")
            private String ordTimeOut;
            @SerializedName("ordTimeOutUserId")
            private String ordTimeOutUserId;
            @SerializedName("ordTimePay")
            private String ordTimePay;
            @SerializedName("ordTimePayUserId")
            private String ordTimePayUserId;
            @SerializedName("ordTimeReceive")
            private String ordTimeReceive;
            @SerializedName("ordTimeRecevieUserId")
            private String ordTimeRecevieUserId;
            @SerializedName("ordTimeStart")
            private String ordTimeStart;
            @SerializedName("ordTimeStartUserId")
            private String ordTimeStartUserId;
            @SerializedName("ordTimeStartUserName")
            private String ordTimeStartUserName;
            @SerializedName("ordToAccountEndTime")
            private String ordToAccountEndTime;
            @SerializedName("ordToAccountStartTime")
            private String ordToAccountStartTime;
            @SerializedName("ordTradePrice")
            private double ordTradePrice;
            @SerializedName("ordType")
            private String ordType;
            @SerializedName("ordWithdrawStatus")
            private String ordWithdrawStatus;
            @SerializedName("totalNum")
            private String totalNum;
            @SerializedName("useCumScore")
            private String useCumScore;
            @SerializedName("useCumScore4Money")
            private double useCumScore4Money;
            @SerializedName("orderProductVoList")
            private List<OrderProductVoListDTO> orderProductVoList;

            public double getBackPrice() {
                return backPrice;
            }

            public void setBackPrice(double backPrice) {
                this.backPrice = backPrice;
            }

            public String getBuyerGroupId() {
                return buyerGroupId;
            }

            public void setBuyerGroupId(String buyerGroupId) {
                this.buyerGroupId = buyerGroupId;
            }

            public String getCreateTime() {
                return createTime;
            }

            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public double getDepositAmt() {
                return depositAmt;
            }

            public void setDepositAmt(double depositAmt) {
                this.depositAmt = depositAmt;
            }

            public String getDepositIsPayed() {
                return depositIsPayed;
            }

            public void setDepositIsPayed(String depositIsPayed) {
                this.depositIsPayed = depositIsPayed;
            }

            public String getGiveCumScore() {
                return giveCumScore;
            }

            public void setGiveCumScore(String giveCumScore) {
                this.giveCumScore = giveCumScore;
            }

            public String getGiveCumScoreIsHas() {
                return giveCumScoreIsHas;
            }

            public void setGiveCumScoreIsHas(String giveCumScoreIsHas) {
                this.giveCumScoreIsHas = giveCumScoreIsHas;
            }

            public String getHasBack() {
                return hasBack;
            }

            public void setHasBack(String hasBack) {
                this.hasBack = hasBack;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIsJc() {
                return isJc;
            }

            public void setIsJc(String isJc) {
                this.isJc = isJc;
            }

            public String getOrdAddressId() {
                return ordAddressId;
            }

            public void setOrdAddressId(String ordAddressId) {
                this.ordAddressId = ordAddressId;
            }

            public String getOrdAddressName() {
                return ordAddressName;
            }

            public void setOrdAddressName(String ordAddressName) {
                this.ordAddressName = ordAddressName;
            }

            public String getOrdAddressUserName() {
                return ordAddressUserName;
            }

            public void setOrdAddressUserName(String ordAddressUserName) {
                this.ordAddressUserName = ordAddressUserName;
            }

            public String getOrdAddressUserPhone() {
                return ordAddressUserPhone;
            }

            public void setOrdAddressUserPhone(String ordAddressUserPhone) {
                this.ordAddressUserPhone = ordAddressUserPhone;
            }

            public String getOrdBuyerMid() {
                return ordBuyerMid;
            }

            public void setOrdBuyerMid(String ordBuyerMid) {
                this.ordBuyerMid = ordBuyerMid;
            }

            public String getOrdBuyerName() {
                return ordBuyerName;
            }

            public void setOrdBuyerName(String ordBuyerName) {
                this.ordBuyerName = ordBuyerName;
            }

            public String getOrdBuyerPhone() {
                return ordBuyerPhone;
            }

            public void setOrdBuyerPhone(String ordBuyerPhone) {
                this.ordBuyerPhone = ordBuyerPhone;
            }

            public double getOrdChangePrice() {
                return ordChangePrice;
            }

            public void setOrdChangePrice(double ordChangePrice) {
                this.ordChangePrice = ordChangePrice;
            }

            public double getOrdClearPrice() {
                return ordClearPrice;
            }

            public void setOrdClearPrice(double ordClearPrice) {
                this.ordClearPrice = ordClearPrice;
            }

            public String getOrdComment() {
                return ordComment;
            }

            public void setOrdComment(String ordComment) {
                this.ordComment = ordComment;
            }

            public double getOrdCouponPrice() {
                return ordCouponPrice;
            }

            public void setOrdCouponPrice(double ordCouponPrice) {
                this.ordCouponPrice = ordCouponPrice;
            }

            public double getOrdDeliverPrice() {
                return ordDeliverPrice;
            }

            public void setOrdDeliverPrice(double ordDeliverPrice) {
                this.ordDeliverPrice = ordDeliverPrice;
            }

            public String getOrdDeliverUserId() {
                return ordDeliverUserId;
            }

            public void setOrdDeliverUserId(String ordDeliverUserId) {
                this.ordDeliverUserId = ordDeliverUserId;
            }

            public String getOrdDeliverUserName() {
                return ordDeliverUserName;
            }

            public void setOrdDeliverUserName(String ordDeliverUserName) {
                this.ordDeliverUserName = ordDeliverUserName;
            }

            public String getOrdDeliverUserPhone() {
                return ordDeliverUserPhone;
            }

            public void setOrdDeliverUserPhone(String ordDeliverUserPhone) {
                this.ordDeliverUserPhone = ordDeliverUserPhone;
            }

            public double getOrdEventPrice() {
                return ordEventPrice;
            }

            public void setOrdEventPrice(double ordEventPrice) {
                this.ordEventPrice = ordEventPrice;
            }

            public String getOrdIsArrvie() {
                return ordIsArrvie;
            }

            public void setOrdIsArrvie(String ordIsArrvie) {
                this.ordIsArrvie = ordIsArrvie;
            }

            public int getOrdIsBackType() {
                return ordIsBackType;
            }

            public void setOrdIsBackType(int ordIsBackType) {
                this.ordIsBackType = ordIsBackType;
            }

            public String getOrdIsEvent() {
                return ordIsEvent;
            }

            public void setOrdIsEvent(String ordIsEvent) {
                this.ordIsEvent = ordIsEvent;
            }

            public String getOrdIsPayed() {
                return ordIsPayed;
            }

            public void setOrdIsPayed(String ordIsPayed) {
                this.ordIsPayed = ordIsPayed;
            }

            public String getOrdIsPrint() {
                return ordIsPrint;
            }

            public void setOrdIsPrint(String ordIsPrint) {
                this.ordIsPrint = ordIsPrint;
            }

            public String getOrdIsReceive() {
                return ordIsReceive;
            }

            public void setOrdIsReceive(String ordIsReceive) {
                this.ordIsReceive = ordIsReceive;
            }

            public String getOrdLineId() {
                return ordLineId;
            }

            public void setOrdLineId(String ordLineId) {
                this.ordLineId = ordLineId;
            }

            public String getOrdLineName() {
                return ordLineName;
            }

            public void setOrdLineName(String ordLineName) {
                this.ordLineName = ordLineName;
            }

            public double getOrdMarketPrice() {
                return ordMarketPrice;
            }

            public void setOrdMarketPrice(double ordMarketPrice) {
                this.ordMarketPrice = ordMarketPrice;
            }

            public String getOrdNo() {
                return ordNo;
            }

            public void setOrdNo(String ordNo) {
                this.ordNo = ordNo;
            }

            public double getOrdNotPayAmt() {
                return ordNotPayAmt;
            }

            public void setOrdNotPayAmt(double ordNotPayAmt) {
                this.ordNotPayAmt = ordNotPayAmt;
            }

            public String getOrdNote() {
                return ordNote;
            }

            public void setOrdNote(String ordNote) {
                this.ordNote = ordNote;
            }

            public double getOrdOriginPrice() {
                return ordOriginPrice;
            }

            public void setOrdOriginPrice(double ordOriginPrice) {
                this.ordOriginPrice = ordOriginPrice;
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

            public double getOrdPrice() {
                return ordPrice;
            }

            public void setOrdPrice(double ordPrice) {
                this.ordPrice = ordPrice;
            }

            public double getOrdRebatePrice() {
                return ordRebatePrice;
            }

            public void setOrdRebatePrice(double ordRebatePrice) {
                this.ordRebatePrice = ordRebatePrice;
            }

            public String getOrdSaleUserName() {
                return ordSaleUserName;
            }

            public void setOrdSaleUserName(String ordSaleUserName) {
                this.ordSaleUserName = ordSaleUserName;
            }

            public String getOrdSellerMid() {
                return ordSellerMid;
            }

            public void setOrdSellerMid(String ordSellerMid) {
                this.ordSellerMid = ordSellerMid;
            }

            public String getOrdSellerName() {
                return ordSellerName;
            }

            public void setOrdSellerName(String ordSellerName) {
                this.ordSellerName = ordSellerName;
            }

            public String getOrdSellerShopId() {
                return ordSellerShopId;
            }

            public void setOrdSellerShopId(String ordSellerShopId) {
                this.ordSellerShopId = ordSellerShopId;
            }

            public String getOrdSellerShopName() {
                return ordSellerShopName;
            }

            public void setOrdSellerShopName(String ordSellerShopName) {
                this.ordSellerShopName = ordSellerShopName;
            }

            public int getOrdStatus() {
                return ordStatus;
            }

            public void setOrdStatus(int ordStatus) {
                this.ordStatus = ordStatus;
            }

            public String getOrdTimeArrive() {
                return ordTimeArrive;
            }

            public void setOrdTimeArrive(String ordTimeArrive) {
                this.ordTimeArrive = ordTimeArrive;
            }

            public String getOrdTimeArriveUserId() {
                return ordTimeArriveUserId;
            }

            public void setOrdTimeArriveUserId(String ordTimeArriveUserId) {
                this.ordTimeArriveUserId = ordTimeArriveUserId;
            }

            public String getOrdTimeAudit() {
                return ordTimeAudit;
            }

            public void setOrdTimeAudit(String ordTimeAudit) {
                this.ordTimeAudit = ordTimeAudit;
            }

            public String getOrdTimeAuditUserId() {
                return ordTimeAuditUserId;
            }

            public void setOrdTimeAuditUserId(String ordTimeAuditUserId) {
                this.ordTimeAuditUserId = ordTimeAuditUserId;
            }

            public String getOrdTimeCancel() {
                return ordTimeCancel;
            }

            public void setOrdTimeCancel(String ordTimeCancel) {
                this.ordTimeCancel = ordTimeCancel;
            }

            public String getOrdTimeCancelUserId() {
                return ordTimeCancelUserId;
            }

            public void setOrdTimeCancelUserId(String ordTimeCancelUserId) {
                this.ordTimeCancelUserId = ordTimeCancelUserId;
            }

            public String getOrdTimeDepart() {
                return ordTimeDepart;
            }

            public void setOrdTimeDepart(String ordTimeDepart) {
                this.ordTimeDepart = ordTimeDepart;
            }

            public String getOrdTimeDepartUserId() {
                return ordTimeDepartUserId;
            }

            public void setOrdTimeDepartUserId(String ordTimeDepartUserId) {
                this.ordTimeDepartUserId = ordTimeDepartUserId;
            }

            public String getOrdTimeFinish() {
                return ordTimeFinish;
            }

            public void setOrdTimeFinish(String ordTimeFinish) {
                this.ordTimeFinish = ordTimeFinish;
            }

            public String getOrdTimeFinishUserId() {
                return ordTimeFinishUserId;
            }

            public void setOrdTimeFinishUserId(String ordTimeFinishUserId) {
                this.ordTimeFinishUserId = ordTimeFinishUserId;
            }

            public String getOrdTimeOut() {
                return ordTimeOut;
            }

            public void setOrdTimeOut(String ordTimeOut) {
                this.ordTimeOut = ordTimeOut;
            }

            public String getOrdTimeOutUserId() {
                return ordTimeOutUserId;
            }

            public void setOrdTimeOutUserId(String ordTimeOutUserId) {
                this.ordTimeOutUserId = ordTimeOutUserId;
            }

            public String getOrdTimePay() {
                return ordTimePay;
            }

            public void setOrdTimePay(String ordTimePay) {
                this.ordTimePay = ordTimePay;
            }

            public String getOrdTimePayUserId() {
                return ordTimePayUserId;
            }

            public void setOrdTimePayUserId(String ordTimePayUserId) {
                this.ordTimePayUserId = ordTimePayUserId;
            }

            public String getOrdTimeReceive() {
                return ordTimeReceive;
            }

            public void setOrdTimeReceive(String ordTimeReceive) {
                this.ordTimeReceive = ordTimeReceive;
            }

            public String getOrdTimeRecevieUserId() {
                return ordTimeRecevieUserId;
            }

            public void setOrdTimeRecevieUserId(String ordTimeRecevieUserId) {
                this.ordTimeRecevieUserId = ordTimeRecevieUserId;
            }

            public String getOrdTimeStart() {
                return ordTimeStart;
            }

            public void setOrdTimeStart(String ordTimeStart) {
                this.ordTimeStart = ordTimeStart;
            }

            public String getOrdTimeStartUserId() {
                return ordTimeStartUserId;
            }

            public void setOrdTimeStartUserId(String ordTimeStartUserId) {
                this.ordTimeStartUserId = ordTimeStartUserId;
            }

            public String getOrdTimeStartUserName() {
                return ordTimeStartUserName;
            }

            public void setOrdTimeStartUserName(String ordTimeStartUserName) {
                this.ordTimeStartUserName = ordTimeStartUserName;
            }

            public String getOrdToAccountEndTime() {
                return ordToAccountEndTime;
            }

            public void setOrdToAccountEndTime(String ordToAccountEndTime) {
                this.ordToAccountEndTime = ordToAccountEndTime;
            }

            public String getOrdToAccountStartTime() {
                return ordToAccountStartTime;
            }

            public void setOrdToAccountStartTime(String ordToAccountStartTime) {
                this.ordToAccountStartTime = ordToAccountStartTime;
            }

            public double getOrdTradePrice() {
                return ordTradePrice;
            }

            public void setOrdTradePrice(double ordTradePrice) {
                this.ordTradePrice = ordTradePrice;
            }

            public String getOrdType() {
                return ordType;
            }

            public void setOrdType(String ordType) {
                this.ordType = ordType;
            }

            public String getOrdWithdrawStatus() {
                return ordWithdrawStatus;
            }

            public void setOrdWithdrawStatus(String ordWithdrawStatus) {
                this.ordWithdrawStatus = ordWithdrawStatus;
            }

            public String getTotalNum() {
                return totalNum;
            }

            public void setTotalNum(String totalNum) {
                this.totalNum = totalNum;
            }

            public String getUseCumScore() {
                return useCumScore;
            }

            public void setUseCumScore(String useCumScore) {
                this.useCumScore = useCumScore;
            }

            public double getUseCumScore4Money() {
                return useCumScore4Money;
            }

            public void setUseCumScore4Money(double useCumScore4Money) {
                this.useCumScore4Money = useCumScore4Money;
            }

            public List<OrderProductVoListDTO> getOrderProductVoList() {
                return orderProductVoList;
            }

            public void setOrderProductVoList(List<OrderProductVoListDTO> orderProductVoList) {
                this.orderProductVoList = orderProductVoList;
            }

            public static class OrderProductVoListDTO {
                @SerializedName("dptCumPro")
                private DptCumProDTO dptCumPro;
                @SerializedName("group")
                private String group;
                @SerializedName("id")
                private String id;
                @SerializedName("isInBulk")
                private Integer isInBulk;
                @SerializedName("isJc")
                private Integer isJc;
                @SerializedName("isStandardProduct")
                private String isStandardProduct;
                @SerializedName("ordProductAliasName")
                private String ordProductAliasName;
                @SerializedName("ordProductBackNum")
                private Integer ordProductBackNum;
                @SerializedName("ordProductBarCode")
                private String ordProductBarCode;
                @SerializedName("ordProductBrandId")
                private String ordProductBrandId;
                @SerializedName("ordProductBrandName")
                private String ordProductBrandName;
                @SerializedName("ordProductEventId")
                private String ordProductEventId;
                @SerializedName("ordProductId")
                private String ordProductId;
                @SerializedName("ordProductImage")
                private String ordProductImage;
                @SerializedName("ordProductIsBack")
                private Integer ordProductIsBack;
                @SerializedName("ordProductIsGift")
                private Integer ordProductIsGift;
                @SerializedName("ordProductManufacturer")
                private String ordProductManufacturer;
                @SerializedName("ordProductMarketPrice")
                private double ordProductMarketPrice;
                @SerializedName("ordProductMaxNum")
                private Integer ordProductMaxNum;
                @SerializedName("ordProductName")
                private String ordProductName;
                @SerializedName("ordProductNum")
                private double ordProductNum;
                @SerializedName("ordProductOnlinePrice")
                private double ordProductOnlinePrice;
                @SerializedName("ordProductPrice")
                private double ordProductPrice;
                @SerializedName("ordProductProSpeUnitId")
                private String ordProductProSpeUnitId;
                @SerializedName("ordProductShopId")
                private String ordProductShopId;
                @SerializedName("ordProductSpecId")
                private String ordProductSpecId;
                @SerializedName("ordProductSpecName")
                private String ordProductSpecName;
                @SerializedName("ordProductSpecialPrice")
                private double ordProductSpecialPrice;
                @SerializedName("ordProductUnitId")
                private String ordProductUnitId;
                @SerializedName("ordProductUnitName")
                private String ordProductUnitName;

                public DptCumProDTO getDptCumPro() {
                    return dptCumPro;
                }

                public void setDptCumPro(DptCumProDTO dptCumPro) {
                    this.dptCumPro = dptCumPro;
                }

                public String getGroup() {
                    return group;
                }

                public void setGroup(String group) {
                    this.group = group;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public Integer getIsInBulk() {
                    return isInBulk;
                }

                public void setIsInBulk(Integer isInBulk) {
                    this.isInBulk = isInBulk;
                }

                public Integer getIsJc() {
                    return isJc;
                }

                public void setIsJc(Integer isJc) {
                    this.isJc = isJc;
                }

                public String getIsStandardProduct() {
                    return isStandardProduct;
                }

                public void setIsStandardProduct(String isStandardProduct) {
                    this.isStandardProduct = isStandardProduct;
                }

                public String getOrdProductAliasName() {
                    return ordProductAliasName;
                }

                public void setOrdProductAliasName(String ordProductAliasName) {
                    this.ordProductAliasName = ordProductAliasName;
                }

                public Integer getOrdProductBackNum() {
                    return ordProductBackNum;
                }

                public void setOrdProductBackNum(Integer ordProductBackNum) {
                    this.ordProductBackNum = ordProductBackNum;
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

                public String getOrdProductEventId() {
                    return ordProductEventId;
                }

                public void setOrdProductEventId(String ordProductEventId) {
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

                public Integer getOrdProductIsBack() {
                    return ordProductIsBack;
                }

                public void setOrdProductIsBack(Integer ordProductIsBack) {
                    this.ordProductIsBack = ordProductIsBack;
                }

                public Integer getOrdProductIsGift() {
                    return ordProductIsGift;
                }

                public void setOrdProductIsGift(Integer ordProductIsGift) {
                    this.ordProductIsGift = ordProductIsGift;
                }

                public String getOrdProductManufacturer() {
                    return ordProductManufacturer;
                }

                public void setOrdProductManufacturer(String ordProductManufacturer) {
                    this.ordProductManufacturer = ordProductManufacturer;
                }

                public double getOrdProductMarketPrice() {
                    return ordProductMarketPrice;
                }

                public void setOrdProductMarketPrice(double ordProductMarketPrice) {
                    this.ordProductMarketPrice = ordProductMarketPrice;
                }

                public Integer getOrdProductMaxNum() {
                    return ordProductMaxNum;
                }

                public void setOrdProductMaxNum(Integer ordProductMaxNum) {
                    this.ordProductMaxNum = ordProductMaxNum;
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

                public double getOrdProductOnlinePrice() {
                    return ordProductOnlinePrice;
                }

                public void setOrdProductOnlinePrice(double ordProductOnlinePrice) {
                    this.ordProductOnlinePrice = ordProductOnlinePrice;
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

                public double getOrdProductSpecialPrice() {
                    return ordProductSpecialPrice;
                }

                public void setOrdProductSpecialPrice(double ordProductSpecialPrice) {
                    this.ordProductSpecialPrice = ordProductSpecialPrice;
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

                public static class DptCumProDTO {
                }
            }
        }

        public static class ShopInfoDTO {
            @SerializedName("shopAddress")
            private String shopAddress;
            @SerializedName("shopPhone")
            private String shopPhone;
            @SerializedName("shopReadme")
            private String shopReadme;

            public String getShopAddress() {
                return shopAddress;
            }

            public void setShopAddress(String shopAddress) {
                this.shopAddress = shopAddress;
            }

            public String getShopPhone() {
                return shopPhone;
            }

            public void setShopPhone(String shopPhone) {
                this.shopPhone = shopPhone;
            }

            public String getShopReadme() {
                return shopReadme;
            }

            public void setShopReadme(String shopReadme) {
                this.shopReadme = shopReadme;
            }
        }

        public static class OrderGiftVoListDTO {
            @SerializedName("isInBulk")
            private Integer isInBulk;
            @SerializedName("ordProductAliasName")
            private String ordProductAliasName;
            @SerializedName("ordProductId")
            private String ordProductId;
            @SerializedName("ordProductName")
            private String ordProductName;
            @SerializedName("ordProductNum")
            private Integer ordProductNum;
            @SerializedName("ordProductProSpeUnitId")
            private String ordProductProSpeUnitId;
            @SerializedName("ordProductSpecId")
            private String ordProductSpecId;

            public Integer getIsInBulk() {
                return isInBulk;
            }

            public void setIsInBulk(Integer isInBulk) {
                this.isInBulk = isInBulk;
            }

            public String getOrdProductAliasName() {
                return ordProductAliasName;
            }

            public void setOrdProductAliasName(String ordProductAliasName) {
                this.ordProductAliasName = ordProductAliasName;
            }

            public String getOrdProductId() {
                return ordProductId;
            }

            public void setOrdProductId(String ordProductId) {
                this.ordProductId = ordProductId;
            }

            public String getOrdProductName() {
                return ordProductName;
            }

            public void setOrdProductName(String ordProductName) {
                this.ordProductName = ordProductName;
            }

            public Integer getOrdProductNum() {
                return ordProductNum;
            }

            public void setOrdProductNum(Integer ordProductNum) {
                this.ordProductNum = ordProductNum;
            }

            public String getOrdProductProSpeUnitId() {
                return ordProductProSpeUnitId;
            }

            public void setOrdProductProSpeUnitId(String ordProductProSpeUnitId) {
                this.ordProductProSpeUnitId = ordProductProSpeUnitId;
            }

            public String getOrdProductSpecId() {
                return ordProductSpecId;
            }

            public void setOrdProductSpecId(String ordProductSpecId) {
                this.ordProductSpecId = ordProductSpecId;
            }
        }

        public static class OrderProductVoDTO {
            @SerializedName("dptCumPro")
            private DptCumProDTOX dptCumPro;
            @SerializedName("group")
            private String group;
            @SerializedName("id")
            private String id;
            @SerializedName("isInBulk")
            private Integer isInBulk;
            @SerializedName("isJc")
            private Integer isJc;
            @SerializedName("isStandardProduct")
            private String isStandardProduct;
            @SerializedName("ordProductAliasName")
            private String ordProductAliasName;
            @SerializedName("ordProductBackNum")
            private Integer ordProductBackNum;
            @SerializedName("ordProductBarCode")
            private String ordProductBarCode;
            @SerializedName("ordProductBrandId")
            private String ordProductBrandId;
            @SerializedName("ordProductBrandName")
            private String ordProductBrandName;
            @SerializedName("ordProductEventId")
            private long ordProductEventId;
            @SerializedName("ordProductId")
            private String ordProductId;
            @SerializedName("ordProductImage")
            private String ordProductImage;
            @SerializedName("ordProductIsBack")
            private Integer ordProductIsBack;
            @SerializedName("ordProductIsGift")
            private Integer ordProductIsGift;
            @SerializedName("ordProductManufacturer")
            private String ordProductManufacturer;
            @SerializedName("ordProductMarketPrice")
            private double ordProductMarketPrice;
            @SerializedName("ordProductMaxNum")
            private Integer ordProductMaxNum;
            @SerializedName("ordProductName")
            private String ordProductName;
            @SerializedName("ordProductNum")
            private double ordProductNum;
            @SerializedName("ordProductOnlinePrice")
            private double ordProductOnlinePrice;
            @SerializedName("ordProductPrice")
            private double ordProductPrice;
            @SerializedName("ordProductProSpeUnitId")
            private String ordProductProSpeUnitId;
            @SerializedName("ordProductShopId")
            private String ordProductShopId;
            @SerializedName("ordProductSpecId")
            private String ordProductSpecId;
            @SerializedName("ordProductSpecName")
            private String ordProductSpecName;
            @SerializedName("ordProductSpecialPrice")
            private double ordProductSpecialPrice;
            @SerializedName("ordProductUnitId")
            private String ordProductUnitId;
            @SerializedName("ordProductUnitName")
            private String ordProductUnitName;

            public DptCumProDTOX getDptCumPro() {
                return dptCumPro;
            }

            public void setDptCumPro(DptCumProDTOX dptCumPro) {
                this.dptCumPro = dptCumPro;
            }

            public String getGroup() {
                return group;
            }

            public void setGroup(String group) {
                this.group = group;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public Integer getIsInBulk() {
                return isInBulk;
            }

            public void setIsInBulk(Integer isInBulk) {
                this.isInBulk = isInBulk;
            }

            public Integer getIsJc() {
                return isJc;
            }

            public void setIsJc(Integer isJc) {
                this.isJc = isJc;
            }

            public String getIsStandardProduct() {
                return isStandardProduct;
            }

            public void setIsStandardProduct(String isStandardProduct) {
                this.isStandardProduct = isStandardProduct;
            }

            public String getOrdProductAliasName() {
                return ordProductAliasName;
            }

            public void setOrdProductAliasName(String ordProductAliasName) {
                this.ordProductAliasName = ordProductAliasName;
            }

            public Integer getOrdProductBackNum() {
                return ordProductBackNum;
            }

            public void setOrdProductBackNum(Integer ordProductBackNum) {
                this.ordProductBackNum = ordProductBackNum;
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

            public Integer getOrdProductIsBack() {
                return ordProductIsBack;
            }

            public void setOrdProductIsBack(Integer ordProductIsBack) {
                this.ordProductIsBack = ordProductIsBack;
            }

            public Integer getOrdProductIsGift() {
                return ordProductIsGift;
            }

            public void setOrdProductIsGift(Integer ordProductIsGift) {
                this.ordProductIsGift = ordProductIsGift;
            }

            public String getOrdProductManufacturer() {
                return ordProductManufacturer;
            }

            public void setOrdProductManufacturer(String ordProductManufacturer) {
                this.ordProductManufacturer = ordProductManufacturer;
            }

            public double getOrdProductMarketPrice() {
                return ordProductMarketPrice;
            }

            public void setOrdProductMarketPrice(double ordProductMarketPrice) {
                this.ordProductMarketPrice = ordProductMarketPrice;
            }

            public Integer getOrdProductMaxNum() {
                return ordProductMaxNum;
            }

            public void setOrdProductMaxNum(Integer ordProductMaxNum) {
                this.ordProductMaxNum = ordProductMaxNum;
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

            public double getOrdProductOnlinePrice() {
                return ordProductOnlinePrice;
            }

            public void setOrdProductOnlinePrice(double ordProductOnlinePrice) {
                this.ordProductOnlinePrice = ordProductOnlinePrice;
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

            public double getOrdProductSpecialPrice() {
                return ordProductSpecialPrice;
            }

            public void setOrdProductSpecialPrice(double ordProductSpecialPrice) {
                this.ordProductSpecialPrice = ordProductSpecialPrice;
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

            public static class DptCumProDTOX {
            }
        }

        public static class PdLogisticsTrilateralVoListDTO {
            @SerializedName("dpAddress")
            private String dpAddress;
            @SerializedName("dpLogTrilateralId")
            private String dpLogTrilateralId;
            @SerializedName("dpLogTrilateralName")
            private String dpLogTrilateralName;
            @SerializedName("dpLogTrilateralOrderNo")
            private String dpLogTrilateralOrderNo;
            @SerializedName("dpMode")
            private String dpMode;
            @SerializedName("dpPrice")
            private double dpPrice;
            @SerializedName("dpPriceReceived")
            private String dpPriceReceived;
            @SerializedName("dpStatus")
            private String dpStatus;
            @SerializedName("id")
            private String id;
            @SerializedName("orderNo")
            private String orderNo;
            @SerializedName("outNo")
            private String outNo;

            public String getDpAddress() {
                return dpAddress;
            }

            public void setDpAddress(String dpAddress) {
                this.dpAddress = dpAddress;
            }

            public String getDpLogTrilateralId() {
                return dpLogTrilateralId;
            }

            public void setDpLogTrilateralId(String dpLogTrilateralId) {
                this.dpLogTrilateralId = dpLogTrilateralId;
            }

            public String getDpLogTrilateralName() {
                return dpLogTrilateralName;
            }

            public void setDpLogTrilateralName(String dpLogTrilateralName) {
                this.dpLogTrilateralName = dpLogTrilateralName;
            }

            public String getDpLogTrilateralOrderNo() {
                return dpLogTrilateralOrderNo;
            }

            public void setDpLogTrilateralOrderNo(String dpLogTrilateralOrderNo) {
                this.dpLogTrilateralOrderNo = dpLogTrilateralOrderNo;
            }

            public String getDpMode() {
                return dpMode;
            }

            public void setDpMode(String dpMode) {
                this.dpMode = dpMode;
            }

            public double getDpPrice() {
                return dpPrice;
            }

            public void setDpPrice(double dpPrice) {
                this.dpPrice = dpPrice;
            }

            public String getDpPriceReceived() {
                return dpPriceReceived;
            }

            public void setDpPriceReceived(String dpPriceReceived) {
                this.dpPriceReceived = dpPriceReceived;
            }

            public String getDpStatus() {
                return dpStatus;
            }

            public void setDpStatus(String dpStatus) {
                this.dpStatus = dpStatus;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderNo() {
                return orderNo;
            }

            public void setOrderNo(String orderNo) {
                this.orderNo = orderNo;
            }

            public String getOutNo() {
                return outNo;
            }

            public void setOutNo(String outNo) {
                this.outNo = outNo;
            }
        }

        public static class PdLogisticsVoListDTO {
            @SerializedName("id")
            private String id;
            @SerializedName("ordNo")
            private String ordNo;
            @SerializedName("outNo")
            private String outNo;
            @SerializedName("pdAddress")
            private String pdAddress;
            @SerializedName("pdAddressId")
            private String pdAddressId;
            @SerializedName("pdLineId")
            private String pdLineId;
            @SerializedName("pdLineName")
            private String pdLineName;
            @SerializedName("pdMode")
            private String pdMode;
            @SerializedName("pdPrice")
            private double pdPrice;
            @SerializedName("pdPriceReceived")
            private String pdPriceReceived;
            @SerializedName("pdStatus")
            private String pdStatus;
            @SerializedName("pdUserId")
            private String pdUserId;
            @SerializedName("pdUserName")
            private String pdUserName;
            @SerializedName("pdUserPhone")
            private String pdUserPhone;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrdNo() {
                return ordNo;
            }

            public void setOrdNo(String ordNo) {
                this.ordNo = ordNo;
            }

            public String getOutNo() {
                return outNo;
            }

            public void setOutNo(String outNo) {
                this.outNo = outNo;
            }

            public String getPdAddress() {
                return pdAddress;
            }

            public void setPdAddress(String pdAddress) {
                this.pdAddress = pdAddress;
            }

            public String getPdAddressId() {
                return pdAddressId;
            }

            public void setPdAddressId(String pdAddressId) {
                this.pdAddressId = pdAddressId;
            }

            public String getPdLineId() {
                return pdLineId;
            }

            public void setPdLineId(String pdLineId) {
                this.pdLineId = pdLineId;
            }

            public String getPdLineName() {
                return pdLineName;
            }

            public void setPdLineName(String pdLineName) {
                this.pdLineName = pdLineName;
            }

            public String getPdMode() {
                return pdMode;
            }

            public void setPdMode(String pdMode) {
                this.pdMode = pdMode;
            }

            public double getPdPrice() {
                return pdPrice;
            }

            public void setPdPrice(double pdPrice) {
                this.pdPrice = pdPrice;
            }

            public String getPdPriceReceived() {
                return pdPriceReceived;
            }

            public void setPdPriceReceived(String pdPriceReceived) {
                this.pdPriceReceived = pdPriceReceived;
            }

            public String getPdStatus() {
                return pdStatus;
            }

            public void setPdStatus(String pdStatus) {
                this.pdStatus = pdStatus;
            }

            public String getPdUserId() {
                return pdUserId;
            }

            public void setPdUserId(String pdUserId) {
                this.pdUserId = pdUserId;
            }

            public String getPdUserName() {
                return pdUserName;
            }

            public void setPdUserName(String pdUserName) {
                this.pdUserName = pdUserName;
            }

            public String getPdUserPhone() {
                return pdUserPhone;
            }

            public void setPdUserPhone(String pdUserPhone) {
                this.pdUserPhone = pdUserPhone;
            }
        }
    }
}
