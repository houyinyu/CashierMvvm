package com.hby.cashier.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;


public class PackageUtils {
    /**
     * 获取安装应用包名
     */
    public static final String getPackageName(Context context) {
        String version = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo info;
        try {
            info = pm.getPackageInfo(context.getPackageName(),
                    0);
            if (info != null) {
                version = String.valueOf(info.packageName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取安装应用版本名
     */
    public static final String getVersionName(Context context) {
        String version = "";
        PackageManager pm = context.getPackageManager();
        PackageInfo info;
        try {
            info = pm.getPackageInfo(context.getPackageName(),
                    0);
            if (info != null) {
                version = info.versionName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return version;
    }

    /**
     * 获取安装应用版本号
     */
    public static int getVersionCode(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo info = pm.getPackageInfo(
                    context.getPackageName(), 0);
            if (info != null) {
                return info.versionCode;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
