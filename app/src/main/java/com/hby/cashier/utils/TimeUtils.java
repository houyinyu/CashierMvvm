package com.hby.cashier.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.widget.DatePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * TimeUtils
 *
 * @author masai
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtils {

    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * yyyy-MM-dd 日期
     */
    public static final String DATE_PATTERN = "yyyy-MM-dd";
    public static final SimpleDateFormat DATE_FORMAT_DATE = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * yyyy-MM-dd 日期
     */
    public static final String TIME_PATTERN = "HH:mm";
    public static final SimpleDateFormat DATE_FORMAT_H_m = new SimpleDateFormat("HH:mm");

    private TimeUtils() {
        throw new AssertionError();
    }

    /**
     * long time to string
     *
     * @param timeInMillis
     * @param dateFormat
     */
    public static String getTime(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }

    public static String getTime(Date date, SimpleDateFormat dateFormat) {
        return dateFormat.format(date);
    }

    public static String getTime(String timeStr, String formatStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        try {
            Date date = dateFormat.parse(timeStr);
            return dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStr;
    }

    /**
     * long time to string, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @param timeInMillis
     * @return 返回形如  yyyy-MM-dd HH:mm:ss 的时间字符串
     */
    public static String getTime(long timeInMillis) {
        return getTime(timeInMillis, DEFAULT_DATE_FORMAT);
    }

    /**
     * get current time in milliseconds
     *
     * @return 返回系统的时间戳
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     *
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTime(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     *
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTime(getCurrentTimeInLong(), dateFormat);
    }

    /**
     * 比较时间,
     *
     * @param start 请传递 yyyy-MM-dd HH:mm:ss否则无法格式化
     * @param end
     * @return start时间比end大。则返回true,否则返回false
     */
    public static boolean BiJiaoShiJian(String start, String end) {
        try {
            Date startDate = DEFAULT_DATE_FORMAT.parse(start);
            Date endDate = DEFAULT_DATE_FORMAT.parse(end);
            if (startDate.getTime() > endDate.getTime()) return true;
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * end大就返回true
     *
     * @param startTime
     * @param endTime
     * @param formatStr
     * @return
     */
    public static boolean compareTime(String startTime, String endTime, String formatStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        try {
            Date startDate = dateFormat.parse(startTime);
            Date endDate = dateFormat.parse(endTime);
            if (startDate != null && endDate != null) {
                if (startDate.getTime() < endDate.getTime()) return true;
            }
            return false;
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将指定的毫秒值返回为 天。时分秒 毫秒
     *
     * @param mss
     * @return x天
     */
    public static String formatDuring(long mss) {

        long days = mss / (1000 * 60 * 60 * 24);//天
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);//时
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);//分
        long seconds = (mss % (1000 * 60)) / 1000;//秒
        //        long milliSecond = mss - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60) - minutes * (1000 * 60) - seconds *
        // 1000;//毫秒
        if (days == 0) {
            return ((hours < 10) ? "0" + hours : hours) + ":" + ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0"
                    + seconds : seconds);//+ "."
            //                    (milliSecond / 100);
        } else {
            hours = (days * 24 + hours);

            return hours + ":" + ((minutes < 10) ? "0" + minutes : minutes) + ":" + ((seconds < 10) ? "0" + seconds : seconds);
            //            return (days + "天 ") + ((hours < 10) ? "0" + hours : hours) + ":" + ((minutes < 10) ? "0" + minutes : minutes)
            // + ":" + (
            //                    (seconds < 10) ? "0" + seconds : seconds);// + "." +
            //                    (milliSecond / 100);
        }
    }

    /**
     * 获取当年的第一天
     *
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * framat 转 时间戳
     *
     * @return 格式化的数据库
     */
    public static String DateToTime(String s) {
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        res = String.valueOf(date.getTime());
        return res;
    }

    /**
     * 如果要学习获取时间。。。看我的Year属性
     *
     * @return
     */
    public static String getTo2NianQian() {
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, -3);
        date = calendar.getTime();
        return DEFAULT_DATE_FORMAT.format(date);
    }


    /**
     * 获取昨天凌晨开始的时间 2017-4-18 00:00:00
     *
     * @return @Link DEFAULT_DATE_FORMAT
     */
    public static String getJinTianLingChenShiJian() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return DEFAULT_DATE_FORMAT.format(new Date(cal.getTimeInMillis()));
    }

    /**
     * 获取昨天最晚的时间 2017-4-18 23:59:59
     *
     * @return
     */
    public static String getZuoTianZuiWanShiJian() {
        try {
            Date date = new Date();
            Calendar cal_1 = Calendar.getInstance();
            //获取前一天的时间戳
            String format = DEFAULT_DATE_FORMAT.format(date);
            String[] split = format.split("-");//yyyy-MM-dd HH:mm:ss
            String[] split1 = split[2].split(" ");//dd HH:mm:ss
            if (split1[0].equals("0")) {
                return DEFAULT_DATE_FORMAT.format(cal_1.getTime());
            } else {
                SimpleDateFormat yearAndMonth = new SimpleDateFormat("yyyy-MM");
                char[] chars = split1[0].toCharArray();
                if (chars[0] == '0') {//第一位是否是0
                    int i = Integer.parseInt(String.valueOf(chars[1])) - 1;
                    if (i == 0) i++;
                    String yearMonth = yearAndMonth.format(date);
                    Date lastMonthDate = DEFAULT_DATE_FORMAT.parse(yearMonth + "-0" + i + " 23:59:59");
                    cal_1.setTime(lastMonthDate);
                    return DEFAULT_DATE_FORMAT.format(cal_1.getTime());
                } else {
                    int lastDay = Integer.parseInt(split1[0]) - 1;
                    //如果昨天是9号就必须得补齐0
                    Date lastMonthDate = DEFAULT_DATE_FORMAT.parse(yearAndMonth.format(date) + "-" + ((lastDay == 9) ? "0" : "") +
                            lastDay + " 23:59:59");
                    cal_1.setTime(lastMonthDate);
                    return DEFAULT_DATE_FORMAT.format(cal_1.getTime());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取本月1号的时间。形如 2017-4-1 00:00:00
     *
     * @return
     */
    public static String getBenYue1Hao() {
        Date date = new Date();
        SimpleDateFormat yearAndMonth = new SimpleDateFormat("yyyy-MM");
        String tempTime = yearAndMonth.format(date);
        tempTime += "-1 00:00:00";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentMonthDate = null;
        try {
            currentMonthDate = DEFAULT_DATE_FORMAT.parse(tempTime);
            Calendar cal_1 = Calendar.getInstance();
            cal_1.setTime(currentMonthDate);
            return DEFAULT_DATE_FORMAT.format(cal_1.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取上个月 1号 凌晨的时间
     *
     * @return
     */
    public static String getShangGeYueLingChen() {
        SimpleDateFormat lastMonth = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return lastMonth.format(calendar.getTime()) + " 00:00:00";
    }

    /**
     * 时间戳转换成字符窜
     *
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    /**
     * 字符串转时间戳
     *
     * @param dateString
     * @return
     */
    public static long getStringToDate(String dateString) {
        return getStringToDate(dateString,DEFAULT_DATE_PATTERN);
    }
    public static long getStringToDate(String dateString, String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        Date date = new Date();
        try {
            date = dateFormat.parse(dateString);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date.getTime();
    }

    //直接传时间戳
    public static CountDownTimer DateCountDown(long timeStamp, OnDateCountDownListener downListener) {
        CountDownTimer timer = new CountDownTimer(timeStamp, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long day = millisUntilFinished / (1000 * 60 * 60 * 24);//天
                long hour = (millisUntilFinished - day * (1000 * 60 * 60 * 24))
                        / (1000 * 60 * 60);//小时
                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60))
                        / (1000 * 60); //单位分
                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60))
                        / 1000;//单位秒

                String hourStr = String.valueOf(hour).length() == 1 ? ("0" + hour) : String.valueOf(hour);
                String minuteStr = String.valueOf(minute).length() == 1 ? ("0" + minute) : String.valueOf(minute);
                String secondStr = String.valueOf(second).length() == 1 ? ("0" + second) : String.valueOf(second);
                downListener.timeOn(day, hourStr, minuteStr, secondStr);
            }

            @Override
            public void onFinish() {
                cancel();
                downListener.timeEnd();
            }
        };
        timer.start();
        return timer;
    }

    //传递时间，自己来算时间差
    public static CountDownTimer DateCountDown(String startDate, String endDate, OnDateCountDownListener downListener) {
        String timePattern = "yyyy-MM-dd HH:mm:ss";
        long startStamp = getStringToDate(startDate, timePattern);
        long endStamp = getStringToDate(endDate, timePattern);
        long timeStamp = endStamp - startStamp;

        CountDownTimer timer = new CountDownTimer(timeStamp, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long day = millisUntilFinished / (1000 * 60 * 60 * 24);//天
                long hour = (millisUntilFinished - day * (1000 * 60 * 60 * 24))
                        / (1000 * 60 * 60);//小时
                long minute = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60))
                        / (1000 * 60); //单位分
                long second = (millisUntilFinished - day * (1000 * 24 * 60 * 60) - hour * (1000 * 60 * 60) - minute * (1000 * 60))
                        / 1000;//单位秒

                String hourStr = String.valueOf(hour).length() == 1 ? ("0" + hour) : String.valueOf(hour);
                String minuteStr = String.valueOf(minute).length() == 1 ? ("0" + minute) : String.valueOf(minute);
                String secondStr = String.valueOf(second).length() == 1 ? ("0" + second) : String.valueOf(second);
                downListener.timeOn(day, hourStr, minuteStr, secondStr);
            }

            @Override
            public void onFinish() {
                cancel();
                downListener.timeEnd();
            }
        };
        timer.start();
        return timer;
    }

    public interface OnDateCountDownListener {
        void timeOn(long day, String hour, String minute, String second);

        void timeEnd();
    }

    private OnDateCountDownListener downListener;

    public void setOnDateCountDownListener(OnDateCountDownListener downListener) {
        this.downListener = downListener;
    }


    /**
     * 判断两个时间是否同一天
     *
     * @param millis1
     * @param millis2
     * @param timeZone
     * @return
     */
    public static boolean isSameDay(long millis1, long millis2, TimeZone timeZone) {
        long interval = millis1 - millis2;
        return interval < 86400000 && interval > -86400000 && millis2Days(millis1, timeZone) == millis2Days(millis2, timeZone);
    }

    private static long millis2Days(long millis, TimeZone timeZone) {
        return (((long) timeZone.getOffset(millis)) + millis) / 86400000;
    }


    /**
     * 时间戳的时间差
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static long subtractStamp(String startDate, String endDate) {
        if (startDate == null || startDate.equals("")) return 0;
        if (endDate == null || endDate.equals("")) return 0;
        String timePattern = "yyyy-MM-dd HH:mm:ss";
        long startStamp = getStringToDate(startDate, timePattern);
        long endStamp = getStringToDate(endDate, timePattern);
        long timeStamp = endStamp - startStamp;
        return timeStamp;
    }

    /**
     * 获取时间差-并转换为时分秒
     * @param startStamp
     * @param endStamp
     * @return
     */
    public static long differenceStamp(long startStamp, long endStamp) {
        long difference = startStamp - endStamp;
        long differenceStamp = Math.abs(difference);

        Long day = differenceStamp / (1000 * 60 * 60 * 24); //以天数为单位取整
        Long hour = (differenceStamp / (60 * 60 * 1000) - day * 24); //以小时为单位取整
        Long min = ((differenceStamp / (60 * 1000)) - day * 24 * 60 - hour * 60); //以分钟为单位取整
//        Long second = (differenceStamp / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);//秒
        return min;
    }

    /**
     * 获取当前时间
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    public static String getCurrentTime(String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);// HH:mm:ss
        // 获取当前时间
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }


    /**
     * 本周开始时间-周一
     *
     * @param formatStr
     * @return
     */
    public static String getTimeOfWeekStart(String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);// HH:mm:ss
//
//        Calendar calendar = Calendar.getInstance();
//
//        int weekday = calendar.get(Calendar.DAY_OF_WEEK) - 2;
//        LogUtils.i("***********************************weekday:"+weekday);
//        calendar.add(Calendar.DATE, -weekday);

        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar. DAY_OF_WEEK) - 1;
        if (day_of_week == 0 ) {
            day_of_week = 7 ;
        }
        cal.add(Calendar.DATE , -day_of_week + 1 );

        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * 本周结束时间-周日
     *
     * @param formatStr
     * @return
     */
    public static String getTimeOfWeekEnd(String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);// HH:mm:ss

