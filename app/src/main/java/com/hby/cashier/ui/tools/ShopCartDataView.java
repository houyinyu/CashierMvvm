package com.hby.cashier.ui.tools;

import com.hby.cashier.bean.LitePalEventInfoBean;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.bean.RecordOrderDetailsBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.utils.NumUtils;

import org.jetbrains.annotations.NotNull;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/18
 */
public class ShopCartDataView {


    /**
     * 加入购物的时候生成列表
     * 获取购物车数据-点击直接加入
     *
     * @param shopCartList
     * @param productBean
     * @return
     */
    public static List<ShopCartBean> getShopCartList(List<ShopCartBean> shopCartList, LitePalProductBean productBean) {
        if (shopCartList.size() == 0) {
            ShopCartBean shopCartBean = new ShopCartBean();
            shopCartBean.setEventID(productBean.getEventID());
            shopCartBean.setEventType(productBean.getEventType());

            List<ShopCartBean.ProductListBean> shopCartProductList = new ArrayList<>();
            shopCartProductList.add(getShopCartDataBean(productBean));

            shopCartBean.setProductList(shopCartProductList);
            shopCartList.add(shopCartBean);
        } else {
            boolean eventExist = false;//活动是否存在
            boolean productExist = false;//商品是否存在
            for (int i = 0; i < shopCartList.size(); i++) {
                if (productBean.getEventID() == shopCartList.get(i).getEventID()) {
                    eventExist = true;
                    for (int j = 0; j < shopCartList.get(i).getProductList().size(); j++) {
                        if (productBean.getProSpecificationUnitId().equals(shopCartList.get(i).getProductList().get(j).getSpecificationUnitID())) {
                            shopCartList.get(i).getProductList().get(j).setProductNum(shopCartList.get(i).getProductList().get(j).getProductNum() + 1);
                            productExist = true;
                            break;
                        }
                    }
                }
                if (productExist) {
                    break;
                }
            }

            //活动存在，但是商品不存在-只添加商品
            if (eventExist && !productExist) {
                for (int i = 0; i < shopCartList.size(); i++) {
                    if (productBean.getEventID() == shopCartList.get(i).getEventID()) {
                        shopCartList.get(i).getProductList().add(getShopCartDataBean(productBean));
                        break;
                    }
                }
            }

            //活动不存在（商品肯定不存在）-添加活动和活动下的商品
            if (!eventExist) {
                ShopCartBean shopCartBean = new ShopCartBean();
                shopCartBean.setEventID(productBean.getEventID());
                shopCartBean.setEventType(productBean.getEventType());

                List<ShopCartBean.ProductListBean> shopCartProductList = new ArrayList<>();
                shopCartProductList.add(getShopCartDataBean(productBean));

                shopCartBean.setProductList(shopCartProductList);
                shopCartList.add(shopCartBean);
            }
        }
        return listSorting(shopCartList);
//        return shopCartList;
    }

    private static ShopCartBean.ProductListBean getShopCartDataBean(LitePalProductBean productBean) {
        ShopCartBean.ProductListBean productListBean = new ShopCartBean.ProductListBean();

        productListBean.setProductName(productBean.getProName());
        productListBean.setProductNum(1);
        productListBean.setProductPrice(NumUtils.getProductPrice(productBean));
        productListBean.setProductID(productBean.getProId());
        productListBean.setProductImage(productBean.getImgUrl());
        productListBean.setSpecificationUnitID(productBean.getProSpecificationUnitId());
        productListBean.setShopID(productBean.getShopId());
        productListBean.setSpecName(productBean.getSpecification());
        productListBean.setSpecID(productBean.getSpecificationId());
        productListBean.setUnitName(productBean.getUnitName());
        productListBean.setUnitID(productBean.getUnitId());
        productListBean.setBarCode(productBean.getBarCode());
        productListBean.setBrandID(productBean.getProBrandId());
        productListBean.setBrandName(productBean.getProBrandName());
        productListBean.setManufacturer(productBean.getProManufacturersName());
        productListBean.setEventID(productBean.getEventID());
        productListBean.setEventType(productBean.getEventType());
        productListBean.setIsInBulk(productBean.getIsInBulk());
        productListBean.setIsMinUnit(productBean.getIsMinUnit());
        productListBean.setIsStandard(productBean.getIsStandardProduct());
        productListBean.setOnlineRebate(productBean.getGroupOnlineRebate());
        productListBean.setOnlineRebateUnit(productBean.getGroupOnlineRebateUnit());

        return productListBean;
    }

