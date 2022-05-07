package com.hby.cashier.utils;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能介绍:杂七杂八的工具
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/11/16 17:05
 * 最后编辑: 2020/11/16 - Hyy
 *
 * @author HouYinYu
 */
public class OtherUtils {

    /**
     * 验证字符串是否为空
     *
     * @param str 传入的字符串
     * @return 为空返回true 否则false
     */
    public static boolean isTextEmpty(String str) {
        if (TextUtils.isEmpty(str) || "null".equals(str) || TextUtils.isEmpty(str.trim()) || "null".equals(str.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 2020-6-18
     * zs说我们这边只做两位验证，11和12不行，其他的给通联验证
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile
                ("^(1[3-9])\\d{9}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 跳转到拨号界面
     *
     * @param context
     * @param phoneNum
     */
    public static void callPhone(Context context, String phoneNum) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        Uri data = Uri.parse("tel:" + phoneNum);
        intent.setData(data);
        context.startActivity(intent);
    }


    /**
     * 手机号加星
     */
    public static String resetMobile(String mobiles) {
        String mobileStr = mobiles;
        if (mobiles == null) {
            return "";
        }
        if (mobiles.length() == 11) {
            String mobileStr1 = mobiles.substring(0, 3);
            String mobileStr2 = "****";
            String mobileStr3 = mobiles.substring(7);
            mobileStr = mobileStr1 + mobileStr2 + mobileStr3;
        }
        return mobileStr;
    }


    /**
     * 复制
     *
     * @param context
     * @param copyStr
     */
    public static void copyStr(Context context, String copyStr) {
        //获取剪贴板管理器：
        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建普通字符型ClipData
        ClipData mClipData = ClipData.newPlainText("Label", copyStr);
        // 将ClipData内容放到系统剪贴板里。
        if (cm != null)
            cm.setPrimaryClip(mClipData);
        Toast.makeText(context, "复制成功", Toast.LENGTH_SHORT).show();
    }


    public static void setTextBold(TextView textView, boolean isBold) {
        //android中为textview动态设置字体为粗体
        if (isBold) {
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        } else {
            //设置不为加粗
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

    }
}
