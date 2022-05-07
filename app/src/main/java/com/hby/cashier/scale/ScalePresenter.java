package com.hby.cashier.scale;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.hby.cashier.R;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.utils.NumUtils;
import com.sunmi.electronicscaleservice.ScaleCallback;
import com.sunmi.scalelibrary.ScaleManager;
import com.sunmi.scalelibrary.ScaleResult;

import java.text.DecimalFormat;


/**
 * Copyright (C), 2018-2019,商米科技有限公司
 * FileName: ScalePresenter
 * Author: liuzhicheng
 * Date: 19-1-9 上午9:54
 * Description:
 * History:
 */

public class ScalePresenter {
    private Context context;
    private static final String TAG = "ScalePresenter";
    private ScaleManager mScaleManager;
    private boolean isScaleSuccess;

    public ScalePresenterCallback callback;

    private DecimalFormat decimalFormat = new DecimalFormat("0.000");
    private DecimalFormat meonyFormat = new DecimalFormat("0.00");
    private boolean mIsStable = false;

    private double price = 0;

//    private GvBeans gvBeans;
    public static int mNet;
    public int mTare;//皮重


    public interface ScalePresenterCallback {
        void getData(int net, int pnet, int statu);

        void isScaleCanUse(boolean isCan);

    }

    public ScalePresenter(Context context, ScalePresenterCallback callback) {
        this.context = context;
        this.callback = callback;
        connectScaleService();
    }

    //连接电子秤服务
    private void connectScaleService() {
        mScaleManager = ScaleManager.getInstance(context);
        mScaleManager.connectService(new ScaleManager.ScaleServiceConnection() {
            @Override
            public void onServiceConnected() {
                getScaleData();
            }

            @Override
            public void onServiceDisconnect() {
                isScaleSuccess = false;
                callback.isScaleCanUse(false);
            }
        });
    }

    private void getScaleData() {
        try {
            mScaleManager.getData(new ScaleResult() {
                @Override
                public void getResult(int net, int tare, boolean isStable) {
                    mNet=net;
                    mTare=tare;
                    mIsStable=isStable;
                }

                @Override
                public void getStatus(boolean isLightWeight, boolean overload, boolean clearZeroErr, boolean calibrationErr) {

                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清皮
     */
    public void clearTare() {
        if (mTare + mNet == 0 && isScaleSuccess) {
            tare();
        } else {
            Toast.makeText(context, R.string.scale_tips_clearfail, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 数字皮重
     *
     * @param numPnet
     */
    public void setNumPnet(int numPnet) {
        if (numPnet > 0 && numPnet <= 5998) {
            if (isScaleSuccess) {
                try {
                    mScaleManager.digitalTare(numPnet);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     * 去皮
     */
    public void tare() {
        try {
            if (isScaleSuccess) {
                mScaleManager.tare();
                Toast.makeText(context, getTare() == 0 ? R.string.more_peele : R.string.more_clear_peele, Toast.LENGTH_LONG).show();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清零
     */
    public void zero() {
        try {
            if (isScaleSuccess) {
                mScaleManager.zero();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public String formatTotalMoney() {
        return meonyFormat.format(mNet * price / 1000);
    }

    public String formatTotalMoney(int net) {
        return  NumUtils.getDoubleStr(net * price / 1000);
//        return meonyFormat.format(net * price / 1000);
    }

    public String formatQuality() {
        return decimalFormat.format(mNet * 1.0f / 1000);
    }

    public String formatQuality(int net) {
        return decimalFormat.format(net * 1.0f / 1000);
    }

    public boolean isScaleSuccess() {
        return isScaleSuccess;
    }

    public void setProductBean(LitePalProductBean productBean) {
//        this.productBean = productBean;
        price = NumUtils.getProductPrice(productBean);
    }

    public double getPrice() {
        return price;
    }

    public boolean isStable() {
        return mIsStable;
    }

//    public GvBeans getGvBeans() {
//        return gvBeans;
//    }


    public void onDestroy() {
        try {
            mScaleManager.cancelGetData();
            mScaleManager.onDestroy();
            mScaleManager = null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }


    public int getTare() {
        return mTare;
    }

    public void setTare(int mTare) {
        this.mTare = mTare;
    }
}
