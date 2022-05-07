package com.hby.cashier.app;


import android.os.Environment;

import com.hby.cashier.BuildConfig;

import java.io.File;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/21
 */
public class AppConfig {

    /**
     * 拼接请求的URL
     *
     * @param url
     * @return
     */
    public static String mergeUrl(String url) {
        return BASE_NET_URL + url;
    }

    public static String qiUrl(String url) {
        return IMAGE_QINIU_URL + url;
    }

    public static String mergeUrlHtmlUrl(String s) {
        return BASE_WEB_URL + s;
    }

    public static String IMAGE_QINIU_URL = BuildConfig.QINIU_URL; //七牛云图片地址
    public static String BASE_NET_URL = BuildConfig.NET_URL;//接口地址
    public static String BASE_DOWNLOAD_URL = BuildConfig.DOWNLOAD_URL;//数据下载地址
    public static String BASE_WEB_URL = BuildConfig.WEB_URL;//Html地址
    public static String SIGN_KEY = BuildConfig.SIGN_KEY;//签名

    public static String DATA_PASS_KEY = BuildConfig.DATA_PASS_KEY;//数据库加密

    public static String CASHIER_SAVE_PATH = Environment
            .getExternalStorageDirectory().toString()
            + File.separator
            + "cashier"
            + File.separator;//zip保存地址
    public static String ZIP_PARENT_PATH = "zip";//zip文件保存地址xxx/zip/
    public static String ZIP_FILE_NAME = "data.zip";//保存zip的名字
    public static String TEXT_FILE_PATH = "text";//保存解压txt文件地址的后缀xxx/ziptext/xxx.txt
    public static String IMAGE_FILE_PATH = "image";//保存banner图的地址
}
