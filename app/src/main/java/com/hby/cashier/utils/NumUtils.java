package com.hby.cashier.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;


import com.alibaba.fastjson.JSON;
import com.hby.cashier.R;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.bean.ProductGroupBean;
import com.hyy.mvvm.utils.ToastUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 功能介绍:数字相关转换  <br/>
 * 调用方式: / <br/>
 * <p/>
 * 作   者: Hyy - 825129541@qq.com <br/>
 * 创建电脑: 82512  <br/>
 * 创建时间: 2019/11/25 16:23 <br/>
 * 最后编辑: 2019/11/25 - Hyy
 *
 * @author HouYinYu
 */
public class NumUtils {
    /**
     * 返回两位小数的字符串
     *
     * @param num
     * @return
     */
    public static String getDoubleStr(Double num) {
        DecimalFormat myformat = new DecimalFormat("0.##");
        //特殊情况-只有三位小数，且最后位为5，它会省去
        if (getDoublePoint(num) == 3) {
            num = num + 0.001;
//            LogUtils.i("这个数字有三位小数，且最后位是5");
        }
        return myformat.format(num);
    }

    private static int getDoublePoint(double num) {
        int a = (String.valueOf(num)).length() - (String.valueOf(num)).indexOf(".") - 1;
        return a;
    }

    /**
     * 返回两位小数的double
     *
     * @param num
     * @return
     */
    public static Double getDouble(Double num) {
        //这里是把double转为两位小数的double
        return Double.parseDouble(getDoubleStr(num));
    }

    public static Double getDouble(String num) {
        //这里是把str的double转换为double类型
        return Double.parseDouble(num);
    }

    public static String getDoubleStr(String num) {
        //这里是把str的double转换为 string 类型并保留两位小数
        double dNum = getDouble(num);
        return getDoubleStr(dNum);
    }

    public static Double getProductPrice(long price) {
        Double priceDouble = BigDecimal.valueOf(price)
                .divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return priceDouble;
    }

    public static Double getProductPrice(LitePalProductBean bean) {
        if (bean.getSpecialPrice() > 0) {
            return bean.getSpecialPrice();
        }
        Double priceDouble = BigDecimal.valueOf(getGroupPrice(bean))
                .divide(new BigDecimal("10000"), 2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return priceDouble;
    }

    public static Long getGroupPrice(LitePalProductBean bean) {
        List<ProductGroupBean> groupList = JSON.parseArray(bean.getGradePrice(), ProductGroupBean.class);
        for (int i = 0; i < groupList.size(); i++) {
            if (groupList.get(i).getGroupId().equals(bean.getGroupId())) {
                return groupList.get(i).getOnlinePrice();
            }
        }
        return 0L;
    }

    /**
     * 判断是否是正常的价格
     *
     * @param priceStr
     */
    public static boolean isPrice(String priceStr) {
        Pattern pattern = Pattern.compile("^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$");
        Matcher match = pattern.matcher(priceStr);
        if (!match.matches()) {
            return false;
        }
        return true;
    }

}
