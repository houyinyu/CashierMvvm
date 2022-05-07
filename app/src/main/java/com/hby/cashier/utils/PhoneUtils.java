package com.hby.cashier.utils;

import android.os.Build;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 功能介绍:获取手机厂商等信息
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2021/12/23 9:54
 * 最后编辑: 2021/12/23 - Hyy
 *
 * @author HouYinYu
 */
public class PhoneUtils {
    public static final String PHONE_XIAOMI = "xiaomi";
    public static final String PHONE_HUAWEI = "huawei";
    public static final String PHONE_OPPO = "oppo";
    public static final String PHONE_MEIZU = "meizu";
    public static final String PHONE_SAMSUNG = "samsung";
    public static final String PHONE_SONY = "sony";
    public static final String PHONE_LG = "lg";
    public static final String PHONE_LETV = "letv";
    public static final String PHONE_QIKU = "qiku";
    public static final String PHONE_360 = "360";

    /**
     * 通过反射获取MIUI版本号
     *
     * @return
     */
    public static String checkMIUI() {
        String versionCode = "";
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        LogUtils.i("Build.MANUFACTURER = " + manufacturer + " ,Build.MODEL = " + Build.MODEL);
        if (!TextUtils.isEmpty(manufacturer) && manufacturer.equals("Xiaomi")) {
            versionCode = getSystemProperty("ro.miui.version.code_time");
        }
        return versionCode;
    }

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            LogUtils.i("Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    LogUtils.i("Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }
}
