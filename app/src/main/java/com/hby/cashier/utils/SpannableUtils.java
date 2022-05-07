package com.hby.cashier.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.core.content.ContextCompat;

import com.hby.cashier.R;


/**
 * 功能介绍:字体相关设置
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/11/2 15:14
 * 最后编辑: 2020/11/2 - Hyy
 *
 * @author HouYinYu
 */
public class SpannableUtils {

    public static SpannableStringBuilder getSpannableBold(Context context, String showText) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(showText);
        //设置加粗
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, showText.indexOf("("), Spanned.SPAN_INCLUSIVE_EXCLUSIVE); //粗体
        return spannable;
    }

    /**
     * 单个价格的spannable
     * 只有￥字符小
     *
     * @param context
     * @param priceText
     * @return
     */
    public static SpannableStringBuilder getSpannable(Context context, double priceText) {
        String priceStr = "￥" + priceText;
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceStr);
        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 11)),
                0, priceStr.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 18)),
                priceStr.indexOf("￥") + 1, priceStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannable;
    }

    public static SpannableStringBuilder getSpannable(Context context, double priceText, int minSize, int maxSize) {
        String priceStr = "￥" + priceText;
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceStr);
        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, minSize)),
                0, priceStr.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, maxSize)),
                priceStr.indexOf("￥") + 1, priceStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannable;
    }

    /**
     * 含多个价格，但是以“起”表示
     *
     * @param context
     * @param firstPrice
     * @return
     */
    public static SpannableStringBuilder getSpannableDouble(Context context, double firstPrice) {
        String priceStr = "￥" + firstPrice + "起";
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceStr);
        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 11)),
                0, priceStr.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 18)),
                priceStr.indexOf("￥") + 1, priceStr.indexOf("起") + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 11)),
                priceStr.indexOf("起"), priceStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        //设置加粗
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, priceStr.indexOf("起"), Spanned.SPAN_INCLUSIVE_EXCLUSIVE); //粗体
        return spannable;
    }

    /**
     * 含多个价格，以第一个和最后个价格表示，中间以“~”连接
     *
     * @param context
     * @param firstPrice
     * @param secondPrice
     * @return
     */
    public static SpannableStringBuilder getSpannable(Context context, double firstPrice, double secondPrice) {
        String priceStr = "￥" + firstPrice + "~￥" + secondPrice;
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceStr);
        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 11)),
                0, priceStr.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 18)),
                priceStr.indexOf("￥") + 1, priceStr.indexOf("~") + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 11)),
                priceStr.indexOf("~") + 1, priceStr.lastIndexOf("￥") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 18)),
                priceStr.lastIndexOf("￥") + 1
                , priceStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        return spannable;
    }

    /**
     * 订单详情的价格
     *
     * @param context
     * @param onlinePrice
     * @return
     */
//    public static SpannableStringBuilder getSpannableOrder(Context context, double onlinePrice) {
//        return getSpannableOrder(context, onlinePrice, R.color.color_28, R.color.color_1f, R.color.color_29, 13, 18, 11);
//    }
    public static SpannableStringBuilder getSpannableOrder(Context context, double onlinePrice,
                                                           int startColor, int centreColor, int endColor,
                                                           int startSize, int centreSize, int endSize) {
        String priceStr = "￥" + onlinePrice;
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceStr);
        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, startSize)),
                0, priceStr.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, centreSize)),
                priceStr.indexOf("￥") + 1, priceStr.indexOf("."), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, endSize)),
                priceStr.indexOf(".") + 1, priceStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置不同字体颜色
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, startColor)), 0,
                priceStr.indexOf("￥") + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, centreColor)),
                priceStr.indexOf("￥") + 1, priceStr.indexOf("."), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, endColor)),
                priceStr.indexOf("."), priceStr.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        //设置加粗
        spannable.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), priceStr.indexOf("￥") + 1,
                priceStr.indexOf("."), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannable;
    }

    /**
     * 单个价格
     * ￥字符和.之后的价格小，中间大
     *
     * @param context
     * @param onlinePrice
     * @return
     */
    public static SpannableStringBuilder getSpannablePoint(Context context, double onlinePrice) {
        String priceStr = "￥" + onlinePrice;
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceStr);
        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 11)),
                0, priceStr.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 19)),
                priceStr.indexOf("￥") + 1, priceStr.indexOf("."), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, 11)),
                priceStr.indexOf(".") + 1, priceStr.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannable;
    }


    /**
     * 商品列表的线上价显示
     * 设置不同字大小和颜色
     *
     * @return
     */
