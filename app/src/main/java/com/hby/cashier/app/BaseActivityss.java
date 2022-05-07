package com.hby.cashier.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;


import com.hby.cashier.R;
import com.hby.cashier.utils.DeviceUtils;
import com.hby.cashier.utils.StatusBarColor;
import com.hyy.mvvm.loading.LoadingDialog;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BaseActivityss extends AppCompatActivity {
    public Context context;
    public LoadingDialog loadingDialog;
    public AppApplication app;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
        context = null;
        ActivityCollector.removeActivity(this);
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        DeviceUtils.setStickFullScreen(getWindow().getDecorView());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        ActivityCollector.addActivity(this);
        if (app == null) app = AppApplication.getInstance();
        if (loadingDialog == null) loadingDialog = new LoadingDialog(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setStatusBarColor(Activity activity) {
        setStatusBarColor(activity, StatusBarColor.COLOR_WHITE);
    }

    public void setStatusBarColor(Activity activity, StatusBarColor barColor) {
        //设置状态栏的颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {//6.0版本大于修改状态栏图标深色
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明的状态栏
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR-状态栏标记为浅色，然后状态栏的字体颜色自动转换为深色
            //SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN-全屏的意思，也就是会将状态栏隐藏
            switch (barColor) {
                case COLOR_THEME_THEME:
                    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.themeColor));
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    break;
                case COLOR_WHITE:
                    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.color_white));
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    break;
                case COLOR_TRANSPARENT:
                    activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
                    activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    break;
            }
        }
    }

    public boolean notEmpty(Object val) {
        return !isEmpty(val);
    }

    public boolean isEmpty(Object obj) {
        if (obj == null) return true;
        if (obj instanceof String && obj.toString().length() <= 0) return true;
        if (obj.getClass().isArray() && Array.getLength(obj) <= 0) return true;
        if (obj instanceof Collection && ((Collection) obj).isEmpty()) return true;
        if (obj instanceof Map && ((Map) obj).isEmpty()) return true;
        if (obj instanceof SparseArray && ((SparseArray) obj).size() <= 0) return true;
        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() <= 0)
            return true;
        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() <= 0) return true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() <= 0) return true;
        }
        return false;
    }

    public void toast(String msg) {
        if (context == null) return;
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 启动其他Activity
     *
     * @param cls 需要启动的那个Activity
     */
    public void goIntent(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * 启动其他Activity
     *
     * @param cls  需要启动的那个Activity
     * @param flag 启动模式
     */
    public void goIntent(Class<?> cls, int flag) {
        goIntent(cls, flag, null);
    }

    /**
     * 启动其他Activity
     *
     * @param cls    需要启动的那个Activity
     * @param flag   启动模式
     * @param bundle 数据
     */
    public void goIntent(Class<?> cls, int flag, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.addFlags(flag);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * 启动其他Activity
     *
     * @param cls    需要启动的那个Activity
     * @param bundle 数据
     */
    public void goIntent(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    /**
     * 启动其他Activity
     *
     * @param cls       需要启动的那个Activity
     * @param ext_key   数据的key
     * @param ext_value 数据
     */
    public void goIntent(Class<?> cls, String ext_key, String ext_value) {
        Intent intent = new Intent(this, cls);
        if (ext_value != null && ext_key != null) {
            intent.putExtra(ext_key, ext_value);
        }
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }

    public void goIntent(Class<?> cls, String ext_key, int ext_value) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(ext_key, ext_value);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
        }
    }

    public void goIntentResult(Class<?> cls, int requestCode) {
        Intent intent = new Intent(this, cls);
        startActivityForResult(intent, requestCode);
    }

    public void goIntentResult(Class<?> cls, int requestCode, String key, String data) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(key, data);
        startActivityForResult(intent, requestCode);
    }


    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.tran_in_2_left, R.anim.tran_in_2_right);
    }

    public void startActivityMgHome(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.tran_out_2_left, R.anim.tran_out_2_right);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.tran_out_2_left, R.anim.tran_out_2_right);
        hideKeyBoard();
    }


    /**
     * 返回桌面
     */
    private long mCurrentTime = 0; // 当前时间毫秒值(退出的时候使用)

    public void onKeyBack() {
        if ((System.currentTimeMillis() - mCurrentTime > 1500)) {
            toast("再按一次返回桌面");
            mCurrentTime = System.currentTimeMillis();
        } else {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
    }


    public void loginOut() {
//        app.clearData();
//        String loginAccount = SharedUtils.readStringMethod(GlobalDataKey.KEY_ACCOUNT, "");
//        String defaultShopID = SharedUtils.readStringMethod(GlobalDataKey.KEY_ORDER_DEFAULT_SHOPID, "");
//        String defaultWarehouseID = SharedUtils.readStringMethod(GlobalDataKey.KEY_ORDER_DEFAULT_WAREHOUSEID, "");
//        jpushOut();
//
//        //将保存在sp中的数据删除
//        SharedUtils.clearSharedMethod();
//        //销毁所有的Activity
//        ActivityCollector.finishAll();
//        //清除聊天数据库
//        LitePal.deleteAll(LitePalMessageBean.class);
//        //清除Glide缓存
//        GlideCacheUtil.getInstance().clearImageAllCache(context);
//
//        SharedUtils.writeBooleanMethod(GlobalDataKey.KEY_IS_FIRST, false);
//        SharedUtils.writeStringMethod(GlobalDataKey.KEY_ORDER_DEFAULT_SHOPID, defaultShopID);
//        SharedUtils.writeStringMethod(GlobalDataKey.KEY_ORDER_DEFAULT_WAREHOUSEID, defaultWarehouseID);
//
//        //重新进入登录页面
//        Intent intent = new Intent(context, LoginActivity.class);
//        intent.putExtra("account", loginAccount);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
    }

    /**
     * 发送消息（类似Handler）
     *
     * @param what
     * @param delaySeconds
     */
    public void sendMsg(int what, int delaySeconds) {
        Observable.timer(delaySeconds, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> msgManagement(what))
                .subscribe();
    }

    public void sendMsg(int what) {
        Observable.empty().observeOn(AndroidSchedulers.mainThread())
                .doOnComplete(() -> msgManagement(what)
                ).subscribe();
    }

    protected void msgManagement(int what) {
    }

    /**
     * 打开软键盘
     *
     * @param view
     */
    public void showKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            //使editText获取焦点
            view.requestFocus();
            imm.showSoftInput(view, InputMethodManager.SHOW_FORCED); // 强制打开软键盘
        }
    }

    /**
     * 关闭软键盘
     */
    public void hideKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

}
