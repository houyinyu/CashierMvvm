package com.hby.cashier.utils;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.View;

import java.lang.reflect.Method;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/21
 */
public class DeviceUtils {

    public static String getSN(Context context) {
//        return "TEST_123";
//        return "SC0221AS00112";
//        return getIMEI(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            String serial = null;
            try {
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.sunmi.serial");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return serial;
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Build.getSerial();
        } else {
            return Build.SERIAL;
        }
    }
    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei;
    }


    public static void setStickFullScreen(View view) {
        int systemUiVisibility = view.getSystemUiVisibility();
        int flags = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        systemUiVisibility |= flags;
        view.setSystemUiVisibility(systemUiVisibility);
    }
}
