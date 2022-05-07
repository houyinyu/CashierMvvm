package com.hby.cashier.printer;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import com.hby.cashier.app.AppApplication;


/**
 * Created by zhicheng.liu on 2018/3/26
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class ResourcesUtils {
    @NonNull
    public static String getString(Context context,@StringRes int id){
        if(context==null){
            return AppApplication.getInstance().getResources().getString(id);
        }
        return context.getResources().getString(id);
    }
    @NonNull
    public static String getString(@StringRes int id){
        return getString(null,id);
    }
}
