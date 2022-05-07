package com.hby.cashier.http;

/**
 * 功能介绍:请求接口统计
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/11/25 9:20
 * 最后编辑: 2020/11/25 - Hyy
 *
 * @author HouYinYu
 */
public class RequestConfig {
    public static final String getVersionsPlus = "/centerCore/versionsPlus/versionsPlusSy";//apk新版本获取地址
    public static final String request_login = "/base/user/login";//登录
    public static final String request_downZip = "/datasync/zipDownLoad";//下载文件
    public static final String request_createOrder = "/centerCore/orderMain/syOrderCreate";//收银订单创建
    public static final String request_getPayStatus = "/centerCore/orderMain/finOrdNoByIsPayed";//轮询支付状态
    public static final String request_scanGunPay = "/payYsepay/user/scan";//扫码枪扫码支付
    public static final String request_uploadChangeShifts = "/base/changeShifts/uploadChangeShifts";//交接班上行
    public static final String request_getServiceTime = "/base/syDeviceManagement/getTime";//订单上行的时间判断
    public static final String request_orderQuery = "/centerCore/orderMain/syOrderQuery";//销售记录
    public static final String request_selectOrderDetail = "/centerCore/orderMain/selectOrderDetail";//订单查询
    public static final String request_orderStatistics = "/centerCore/orderMain/syOrderStatistics";//销售统计
    public static final String request_orderBackCreate = "/centerCore/orderBack/OrderBackCreate";//创建退单
    public static final String request_orderDrop = "/centerCore/orderBack/orderDrop";//订单作废-
}
