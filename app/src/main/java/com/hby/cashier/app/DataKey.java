package com.hby.cashier.app;

/**
 * 功能介绍:获取全局变量的KEY
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/14 14:16
 * 最后编辑: 2020/12/14 - Hyy
 *
 * @author HouYinYu
 */
public class DataKey {
    //是否第一次使用
    public  static String KEY_FIRST="isFirst";
    //是否第一次登录
    public  static String KEY_LOGIN="isLogin";

    //是否第一次登录
    public  static String KEY_DEVICE_ID="device_id";

    //当前登录账号
    public  static String KEY_LOGIN_ACCOUNT="login_account";
    //当前登录密码
    public  static String KEY_LOGIN_PASS="login_pass";
    //当前登录sessionID
    public  static String KEY_SESSION_ID="sessionID";
    //当前登录的店铺名称
    public  static String KEY_SHOP_NAME="shopName";
    //当前登录的店铺ID
    public  static String KEY_SHOP_ID="shopID";
    //当前登录的serviceID
    public  static String KEY_SERVICE_ID="serviceID";
    //当前登录账号的收银员姓名
    public  static String KEY_USER_NAME="userName";
    //当前登录账号的MID
    public  static String KEY_USER_MID="userMID";
    //当前登录账号的ID
    public  static String KEY_USER_ID="userID";

    //当前登录账号的数据同步频率
    public  static String KEY_DATA_SYNC="dataSync";
    //当前登录是否需要交接班
    public  static String KEY_IS_CHANGE="isChange";
    //当前需要无码商品
    public  static String KEY_IS_CODE_LESS="isCodeLess";
    //当前是否需要语音提示
    public  static String KEY_IS_VOICE="isPayVoice";
    //当前是否需要副屏
    public  static String KEY_IS_SECOND_SCREEN="isSecondPrice";

    //当前账号登录时间-交接班记录使用
    public  static String KEY_LOGIN_TIME_STAMP="login_time_stamp";

    //当前是否锁屏
    public  static String KEY_IS_LOCK="isLock";

}