    /**
     * 加入购物的时候生成列表
     * 获取购物车数据-称重商品加入
     *
     * @param shopCartList
     * @param productBean
     * @return
     */
    public static List<ShopCartBean> getShopCartList(List<ShopCartBean> shopCartList, ShopCartBean.ProductListBean productBean) {
        if (shopCartList.size() == 0) {
            ShopCartBean shopCartBean = new ShopCartBean();
            shopCartBean.setEventID(productBean.getEventID());
            shopCartBean.setEventType(productBean.getEventType());

            List<ShopCartBean.ProductListBean> shopCartProductList = new ArrayList<>();
            shopCartProductList.add(productBean);

            shopCartBean.setProductList(shopCartProductList);
            shopCartList.add(shopCartBean);
        } else {
            boolean eventExist = false;//活动是否存在
            boolean productExist = false;//商品是否存在
            for (int i = 0; i < shopCartList.size(); i++) {
                if (productBean.getEventID() == shopCartList.get(i).getEventID()) {
                    eventExist = true;
                    for (int j = 0; j < shopCartList.get(i).getProductList().size(); j++) {
                        if (productBean.getSpecificationUnitID().equals(shopCartList.get(i).getProductList().get(j).getSpecificationUnitID())) {
                            shopCartList.get(i).getProductList().get(j).setProductNum(shopCartList.get(i).getProductList().get(j).getProductNum() + 1);
                            productExist = true;
                            break;
                        }
                    }
                }
                if (productExist) {
                    break;
                }
            }

            //活动存在，但是商品不存在-只添加商品
            if (eventExist && !productExist) {
                for (int i = 0; i < shopCartList.size(); i++) {
                    if (productBean.getEventID() == shopCartList.get(i).getEventID()) {
                        shopCartList.get(i).getProductList().add(productBean);
                        break;
                    }
                }
            }

            //活动不存在（商品肯定不存在）-添加活动和活动下的商品
            if (!eventExist) {
                ShopCartBean shopCartBean = new ShopCartBean();
                shopCartBean.setEventID(productBean.getEventID());
                shopCartBean.setEventType(productBean.getEventType());

                List<ShopCartBean.ProductListBean> shopCartProductList = new ArrayList<>();
                shopCartProductList.add(productBean);

                shopCartBean.setProductList(shopCartProductList);
                shopCartList.add(shopCartBean);
            }
        }
        return listSorting(shopCartList);
//        return shopCartList;
    }

    public static List<ShopCartBean> listSorting(List<ShopCartBean> listInAppxList) {
        Comparator<ShopCartBean> comparator = new Comparator<ShopCartBean>() {
            @Override
            public int compare(ShopCartBean details1, ShopCartBean details2) {
                //排序规则，按照价格由大到小顺序排列("<"),按照价格由小到大顺序排列(">"),
                if (details1.getEventType() > details2.getEventType())
                    return 1;
                else {
                    return -1;
                }
            }
        };
        //这里就会自动根据规则进行排序
        Collections.sort(listInAppxList, comparator);
        return listInAppxList;
    }


