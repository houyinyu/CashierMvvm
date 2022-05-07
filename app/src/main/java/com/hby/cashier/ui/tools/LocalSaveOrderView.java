package com.hby.cashier.ui.tools;

import android.text.TextUtils;

import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderBean;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.printer.PrinterServiceView;
import com.hby.cashier.utils.DeviceUtils;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能介绍 :本地创建订单公共类
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/20
 */
public class LocalSaveOrderView {

    public LocalSaveOrderView() {
    }

    List<ShopCartBean> shopCartList = new ArrayList<>();

    public void setShopCartList(List<ShopCartBean> shopCartList) {
        this.shopCartList = shopCartList;
    }

    String paymentMethod;

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    String paymentType;

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * 本地创建订单
     * isUpdate:线上订单后端都已经保存了不用上传，线下订单（现金和免单需要定时上传到服务器-走创建订单接口）
     */
    public boolean localSaveOrderDetail(String orderNo, int payStatus, double orderPrice, double actualPrice
            , double promotionPrice,double rebatePrice, double changePrice, boolean isUpdate) {
        String postOrderJson = JsonUtils.Object2Json(getCreateOrderData(orderNo));
        LitePalOrderDetailsBean orderDetailsBean = JsonUtils.parseJson(postOrderJson, LitePalOrderDetailsBean.class);
        orderDetailsBean.setOrderProductListJson(JsonUtils.Object2Json(getProductList()));
        orderDetailsBean.setOrderPrice(orderPrice);
        orderDetailsBean.setActualPrice(actualPrice);
        orderDetailsBean.setPromotionPrice(promotionPrice);
        orderDetailsBean.setRebatePrice(rebatePrice);
        orderDetailsBean.setChangePrice(changePrice);
        orderDetailsBean.setCreateTimeStamp(System.currentTimeMillis());
        orderDetailsBean.setPaymentStatus(payStatus);
        orderDetailsBean.setUpdate(isUpdate);
        return orderDetailsBean.save();
    }

    /**
     * 创建订单前的数据
     *
     * @param orderNo-创建本地订单传的数据、线上接口不传
     * @return
     */
    public CreateOrderBean getCreateOrderData(String orderNo) {
        String shopID = SPUtils.getInstance().getString(DataKey.KEY_SHOP_ID);
        String deviceID = SPUtils.getInstance().getString(DataKey.KEY_DEVICE_ID);
        CreateOrderBean createOrderBean = new CreateOrderBean();
        if (!TextUtils.isEmpty(orderNo)) {
            createOrderBean.setOrdNo(orderNo);
        }
        createOrderBean.setSyOrder(true);
        createOrderBean.setSyDeviceId("dev_"+deviceID);
        createOrderBean.setOrdResource("APP");
        createOrderBean.setOrdSellerShopId(shopID);
        createOrderBean.setOrdPaymentMethod(paymentMethod);
        createOrderBean.setOrdPaymentType(paymentType);
        createOrderBean.setOrdType(30);
        createOrderBean.setOrdComment("");

        createOrderBean.setOrderProductAoList(getProductList());

        return createOrderBean;
    }

    private List<CreateOrderBean.OrderProductAoListBean> getProductList() {
        List<CreateOrderBean.OrderProductAoListBean> createOrderProductList = new ArrayList<>();
        for (int i = 0; i < shopCartList.size(); i++) {
            for (int j = 0; j < shopCartList.get(i).getProductList().size(); j++) {
                ShopCartBean.ProductListBean shopCartProductBean = shopCartList.get(i).getProductList().get(j);
                CreateOrderBean.OrderProductAoListBean createOrderProductBean =
                        new CreateOrderBean.OrderProductAoListBean();
                createOrderProductBean.setOrdProductName(shopCartProductBean.getProductName());
                createOrderProductBean.setOrdProductNum(shopCartProductBean.getProductNum());
                createOrderProductBean.setOrdProductPrice(shopCartProductBean.getProductPrice());
                createOrderProductBean.setOrdProductProSpeUnitId(shopCartProductBean.getSpecificationUnitID());
                createOrderProductBean.setOrdProductShopId(shopCartProductBean.getShopID());
                createOrderProductBean.setOrdProductSpecId(shopCartProductBean.getSpecID());
                createOrderProductBean.setOrdProductSpecName(shopCartProductBean.getSpecName());
                createOrderProductBean.setOrdProductUnitId(shopCartProductBean.getUnitID());
                createOrderProductBean.setOrdProductUnitName(shopCartProductBean.getUnitName());
                createOrderProductBean.setOrdProductBarCode(shopCartProductBean.getBarCode());
                createOrderProductBean.setOrdProductBrandId(shopCartProductBean.getBrandID());
                createOrderProductBean.setOrdProductBrandName(shopCartProductBean.getBrandName());
                createOrderProductBean.setOrdProductEventId(shopCartProductBean.getEventID());
                createOrderProductBean.setOrdProductId(shopCartProductBean.getProductID());
                createOrderProductBean.setOrdProductImage(shopCartProductBean.getProductImage());
                createOrderProductBean.setOrdProductManufacturer(shopCartProductBean.getManufacturer());
                createOrderProductBean.setIsInBulk(shopCartProductBean.getIsInBulk());
                createOrderProductBean.setIsStandardProduct(shopCartProductBean.getIsStandard());

                createOrderProductList.add(createOrderProductBean);
            }
        }
        return createOrderProductList;
    }


    /**
     * 打印
     *
     * @param payTypeStr
     */
    public void showPrint(String payTypeStr,String orderNo, double orderPrice
            , double actualPrice, double promotionPrice,double rebatePrice, double changePrice) {
        PrinterServiceView printView = PrinterServiceView.getInstance();
        printView.setOrderNo(orderNo);
        printView.setOrderPrice(orderPrice);
        printView.setActualPrice(actualPrice);
        printView.setPromotionPrice(promotionPrice);
        printView.setRebatePrice(rebatePrice);
        printView.setChangePrice(changePrice);
        String jsonData = printView.buildProductData(shopCartList, payTypeStr);
        printView.paySuccessToPrinter(jsonData, paymentMethod);
    }

}
