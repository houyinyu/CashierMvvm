package com.hby.cashier.views;

import static android.content.Context.WIFI_SERVICE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.ImageButton;

import com.hby.cashier.R;
import com.hby.cashier.utils.LogUtils;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/28
 */
public class WifiSignalView {
    private Context context;
    private ImageButton wifiImage;
    private WifiManager wifi_service;
    private WifiStateBroadcastReceive mReceive;

    public WifiSignalView(Context context, ImageButton wifiImage) {
        this.context = context;
        this.wifiImage = wifiImage;
        wifi_service = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
    }

    public void registerWifiReceiver() {
        if (mReceive == null) {
            mReceive = new WifiStateBroadcastReceive();
        }
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        context.registerReceiver(mReceive, intentFilter);
    }


    public void unregisterWifiReceiver() {
        if (mReceive != null) {
            context.unregisterReceiver(mReceive);
            mReceive = null;
        }
    }

    public boolean isWifiConnect() {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        return mWifiInfo.isConnected();
    }


    private class WifiStateBroadcastReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int level = ((WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE))
                    .getConnectionInfo().getRssi();
            switch (intent.getAction()) {
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    //wifi开关变化
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE,
                            WifiManager.WIFI_STATE_DISABLED);
                    switch (wifiState) {
                        case WifiManager.WIFI_STATE_DISABLED:
                            LogUtils.i("**************************A:wifi关闭");
                            setWifiRSSI(level);
                            break;
                        case WifiManager.WIFI_STATE_ENABLED:
                            LogUtils.i("**************************A:wifi打开");
                            setWifiRSSI(level);
                            break;
                    }
                    break;
                case WifiManager.RSSI_CHANGED_ACTION:
                    //WIFI 热点信号强度发生变化
                    setWifiRSSI(level);
                    break;
                case WifiManager.SCAN_RESULTS_AVAILABLE_ACTION:
                    //wifi扫描结果
                    break;
                case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                    //wifi连接结果
                    break;
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    //网络状态变化
                    break;
            }

        }
    }


    private void setWifiRSSI(int level) {
        LogUtils.i("**************************设置WIFI强度:" + level);
        wifiImage.setImageResource(R.drawable.wifi_signal_image);
        if (level > -50 && level < 0) {
            //最强
            wifiImage.setImageLevel(35);
        } else if (level > -70 && level < -50) {
            //较强
            wifiImage.setImageLevel(25);
        } else if (level > -80 && level < -70) {
            //较弱
            wifiImage.setImageLevel(15);
        } else if (level > -100 && level < -80) {
            //微弱
            wifiImage.setImageLevel(9);
        }else {
            wifiImage.setImageLevel(4);
        }
    }


}
