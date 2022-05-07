package com.hby.cashier.ui.tools;

import com.hby.cashier.bean.LitePalEventRuleBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NumUtils;

import org.litepal.LitePal;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 功能介绍 :价格计算-因为有活动，所以在这计算价格
 * 调用方式 :购物车计算、取单界面搜索
 * 作   者 :Hyy
 * 创建时间 :2022/4/18
 */
public class PriceCalculationView {


    public static int getProductNum(List<ShopCartBean> shopCartList) {
        int shopCartNum = 0;
        for (int i = 0; i < shopCartList.size(); i++) {
            for (int j = 0; j < shopCartList.get(i).getProductList().size(); j++) {
                if (shopCartList.get(i).getProductList().get(j).getIsInBulk() > 0 ||
                        !shopCartList.get(i).getProductList().get(j).getIsStandard().equals("Y")) {
                    shopCartNum += 1;
                } else {
                    shopCartNum += shopCartList.get(i).getProductList().get(j).getProductNum();
                }
            }
        }
        return shopCartNum;
    }

    /**
     * 获取所有商品加起来的实际价格
     *
     * @param shopCartList
     * @return
     */
    public static double getProductPriceDefault(List<ShopCartBean> shopCartList) {
        double shopCartPrice = 0;
        for (int i = 0; i < shopCartList.size(); i++) {
            for (int j = 0; j < shopCartList.get(i).getProductList().size(); j++) {
                shopCartPrice += shopCartList.get(i).getProductList().get(j).getProductNum()
                        * shopCartList.get(i).getProductList().get(j).getProductPrice();
            }
        }
        return shopCartPrice;
    }

    /**
     * 满量送：是送自己-10
     * 满额赠：是送赠品-20
     * 满减：每满x价格减y价格-满100减10，满200减20/阶梯减就是只满足单独一个
     * 满折：每满x价格打y折
     * 获取打折后的实际价格
     *
     * @param shopCartList
     */
    public static double getProductPrice(List<ShopCartBean> shopCartList) {
        double shopCartPrice = 0;
        for (int i = 0; i < shopCartList.size(); i++) {
            long eventID = shopCartList.get(i).getEventID();
            int eventType = shopCartList.get(i).getEventType();
            switch (eventType) {
//                //满送
//                case EnumUtils.EventType.EVENT_FULL_GIVE:
//                case EnumUtils.EventType.EVENT_LADDER_GIVE:
//                    break;
//                //满赠
//                case EnumUtils.EventType.EVENT_FULL_GIFT:
//                case EnumUtils.EventType.EVENT_LADDER_GIFT:
//                    break;
                //满减
                case EnumUtils.EventType.EVENT_FULL_REDUCTION:
                    shopCartPrice += getFullReductionPrice(eventID, shopCartList.get(i).getProductList());
                    break;
                case EnumUtils.EventType.EVENT_LADDER_REDUCTION:
                    shopCartPrice += getLadderReductionPrice(eventID, shopCartList.get(i).getProductList());
                    break;
                //满折
                case EnumUtils.EventType.EVENT_LADDER_DISCOUNT:
                    shopCartPrice += getFullDiscountPrice(eventID, shopCartList.get(i).getProductList());
                    break;
                default:
                    shopCartPrice += getOtherPrice(shopCartList.get(i).getProductList());
                    break;
            }
        }

        return shopCartPrice;
    }


    /**
     * 获取满减的价格
     */
    private static double getFullReductionPrice(long eventID, List<ShopCartBean.ProductListBean> productList) {
        //1.先获取全部的价格
        double shopCartPrice = 0;
        for (int i = 0; i < productList.size(); i++) {
            shopCartPrice += productList.get(i).getProductNum() * productList.get(i).getProductPrice();
        }
        //2.根据活动ID查找活动规则
        List<LitePalEventRuleBean> eventRuleList =
                LitePal.where("eventid = ?", String.valueOf(eventID)).find(LitePalEventRuleBean.class);

        int multipleNum = BigDecimal.valueOf(shopCartPrice)
                .divide(BigDecimal.valueOf(NumUtils.getProductPrice(eventRuleList.get(0).getRuleReachValue())), 0, BigDecimal.ROUND_DOWN).intValue();

        return shopCartPrice - NumUtils.getProductPrice(eventRuleList.get(0).getRuleGiftValue()) * multipleNum;
    }