//    public static SpannableStringBuilder getSpannable(Context context, String priceText) {
//        SpannableStringBuilder spannable = getSpannable(context, R.color.color_333, R.color.color_red,
//                12, 16, priceText);
//        return spannable;
//    }
//
//    public static SpannableStringBuilder getSpannable(Context context, int minSize,
//                                                      int maxSize, String priceText) {
//        SpannableStringBuilder spannable = getSpannable(context, R.color.color_333, R.color.color_red,
//                minSize, maxSize, priceText);
//        return spannable;
//    }
    public static SpannableStringBuilder getSpannable(Context context, int colorStart, int colorEnd, int minSize,
                                                      int maxSize, String priceText) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceText);
        CharacterStyle textColor =
                new ForegroundColorSpan(context.getResources().getColor(colorStart));
        CharacterStyle priceColor =
                new ForegroundColorSpan(context.getResources().getColor(colorEnd));
        //设置不同字体颜色
        if (priceText.contains("：")) {
            spannable.setSpan(textColor, 0, priceText.indexOf("：") + 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannable.setSpan(priceColor, priceText.indexOf("：") + 1, priceText.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, minSize)),
                0, priceText.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, maxSize)),
                priceText.indexOf("￥") + 1, priceText.indexOf("."), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        if (priceText.contains("~")) {
            spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, minSize)),
                    priceText.indexOf("."), priceText.lastIndexOf("￥") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, maxSize)),
                    priceText.lastIndexOf("￥") + 1
                    , priceText.lastIndexOf("."), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, minSize)),
                priceText.lastIndexOf("."), priceText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

    //
//    public static SpannableStringBuilder getSpannable2(Context context, String priceText) {
//        SpannableStringBuilder spannable = getSpannable2(context, R.color.color_333, R.color.color_red,
//                12, 16, priceText);
//        return spannable;
//    }

    public static SpannableStringBuilder getSpannable2(Context context, int colorStart, int colorEnd, int minSize,
                                                       int maxSize, String priceText) {
        SpannableStringBuilder spannable = new SpannableStringBuilder(priceText);
        CharacterStyle textColor =
                new ForegroundColorSpan(context.getResources().getColor(colorStart));
        CharacterStyle priceColor =
                new ForegroundColorSpan(context.getResources().getColor(colorEnd));
        //设置不同字体颜色
        if (priceText.contains("：")) {
            spannable.setSpan(textColor, 0, priceText.indexOf("：") + 1,
                    Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannable.setSpan(priceColor, priceText.indexOf("：") + 1, priceText.length(),
                    Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }

        //设置不同字体大小
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, minSize)),
                0, priceText.indexOf("￥") + 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, maxSize)),
                priceText.indexOf("￥") + 1, priceText.indexOf("."), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        if (priceText.contains("~")) {
            spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, minSize)),
                    priceText.indexOf("."), priceText.lastIndexOf("￥") + 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, maxSize)),
                    priceText.lastIndexOf("￥") + 1
                    , priceText.lastIndexOf("."), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        spannable.setSpan(new AbsoluteSizeSpan(DensityUtils.dip2px(context, minSize)),
                priceText.lastIndexOf("."), priceText.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannable;
    }

}
