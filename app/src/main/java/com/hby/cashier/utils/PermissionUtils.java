package com.hby.cashier.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hby.cashier.BuildConfig;
import com.hby.cashier.R;
import com.hby.cashier.weight.dialog.ConfirmDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/11/6 13:10
 * 最后编辑: 2020/11/6 - Hyy
 *
 * @author HouYinYu
 */
public class PermissionUtils {
    private Context context;

    public interface OnPermissionListener {
        public void cancel();

        public void agree();

        public void finish();
    }

    public PermissionUtils(Context context) {
        this.context = context;
    }

    public void showSinglePermission(String permissionsStr, String permissions, OnPermissionListener permissionListener) {
        if (ContextCompat.checkSelfPermission(context, permissions) == PackageManager.PERMISSION_DENIED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permissions)) {
                //权限不再询问
                new ConfirmDialog(context)
                        .isSingleBtn()
                        .setTitleText("您已设置 " + permissionsStr + " 权限不再询问，请到设置中开启")
                        .setDescribeText("设置-->应用管理-->权限管理")
                        .setConfirmText("确定")
                        .setDescribeTextColor(R.color.color_60)
                        .setTitleTextBold()
                        .setOnDialogClickListener(new ConfirmDialog.DialogClickListener() {
                            @Override
                            public void confirm() {
                                permissionListener.finish();
                                goPermissionManage();
                            }
                        }).setOnDialogCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        permissionListener.finish();
                    }
                }).show();
            } else {
                //权限没有授予
                AndPermission.with(context)
                        .runtime()
                        .permission(permissions)
                        .onGranted(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                permissionListener.agree();
                            }
                        })
                        .onDenied(new Action<List<String>>() {
                            @Override
                            public void onAction(List<String> data) {
                                permissionListener.cancel();
                            }
                        })
                        .start();
            }
        } else {
            //权限已经授予
            permissionListener.agree();
        }
    }


    public void showMultiplePermission(OnPermissionListener permissionListener, String... permissions) {
        showMultiplePermission("相关", permissionListener, permissions);
    }

    @SuppressLint("WrongConstant")
    public void showMultiplePermission(String permissionStr, OnPermissionListener permissionListener, String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                //权限没有申请
                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission)) {
                    //权限已不再询问
                    new ConfirmDialog(context)
                            .isSingleBtn()
                            .setTitleText("您已设置权限不再提示，请到设置中开启" + permissionStr + "权限")
                            .setDescribeText("设置-->应用管理-->权限管理")
                            .setConfirmText("确定")
                            .setDescribeTextColor(R.color.color_60)
                            .setTitleTextBold()
                            .setOnDialogClickListener(new ConfirmDialog.DialogClickListener() {
                                @Override
                                public void confirm() {
                                    permissionListener.finish();
                                    goPermissionManage();
                                }
                            }).setOnDialogCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            permissionListener.finish();
                        }
                    }).show();

                    return;
                }
            }
        }
        AndPermission.with(context)
                .runtime()
                .permission(permissions)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        permissionListener.agree();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        permissionListener.cancel();
                    }
                })
                .start();
    }

    public void showLocation(OnPermissionListener permissionListener) {
        //定位权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            AndPermission.with(context)
                    .runtime()
                    .permission(
                            Permission.ACCESS_COARSE_LOCATION,
                            Permission.ACCESS_FINE_LOCATION
                    )
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            permissionListener.agree();
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            permissionListener.cancel();
                        }
                    })
                    .start();
        }
    }

    /**
     * 录音
     *
     * @param permissionListener
     */
    public void showRecord(OnPermissionListener permissionListener) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AndPermission.with(context)
                    .runtime()
                    .permission(
                            Permission.WRITE_EXTERNAL_STORAGE,
                            Permission.RECORD_AUDIO
                    )
                    .onGranted(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            permissionListener.agree();
                        }
                    })
                    .onDenied(new Action<List<String>>() {
                        @Override
                        public void onAction(List<String> data) {
                            permissionListener.cancel();
                        }
                    })
                    .start();
        }
    }


    /**
     * 跳转到权限设置界面
     */
    public void goPermissionManage() {
        String manufacturer = Build.MANUFACTURER.toLowerCase();
        if (TextUtils.isEmpty(manufacturer)) {
            goAppDetailSettingIntent();
            return;
        }
        switch (manufacturer) {
            case PhoneUtils.PHONE_HUAWEI:
                gotoHuaweiPermission();
                break;
            case PhoneUtils.PHONE_XIAOMI:
                gotoMiuiPermission();
                break;
            case PhoneUtils.PHONE_OPPO:
                goOppoPermission();
                break;
            case PhoneUtils.PHONE_MEIZU:
                gotoMeizuPermission();
                break;
            default:
                goAppDetailSettingIntent();
                break;
        }
    }

    /**
     * 跳转到miui的权限管理页面
     */
    private void gotoMiuiPermission() {
        String rom = PhoneUtils.checkMIUI();
        try {
            Intent intent = new Intent();
            if (rom.equals("V5")) {
                Uri packageURI = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
            } else if (rom.equals("V6") || rom.equals("V7")) {
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
                intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID);
            } else if (rom.equals("V8") || rom.equals("V9")) {
                intent.setAction("miui.intent.action.APP_PERM_EDITOR");
                intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
                intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID);
            } else {
                goAppDetailSettingIntent();
            }
            context.startActivity(intent);
        } catch (Exception e) {
            goAppDetailSettingIntent();
        }
    }

    /**
     * 跳转到魅族的权限管理系统
     */
    private void gotoMeizuPermission() {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            goAppDetailSettingIntent();
        }
    }

    /**
     * 华为的权限管理页面
     */
    private void gotoHuaweiPermission() {
        try {
            Intent intent = new Intent(BuildConfig.APPLICATION_ID);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");//华为权限管理
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            goAppDetailSettingIntent();
        }

    }

    /**
     * OPPO权限管理
     */
    private void goOppoPermission() {
        try {
            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
//            val comp = ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.PermissionAppAllPermissionActivity")//R9SK 6.0.1  os-v3.0
            ComponentName comp = new ComponentName("com.coloros.securitypermission",
                    "com.coloros.securitypermission.permission.PermissionAppAllPermissionActivity");//R11t 7.1.1 os-v3.2
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            goAppDetailSettingIntent();
        }
    }

    /**
     * 获取应用详情页面intent（如果找不到要跳转的界面，也可以先把用户引导到系统设置页面）
     *
     * @return
     */
    private void goAppDetailSettingIntent() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        localIntent.setData(Uri.fromParts("package", BuildConfig.APPLICATION_ID, null));
        context.startActivity(localIntent);
    }


}
