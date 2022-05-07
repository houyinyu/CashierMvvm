package com.hby.cashier.views;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.hby.cashier.utils.LogUtils;

import retrofit2.http.PUT;

/**
 * 功能介绍 :扫描View
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/3/28
 */
public class ScanningView {
    private StringBuilder mResult;
    public OnScanValueListener mListener;

    public interface OnScanValueListener {
        void onScanValue(String value);
    }

    public ScanningView(OnScanValueListener listener) {
        this.mListener = listener;
        this.mResult = new StringBuilder();
    }

    /**
     * 扫码设备事件解析
     */
    public void analysisKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            int unicodeChar = event.getUnicodeChar();
            if (unicodeChar != 0) {
                mResult.append((char) unicodeChar);
            }
            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                if (mListener != null) {
                    mListener.onScanValue(mResult.toString());
                }
                mResult.delete(0, mResult.length());
            }
        }
    }

}