    /**
     * 获取阶梯满减的价格
     */
    private static double getLadderReductionPrice(long eventID, List<ShopCartBean.ProductListBean> productList) {
        //1.先获取全部的价格
        double shopCartPrice = 0;
        for (int i = 0; i < productList.size(); i++) {
            shopCartPrice += productList.get(i).getProductNum() * productList.get(i).getProductPrice();
        }
        //2.根据活动ID查找活动规则
        List<LitePalEventRuleBean> eventRuleList =
                LitePal.where("eventid = ?", String.valueOf(eventID)).find(LitePalEventRuleBean.class);

        if (eventRuleList.size() > 0) {
            List<LitePalEventRuleBean> eventRuleSortList = listSorting(eventRuleList);//从小到大的排序
            if (shopCartPrice < NumUtils.getProductPrice(eventRuleSortList.get(0).getRuleReachValue())) {
                //价格不满足最低限度
                return shopCartPrice;
            }
            if (shopCartPrice >= NumUtils.getProductPrice(eventRuleSortList.get(eventRuleSortList.size() - 1).getRuleReachValue())) {
                //价格处于最大限度或之上,就按照最大的来
                return shopCartPrice - NumUtils.getProductPrice(eventRuleSortList.get(eventRuleSortList.size() - 1).getRuleGiftValue());
            }

            for (int i = 0; i < eventRuleSortList.size(); i++) {
                //价格处于某个限度区间
                if (shopCartPrice >= NumUtils.getProductPrice(eventRuleSortList.get(i).getRuleReachValue()) &&
                        shopCartPrice < NumUtils.getProductPrice(eventRuleSortList.get(i + 1).getRuleReachValue())) {
                    return shopCartPrice - NumUtils.getProductPrice(eventRuleSortList.get(i).getRuleGiftValue());
                }
            }
        }
        return shopCartPrice;
    }

    /**
     * 获取满折的价格
     */
    private static double getFullDiscountPrice(long eventID, List<ShopCartBean.ProductListBean> productList) {
        //1.先获取全部的价格
        double shopCartPrice = 0;
        for (int i = 0; i < productList.size(); i++) {
            shopCartPrice += productList.get(i).getProductNum() * productList.get(i).getProductPrice();
        }
        //2.根据活动ID查找活动规则
        List<LitePalEventRuleBean> eventRuleList =
                LitePal.where("eventid = ?", String.valueOf(eventID)).find(LitePalEventRuleBean.class);

        return shopCartPrice
                * NumUtils.getProductPrice(eventRuleList.get(0).getRuleGiftValue())
                / 10;
    }

    /**
     * 获取其他的价格（正常商品、满送、满赠）
     */
    private static double getOtherPrice(List<ShopCartBean.ProductListBean> productList) {
        double shopCartPrice = 0;
        for (int i = 0; i < productList.size(); i++) {
            shopCartPrice += productList.get(i).getProductNum() * productList.get(i).getProductPrice();
        }
        return shopCartPrice;
    }

    /**
     * 获取返利的价格
     */
    public static double getRebatePrice(List<ShopCartBean> productList) {
        double rebate = productList.get(0).getProductList().get(0).getOnlineRebate();
        double rebateUnit = productList.get(0).getProductList().get(0).getOnlineRebateUnit();
        double rebatePrice = rebate / rebateUnit * getProductPrice(productList);
        return rebatePrice;
    }


    private static List<LitePalEventRuleBean> listSorting(List<LitePalEventRuleBean> listInAppxList) {
        Comparator<LitePalEventRuleBean> comparator = new Comparator<LitePalEventRuleBean>() {
            @Override
            public int compare(LitePalEventRuleBean details1, LitePalEventRuleBean details2) {
                //排序规则，按照价格由大到小顺序排列("<"),按照价格由小到大顺序排列(">"),
                if (details1.getRuleReachValue() < details2.getRuleReachValue())
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
}