    /**
     * 销售记录直接结算
     *
     * @return
     */
    public static List<ShopCartBean> getShopCartList(List<RecordOrderDetailsBean.ResultObjectDTO.OrderProductVoDTO> productList) {
        List<ShopCartBean> shopCartList = new ArrayList<>();
        List<LitePalEventInfoBean> selectEventInfoList = LitePal.findAll(LitePalEventInfoBean.class);

        for (int i = 0; i < productList.size(); i++) {
            RecordOrderDetailsBean.ResultObjectDTO.OrderProductVoDTO productBean = productList.get(i);
            if (productBean.getIsInBulk() > 0) continue;
            //第一次加入
            if (shopCartList.size() == 0) {
                ShopCartBean shopCartBean = new ShopCartBean();
                shopCartBean.setEventID(productBean.getOrdProductEventId());
                shopCartBean.setEventType(getEventType(productBean.getOrdProductEventId(), selectEventInfoList));

                List<ShopCartBean.ProductListBean> shopCartProductList = new ArrayList<>();
                shopCartProductList.add(getShopCartDataBean(productBean, selectEventInfoList));

                shopCartBean.setProductList(shopCartProductList);
                shopCartList.add(shopCartBean);
            } else {
                boolean eventExist = false;//活动是否存在
                boolean productExist = false;//商品是否存在
                for (int j = 0; j < shopCartList.size(); j++) {
                    if (productBean.getOrdProductEventId() == shopCartList.get(j).getEventID()) {
                        eventExist = true;
                        for (int k = 0; k < shopCartList.get(j).getProductList().size(); k++) {
                            if (productBean.getOrdProductProSpeUnitId().equals(
                                    shopCartList.get(j).getProductList().get(k).getSpecificationUnitID())) {
                                shopCartList.get(j).getProductList().get(k).setProductNum(
                                        shopCartList.get(j).getProductList().get(k).getProductNum() + productBean.getOrdProductNum());
                                productExist = true;
                                break;
                            }
                        }
                    }
                    if (productExist) {
                        break;
                    }
                }

                //活动存在，但是商品不存在-只添加商品
                if (eventExist && !productExist) {
                    for (int j = 0; j < shopCartList.size(); j++) {
                        if (productBean.getOrdProductEventId() == shopCartList.get(j).getEventID()) {
                            shopCartList.get(j).getProductList().add(getShopCartDataBean(productBean, selectEventInfoList));
                            break;
                        }
                    }
                }

                //活动不存在（商品肯定不存在）-添加活动和活动下的商品
                if (!eventExist) {
                    ShopCartBean shopCartBean = new ShopCartBean();
                    shopCartBean.setEventID(productBean.getOrdProductEventId());
                    shopCartBean.setEventType(getEventType(productBean.getOrdProductEventId(), selectEventInfoList));

                    List<ShopCartBean.ProductListBean> shopCartProductList = new ArrayList<>();
                    shopCartProductList.add(getShopCartDataBean(productBean, selectEventInfoList));

                    shopCartBean.setProductList(shopCartProductList);
                    shopCartList.add(shopCartBean);
                }
            }
        }
        return listSorting(shopCartList);
    }

    private static ShopCartBean.ProductListBean getShopCartDataBean(RecordOrderDetailsBean.ResultObjectDTO.OrderProductVoDTO productBean,
                                                                    List<LitePalEventInfoBean> selectEventInfoList) {
        List<LitePalProductBean> searchProductList =
                LitePal.where("prospecificationunitid = ?", productBean.getOrdProductProSpeUnitId()).find(LitePalProductBean.class);

        return getShopCartDataBean(searchProductList.get(0));
//        ShopCartBean.ProductListBean productListBean = new ShopCartBean.ProductListBean();
//
//        productListBean.setProductName(productBean.getOrdProductName());
//        productListBean.setProductNum(productBean.getOrdProductNum());
//        productListBean.setProductPrice(productBean.getOrdProductPrice());
//        productListBean.setProductID(productBean.getOrdProductId());
//        productListBean.setProductImage(productBean.getOrdProductImage());
//        productListBean.setSpecificationUnitID(productBean.getOrdProductProSpeUnitId());
//        productListBean.setShopID(productBean.getOrdProductShopId());
//        productListBean.setSpecName(productBean.getOrdProductSpecName());
//        productListBean.setSpecID(productBean.getOrdProductSpecId());
//        productListBean.setUnitName(productBean.getOrdProductUnitName());
//        productListBean.setUnitID(productBean.getOrdProductUnitId());
//        productListBean.setBarCode(productBean.getOrdProductBarCode());
//        productListBean.setBrandID(productBean.getOrdProductBrandId());
//        productListBean.setBrandName(productBean.getOrdProductBrandName());
//        productListBean.setManufacturer(productBean.getOrdProductManufacturer());
//        productListBean.setEventID(productBean.getOrdProductEventId());
//        productListBean.setEventType(getEventType(productBean.getOrdProductEventId(), selectEventInfoList));
//        productListBean.setIsInBulk(productBean.getIsInBulk());
//        productListBean.setIsStandard(productBean.getIsStandardProduct());
//        productListBean.setOnlineRebate(productBean.getR());
//        productListBean.setOnlineRebateUnit(productBean.getGroupOnlineRebateUnit());

//        return productListBean;
    }

    private static int getEventType(Long eventId, List<LitePalEventInfoBean> selectEventInfoList) {
        for (int i = 0; i < selectEventInfoList.size(); i++) {
            if (eventId == selectEventInfoList.get(i).getEventId()) {
                return selectEventInfoList.get(i).getEventType();
            }
        }
        return 0;
    }
}
