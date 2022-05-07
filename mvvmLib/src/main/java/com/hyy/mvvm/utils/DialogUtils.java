package com.hyy.mvvm.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;

import androidx.appcompat.app.AlertDialog;


/**
 * 请创建对象在使用!!本次更新减少了静态方法和静态常量 <br>
 *
 * @author masai 2014-2-14
 */
public class DialogUtils {
    private static AlertDialog.Builder builder;

    static AlertDialog dl;

    private Activity context;

    /**
     * 单一的dialog!!会回调一个right的方法
     */
    public static AlertDialog singleDialog(Activity activity, String title, String msg, String right, final ButtomCallBack callBack) {
        return ThreeDialog(activity, title, msg, null, null, right, callBack);
    }

    /**
     * 2个按钮的dialog！！会回调 left,right的方法
     */
    public static AlertDialog twoDialog(Activity activity, String title, String msg, String left, String right, final ButtomCallBack
            callBack) {
        return ThreeDialog(activity, title, msg, left, null, right, callBack);
    }

    /**
     * 3个按钮的dialog
     */
    public static AlertDialog ThreeDialog(Activity activity, String title, String msg, String left, String min, String right, final
    ButtomCallBack callBack) {
        builder = new AlertDialog.Builder(activity);
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        if (!TextUtils.isEmpty(msg)) builder.setMessage(msg);

        if (!TextUtils.isEmpty(left)) {
            builder.setNegativeButton(left, new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (callBack != null) {
                        callBack.left();
                    }
                }
            });
        }

        if (!TextUtils.isEmpty(min)) {
            builder.setNeutralButton(min, new OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    if (callBack != null) callBack.min();
                }
            });
        }
        if (TextUtils.isEmpty(right)) {
            right = "确定";
        }
        dl = builder.setPositiveButton(right, new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (callBack != null) callBack.right();
            }
        }).create();

        if (!activity.isFinishing() && !dl.isShowing()) {
            dl.show();
        }
        return dl;
    }


    /**
     * 单选列表（无取消和确认。）
     *
     * @param title
     * @param str      列表数组
     * @param callBack
     */
    public static void SingleSelectDialog(Activity activity, String title, final String[] str, final SingleDialogCallBack callBack) {
        builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setItems(str, new OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                callBack.selectCallBack(which);
            }
        });
        builder.create().show();
    }


    /**
     * 多选择的dialog!为了省事直接使用了 progressCallBack类的回调
     *
     * @param title 多选标题
     * @param items 多选条目
     */
    public void moreSelectDialog(String title, final String[] items, final boolean[] checks, final MoreSelectCallBack callback) {
        if (!TextUtils.isEmpty(title)) builder.setTitle(title);
        builder.setMultiChoiceItems(items, checks, new DialogInterface.OnMultiChoiceClickListener() {
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                checks[which] = isChecked;
            }
        });
        builder.setPositiveButton("确认", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.right(checks);
            }
        });
        builder.setNegativeButton("取消", new OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                callback.left();
            }
        });
        builder.create().show();
    }

    public void loadIcon(AlertDialog.Builder builder) {
        builder.setIcon(context.getApplicationInfo().icon);
    }

    /**
     * 取消进度框
     *
     * @param mDialog
     */
    public void dismissDialog(Dialog mDialog) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void dismissDialog() {
        dl.dismiss();
    }

    /**
     * 1，2，3个按钮请callBack回调类，必须要覆写right。 <br>
     * 其他的请根据自己的需求覆写。
     *
     * @author masai
     */
    public interface ButtomCallBack {
        public void left();

        public void right();

        public void min();
    }

    /**
     * 简单的dialog单选<br>
     * position 选中的item的角标
     *
     * @author masai
     */
    public interface SingleDialogCallBack {
        public void selectCallBack(int position);
    }

    public interface ProgressCallBack {
        public void left();

        public void right();
    }

    public interface MoreSelectCallBack {
        /**
         * 取消
         */
        public void left();

        public void right(boolean[] checks);
    }
}
