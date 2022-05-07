package com.hby.cashier.printer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hby.cashier.R;
import com.hby.cashier.app.AppApplication;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.PrintMenuBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.ui.tools.PriceCalculationView;
import com.hby.cashier.utils.DensityUtils;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.utils.TimeUtils;
import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.sunmi.extprinterservice.ExtPrinterService;
import com.sunmi.peripheral.printer.InnerPrinterCallback;
import com.sunmi.peripheral.printer.InnerPrinterException;
import com.sunmi.peripheral.printer.InnerPrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能介绍 :打印自定义
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/2
 */
public class PrinterServiceView {
    private static BaseActivity mContext;
    public static boolean isK1 = false;
    public static boolean isVertical = false;//是横屏还是竖屏

    private SunmiPrinterService woyouService = null;//商米标准打印 打印服务
    private ExtPrinterService extPrinterService = null;//k1 打印服务

    public static PrinterPresenter printerPresenter;
    public static KPrinterPresenter kPrinterPresenter;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    private static PrinterServiceView singleton = null;

    public static PrinterServiceView getInstance() {
        if (singleton == null) {
            synchronized (PrinterServiceView.class) {
                if (singleton == null) {
                    singleton = new PrinterServiceView();
                }
            }
        }
        return singleton;
    }

    public PrinterServiceView() {
    }

    public static void init(BaseActivity context) {
        mContext = context;
    }

    public void initPrintService() {
        isVertical = DensityUtils.getScreenH(mContext) > DensityUtils.getScreenW(mContext);
        isK1 = AppApplication.getInstance().isHaveCamera() && isVertical;

        if (isK1) {
            connectKPrintService();
        } else {
            connectPrintService();
        }
    }

    /**
     * 开始打印
     *
     * @param product_print_data
     * @param payMode
     */
    public void paySuccessToPrinter(String product_print_data, String payMode) {
        if (isK1) {
            if (kPrinterPresenter != null) {
                kPrinterPresenter.print(product_print_data, payMode);
            }
        } else {
            if (printerPresenter == null) {
                printerPresenter = new PrinterPresenter(mContext, woyouService);
            }
            printerPresenter.print(product_print_data, payMode);

            //第三方打印
//            ThreadPoolManager.getsInstance().execute(() -> {
//                List<Device> deviceList = PrinterManager.getInstance().getPrinterDevice();
//                if (deviceList == null || deviceList.isEmpty()) return;
//                for (Device device : deviceList) {
//                    if (device.type == Cons.Type.PRINT && device.connectType == Cons.ConT.INNER) {
//                        continue;
//                    }
//                    printerPresenter.printByDeviceManager(product_print_data, payMode, device);
//                }
//            });

        }
    }


    /**
     * 整合商品打印数据
     */
    private String orderNo = "";
    private double orderPrice = 0;
    private double promotionPrice = 0;
    private double rebatePrice = 0;
    private double actualPrice = 0;
    private double changePrice = 0;

    public void setOrderNo(String orderNo) {
        //设置订单金额
        this.orderNo = orderNo;
    }

    public void setOrderPrice(double orderPrice) {
        //设置订单金额
        this.orderPrice = orderPrice;
    }

    public void setActualPrice(double actualPrice) {
        //设置实收金额
        this.actualPrice = actualPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        //设置活动金额
        this.promotionPrice = promotionPrice;
    }

    public void setRebatePrice(double rebatePrice) {
        //设置返利金额
        this.rebatePrice = rebatePrice;
    }

    public void setChangePrice(double changePrice) {
        //设置找零金额
        this.changePrice = changePrice;
    }

