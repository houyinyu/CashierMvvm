package com.hby.cashier.utils;


/**
 * 功能介绍:
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/4/9 15:29
 * 最后编辑: 2020/4/9 - Hyy
 *
 * @author HouYinYu
 */
public class EnumUtils {

    /**
     * 登录类型
     */
    public enum LoginType {
        ;
        public static final String LOGIN_ALL = "A";//商城后台都能登录
        public static final String LOGIN_MANAGE = "H";//只能登录后台
        public static final String LOGIN_MERCHANT = "S";//只能登录商城
    }

    /**
     * 支付的类型
     */
    public enum PayMethod {
        ;
        public static final String PAY_ALIPAY = "ALIPAY";//支付宝支付
        public static final String PAY_WXPAY = "WXPAY";//微信支付
        public static final String PAY_CASH = "CASH";//现金
        public static final String PAY_FREE = "FREE";//免单
        public static final String PAY_RETURN = "RETURN";//反结算（用来交接班那里显示）
        public static final String PAY_NON = "NON";//取单那个打印使用
    }

    public static String getPaymentMethodStr(String paymentMethod) {
        switch (paymentMethod) {
            case EnumUtils.PayMethod.PAY_WXPAY:
                return "微信收款";
            case EnumUtils.PayMethod.PAY_ALIPAY:
                return "支付宝收款";
            case EnumUtils.PayMethod.PAY_CASH:
                return "现金收款";
            case EnumUtils.PayMethod.PAY_FREE:
                return "免单";
            case EnumUtils.PayMethod.PAY_RETURN:
                return "反结算";
        }
        return "";
    }


    /**
     * 支付的
     */
    public enum PayType {
        ;
        public static final String PAY_ONLINE = "ONLINE";//线上支付
        public static final String PAY_OFFLINE = "OFFLINE";//线下支付
    }

    /**
     * 支付的状态
     */
    public enum PayStatus {
        ;
        public static final int PAY_SUCCESS = 1;//已支付
        public static final int PAY_FAIL = 0;//未支付
    }


    /**
     * 商品活动标签
     */
    public enum EventType {
        ;
        public static final int EVENT_FULL_GIVE = 10;//每满量送  Full give
        public static final int EVENT_LADDER_GIVE = 12;//阶梯送 Ladder give
        public static final int EVENT_FULL_GIFT = 20;//每满额赠 Full gift
        public static final int EVENT_LADDER_GIFT = 22;//阶梯赠 Ladder gift
        public static final int EVENT_FULL_REDUCTION = 30;//每满额减 Full reduction
        public static final int EVENT_LADDER_REDUCTION = 32;//阶梯减  Ladder reduction
        public static final int EVENT_LADDER_DISCOUNT = 40;//阶梯折 Ladder discount
        public static final int EVENT_SPECIAL_OFFER = 50;//特价 special offer
    }

    /**
     * 活动文字显示
     *
     * @param type
     * @return
     */
    public static String getEventStr(int type) {
        switch (type) {
            case EventType.EVENT_FULL_GIVE:
                return "每满量送";
            case EventType.EVENT_LADDER_GIVE:
                return "阶梯送";
            case EventType.EVENT_FULL_GIFT:
                return "每满额赠";
            case EventType.EVENT_LADDER_GIFT:
                return "阶梯赠";
            case EventType.EVENT_FULL_REDUCTION:
                return "每满额减";
            case EventType.EVENT_LADDER_REDUCTION:
                return "阶梯减";
            case EventType.EVENT_LADDER_DISCOUNT:
                return "折扣";//阶梯折
            case EventType.EVENT_SPECIAL_OFFER:
                return "特价";
            default:
                return "活动";
        }
    }


    /**
     * 订单的状态
     */
    public enum OrderType {
        ;
        public static final int ORDER_EXAMINE = 10;//待审核
        public static final int ORDER_DELIVERY = 20;//待出库
        public static final int ORDER_NO_SETOUT = 30;//待出发
        public static final int ORDER_IS_SETOUT = 40;//已出发(配送中)
        public static final int ORDER_FINISH = 50;//已完成
        public static final int ORDER_VOID = 60;//已作废
        public static final int ORDER_CANCEL = 70;//已取消
    }
    /**
     * 订单状态的文字显示
     *
     * @param type
     * @return
     */
    public static String getOrderStatusStr(int type) {
        switch (type) {
            case OrderType.ORDER_EXAMINE:
                return "待审核";
            case OrderType.ORDER_DELIVERY:
                return "待出库";
            case OrderType.ORDER_NO_SETOUT:
                return "待出发";
            case OrderType.ORDER_IS_SETOUT:
                return "配送中";
            case OrderType.ORDER_FINISH:
                return "已完成";
            case OrderType.ORDER_VOID:
                return "已作废";
            case OrderType.ORDER_CANCEL:
                return "已取消";
            default:
                return "无效状态";
        }
    }

}