//        Calendar calendar = Calendar.getInstance();
//
//        int weekday = calendar.get(Calendar.DAY_OF_WEEK);
//        calendar.add(Calendar.DATE, 8 - weekday);

        Calendar cal = Calendar.getInstance();
        int day_of_week = cal.get(Calendar. DAY_OF_WEEK) - 1;
        if (day_of_week == 0 ) {
            day_of_week = 7 ;
        }
        cal.add(Calendar.DATE , -day_of_week + 7 );

        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * 本月开始时间
     *
     * @param formatStr
     * @return
     */
    public static String getTimeOfMonthStart(String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);// HH:mm:ss

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();

        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * 本月结束时间
     *
     * @param formatStr
     * @return
     */
    public static String getTimeOfMonthEnd(String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);// HH:mm:ss

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.getTime();

        cal.set(Calendar.DAY_OF_MONTH,
                cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        return simpleDateFormat.format(cal.getTime());
    }

    /**
     * 本年开始时间
     *
     * @param formatStr
     * @return
     */
    public static String getTimeOfYearStart(String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);// HH:mm:ss

        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);


        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 本年结束时间
     *
     * @param formatStr
     * @return
     */
    public static String getTimeOfYearEnd(String formatStr) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatStr);// HH:mm:ss

        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);

        return simpleDateFormat.format(calendar.getTime());
    }

    /**
     * 显示日期选择器, 默认白色背景
     */
    public static void showDatePickerDialog(Context context, String title, int year, int month, int day, OnDatePickerListener onDateTimePickerListener) {
        showDatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, title, year, month, day, onDateTimePickerListener);
    }

    public static void showDatePickerDialog(Context context, String title, OnDatePickerListener onDateTimePickerListener) {
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH) + 1;
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);
        showDatePickerDialog(context, title, year, month, day, onDateTimePickerListener);
    }

    /**
     * 显示日期选择器
     */
    public static void showDatePickerDialog(Context context, int themeId, String title, int year, int month, int day,
                                            final OnDatePickerListener onDateTimePickerListener) {
        DatePickerDialog mDatePickerDialog = new DatePickerDialog(context, themeId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;//月份加一
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onConfirm(year, month, dayOfMonth);
                }
            }

        }, year, month - 1, day);//月份减一

        mDatePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onCancel();
                }
            }
        });

        if (!TextUtils.isEmpty(title)) {
            mDatePickerDialog.setTitle(title);
        }
        mDatePickerDialog.show();
    }

    /**
     * 日期选择器监听
     */
    public interface OnDatePickerListener {
        void onConfirm(int year, int month, int dayOfMonth);

        void onCancel();
    }

}