    public String buildProductData(List<ShopCartBean> shopCartList, String payType) {
        PrintMenuBean printMenuBean = new PrintMenuBean();
        printMenuBean.setPrintType(0);
        printMenuBean.setLogoID(R.drawable.print_logo);
        printMenuBean.setShopName(SPUtils.getInstance().getString(DataKey.KEY_SHOP_NAME));
        printMenuBean.setCashierUser(SPUtils.getInstance().getString(DataKey.KEY_USER_NAME));
        printMenuBean.setPrintTime(TimeUtils.getTime(System.currentTimeMillis()));
        List<ShopCartBean.ProductListBean> printProductList = new ArrayList<>();

        for (int i = 0; i < shopCartList.size(); i++) {
            printProductList.addAll(shopCartList.get(i).getProductList());
        }

        printMenuBean.setProductList(printProductList);
        printMenuBean.setOrderNo(orderNo);
        printMenuBean.setTotalPrice(orderPrice);
        printMenuBean.setTotalNum(PriceCalculationView.getProductNum(shopCartList));
        printMenuBean.setPromotionPrice(promotionPrice);
        printMenuBean.setRebatePrice(rebatePrice);
        printMenuBean.setActualPrice(actualPrice);
        printMenuBean.setPayType(payType);
        printMenuBean.setGiveChange(changePrice);


        printMenuBean.setContactPhone("17777777777");

        return JsonUtils.Object2Json(printMenuBean);
    }

    /**
     * 整合交接班打印数据
     */
    public String buildChangeShiftData(long loginTimeStamp, long nowTimeStamp,
                                       List<PrintMenuBean.ChangeShiftListBean> changeShiftList) {
        PrintMenuBean printMenuBean = new PrintMenuBean();
        printMenuBean.setPrintType(1);
        printMenuBean.setLogoID(R.drawable.print_logo);
        printMenuBean.setShopName(SPUtils.getInstance().getString(DataKey.KEY_SHOP_NAME));
        printMenuBean.setStartTime(TimeUtils.getTime(loginTimeStamp));
        printMenuBean.setEndTime(TimeUtils.getTime(nowTimeStamp));
        printMenuBean.setCashierUser(SPUtils.getInstance().getString(DataKey.KEY_USER_NAME));
        printMenuBean.setPrintTime(TimeUtils.getTime(System.currentTimeMillis()));
        List<PrintMenuBean.ChangeShiftListBean> printChangeShiftList = new ArrayList<>();
        for (int i = 0; i < changeShiftList.size(); i++) {
            PrintMenuBean.ChangeShiftListBean changeShiftListBean = new PrintMenuBean.ChangeShiftListBean();
            changeShiftListBean.setCollectionType(changeShiftList.get(i).getCollectionType());
            changeShiftListBean.setCollectionNum(changeShiftList.get(i).getCollectionNum());
            changeShiftListBean.setCollectionPrice(changeShiftList.get(i).getCollectionPrice());
            printChangeShiftList.add(changeShiftListBean);
        }
        printMenuBean.setChangeShiftList(printChangeShiftList);
        return JsonUtils.Object2Json(printMenuBean);
    }

    //连接打印服务
    private void connectPrintService() {
        try {
            InnerPrinterManager.getInstance().bindService(mContext,
                    innerPrinterCallback);
        } catch (InnerPrinterException e) {
            e.printStackTrace();
        }
    }

    private InnerPrinterCallback innerPrinterCallback = new InnerPrinterCallback() {
        @Override
        protected void onConnected(SunmiPrinterService service) {
            woyouService = service;
            printerPresenter = new PrinterPresenter(mContext, woyouService);
        }

        @Override
        protected void onDisconnected() {
            woyouService = null;

        }
    };

    //连接K1打印服务
    private void connectKPrintService() {
        Intent intent = new Intent();
        intent.setPackage("com.sunmi.extprinterservice");
        intent.setAction("com.sunmi.extprinterservice.PrinterService");
        mContext.bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            extPrinterService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            extPrinterService = ExtPrinterService.Stub.asInterface(service);
            kPrinterPresenter = new KPrinterPresenter(mContext, extPrinterService);
        }
    };

    public void unBindPrintService() {
        if (woyouService != null) {
            try {
                InnerPrinterManager.getInstance().unBindService(mContext,
                        innerPrinterCallback);
            } catch (InnerPrinterException e) {
                e.printStackTrace();
            }
        }
    }
}
