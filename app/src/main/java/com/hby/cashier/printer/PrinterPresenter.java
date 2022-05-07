package com.hby.cashier.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.util.Log;

import com.hby.cashier.R;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.PrintMenuBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.utils.BitmapUtils;
import com.hby.cashier.utils.DeviceUtils;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.NumUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.sunmi.devicemanager.cons.PrtCts;
import com.sunmi.devicesdk.core.PrinterManager;
import com.sunmi.peripheral.printer.SunmiPrinterService;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by zhicheng.liu on 2018/4/4
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class PrinterPresenter {
    private Context context;
    private static final String TAG = "PrinterPresenter";
    public SunmiPrinterService printerService;
    private PrinterManager mManager;

    public PrinterPresenter(Context context, SunmiPrinterService printerService) {
        this.context = context;
        this.printerService = printerService;
        mManager = PrinterManager.getInstance();
    }

    public void print(final String json, final String payMode) {
        if (printerService == null) {
            return;
        }
        try {
            int ppp = printerService.getPrinterPaper();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintMenuBean printMenuBean = JsonUtils.parseJson(json, PrintMenuBean.class);
                int fontsizeTitle = 35;//标题字体
                int fontsizeContent = 25;//内容字体
//                String divideStar = "**************************************" + "\n";
//
//                String divideLine = "--------------------------------------" + "\n";
//
//                if (PrinterServiceView.isVertical) {
//                    divideStar = "************************" + "\n";
//
//                }
                String divideLine = "-----------------------------" + "\n";

                int width = divideLine.length();
                String productListTitle = formatProductTitle(width);
                String shiftChangeListTitle = formatShiftChangeTitle(width);
                try {
                    if (printerService.updatePrinterState() != 1) {
                        return;
                    }

                    if (payMode.equals(EnumUtils.PayMethod.PAY_CASH)) {
                        try {
                            //打开钱箱
                            printerService.openDrawer(null);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    //居中对齐
                    printerService.setAlignment(1, null);
                    //图片
                    Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.print_logo);
                    if (bitmap.getWidth() > 384) {
                        int newHeight = (int) (1.0 * bitmap.getHeight() * 384 / bitmap.getWidth());
                        bitmap = BitmapUtils.scale(bitmap, 384, newHeight);
                    }
                    printerService.printBitmap(bitmap, null);
                    printerService.printText("\n", null);
                    //店铺
                    printerService.printTextWithFont("\n" + printMenuBean.getShopName() + "\n\n", "", fontsizeTitle, null);
                    if (printMenuBean.getPrintType() == 1) {
                        //交接班单据
                        printerService.sendRAWData(boldOn(), null);
                        printerService.printTextWithFont("交接班单据" + "\n", "", 30, null);
                        printerService.sendRAWData(boldOff(), null);
                        printerService.printText("\n", null);
                    }
                    printerService.setAlignment(0, null);//距左对齐
                    //机号
                    printerService.printTextWithFont("机号：" + SPUtils.getInstance().getString(DataKey.KEY_DEVICE_ID) + "\n", "", fontsizeContent, null);
                    //收银员
                    printerService.printTextWithFont("收银员：" + printMenuBean.getCashierUser() + "\n", "", fontsizeContent, null);
                    if (printMenuBean.getPrintType() == 0) {
                        //商品打印
                        printerService.printTextWithFont("交易时间：" + printMenuBean.getPrintTime() + "\n", "", fontsizeContent, null);
                    } else {
                        //交接班打印
                        printerService.printTextWithFont("打印时间：" + printMenuBean.getPrintTime() + "\n", "", fontsizeContent, null);
                        printerService.printText("\n", null);
                        printerService.sendRAWData(boldOn(), null);
                        printerService.printTextWithFont("收银时间：" + "\n", "", fontsizeContent, null);
                        printerService.sendRAWData(boldOff(), null);
                        printerService.printText("\n", null);
                        printerService.printTextWithFont("开始时间：" + printMenuBean.getStartTime() + "\n", "", fontsizeContent, null);
                        printerService.printTextWithFont("结束时间：" + printMenuBean.getEndTime() + "\n", "", fontsizeContent, null);
                    }

                    //线
                    printerService.printTextWithFont(divideLine, "", fontsizeContent, null);
                    if (printMenuBean.getPrintType() == 0) {
                        //商品列表数据
                        printerService.printTextWithFont(productListTitle + "\n", "", fontsizeContent, null);
                        printProductList(printMenuBean, fontsizeContent, divideLine, width);
                        printerService.printText("\n", null);
                    } else {
                        //交接班列表数据
                        printerService.printTextWithFont(shiftChangeListTitle + "\n", "", fontsizeContent, null);
                        printChangeShiftList(printMenuBean, fontsizeContent, divideLine, width);
                    }
                    //商品打印添加
                    if (printMenuBean.getPrintType() == 0) {
                        printerService.printTextWithFont("订单号：" + printMenuBean.getOrderNo() + "\n", "", fontsizeContent, null);
//                        printerService.printTextWithFont("客服电话：" + printMenuBean.getContactPhone() + "\n", "", fontsizeContent, null);
                        printerService.printText("\n", null);
                        printerService.printTextWithFont(ResourcesUtils.getString(context, R.string.print_remark) + "\n", "", fontsizeContent, null);
                    }
                    printerService.printText("\n\n", null);
                    printerService.lineWrap(4, null);
                    printerService.cutPaper(null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

//    public void printByDeviceManager(String json, String payMode, Device device) {
//        if (mManager == null) return;
//        mManager.setDefaultDevice(device);
//        mManager.enter(true);
//        mManager.initPrinter();
//        PrintMenuBean printMenuBean = JsonUtils.parseJson(json, PrintMenuBean.class);
//        try {
//            mManager.addBold(true);
//            mManager.addTextSizeDouble();
//            //标题
//            mManager.addTextAtCenter(printMenuBean.getTitleName());
////            mManager.addTextAtCenter(ResourcesUtils.getString(context, R.string.print_proofs));
//            mManager.addTextSizeNormal();
//            mManager.addBold(false);
//            mManager.addFeedDots(10);
//            mManager.addHorizontalCharLine('*');
//
//            //图标
//            mManager.addFeedDots(10);
//            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.print_logo);
//            if (bitmap.getWidth() > 384) {
//                int newHeight = (int) (1.0 * bitmap.getHeight() * 384 / bitmap.getWidth());
//                bitmap = BitmapUtils.scale(bitmap, 384, newHeight);
//            }
//            mManager.addImage(bitmap);
//            mManager.addFeedDots(10);
//            //店铺
//            mManager.addText(printMenuBean.getShopName());
//            mManager.addFeedDots(10);
//
//            if (printMenuBean.getPrintType() == 0) {
//                //商品单据
//                mManager.addText("单号：" + printMenuBean.getOrderCode());
//            } else {
//                //交接班单据
//                mManager.addText("收银时间：");
//                mManager.addFeedDots(10);
//                mManager.addText(printMenuBean.getStartTime());
//                mManager.addText("至");
//                mManager.addText(printMenuBean.getEndTime());
//                mManager.addFeedDots(10);
//                mManager.addText("收银员：" + printMenuBean.getCashierUser());
//            }
//            //打印时间
//            mManager.addFeedDots(10);
//            mManager.addText("打印时间：" + printMenuBean.getPrintTime());
//
//            mManager.addHorizontalCharLine('*');
//            mManager.addFeedDots(10);
//
//            if (printMenuBean.getPrintType() == 0) {
//                //商品列表数据
//                String[] title = {
//                        ResourcesUtils.getString(context, R.string.menus_product_name),
//                        ResourcesUtils.getString(context, R.string.menus_unit_num),
//                        ResourcesUtils.getString(context, R.string.menus_unit_price),
//                        ResourcesUtils.getString(context, R.string.menus_total_price),
//                };
//                mManager.addTextsAutoWrap(new float[]{3, 1, 2, 2},
//                        new int[]{PrtCts.ALIGN_LEFT, PrtCts.ALIGN_CENTER, PrtCts.ALIGN_CENTER, PrtCts.ALIGN_RIGHT}
//                        , title);
//                mManager.addFeedDots(10);
//                mManager.addHorizontalLine(0);
//                mManager.addFeedDots(10);
//                printProductListByDeviceManager(printMenuBean);
//
//            } else {
//                //交接班列表数据
//                mManager.addHorizontalLine(0);
//                mManager.addFeedDots(10);
//                printChangeShiftListByDeviceManager(printMenuBean);
//            }
//
//            mManager.addFeedDots(10);
//            mManager.addHorizontalCharLine('*');
//            if (printMenuBean.getPrintType() == 0) {
//                mManager.addFeedDots(10);
//                mManager.addText("店铺地址：" + printMenuBean.getShopAddress());
//                mManager.addFeedDots(10);
//                mManager.addText("收银员：" + printMenuBean.getCashierUser());
//                mManager.addFeedDots(10);
//                mManager.addText("联系电话：" + printMenuBean.getContactPhone());
//                mManager.addFeedDots(10);
//                mManager.addText("客户需知：" + printMenuBean.getRemark());
//            }
//            mManager.addFeedLine(6);
//            mManager.addCutter();
//            mManager.commit(null);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void printProductListByDeviceManager(PrintMenuBean menuBean) {
//        for (PrintMenuBean.ProductListBean listBean : menuBean.getProductList()) {
//            mManager.addTextsAutoWrap(new float[]{3, 1, 2, 2},
//                    new int[]{PrtCts.ALIGN_LEFT, PrtCts.ALIGN_CENTER, PrtCts.ALIGN_CENTER, PrtCts.ALIGN_RIGHT},
//                    new String[]{listBean.getProductName(),
//                            NumUtils.getDoubleStr(listBean.getProductNum()),
//                            NumUtils.getDoubleStr(listBean.getUnitPrice()),
//                            NumUtils.getDoubleStr(listBean.getTotalPrice())});
//            mManager.addFeedDots(10);
//        }
//        mManager.addHorizontalLine(0);
//        mManager.addFeedDots(10);
//
//        mManager.addTextLeftRight("总数：", String.valueOf(menuBean.getTotalNum()));
//        mManager.addFeedDots(10);
//        mManager.addTextLeftRight("总金额：", NumUtils.getDoubleStr(menuBean.getTotalPrice()));
//        mManager.addFeedDots(10);
//        mManager.addTextLeftRight("促销：", NumUtils.getDoubleStr(menuBean.getPromotionPrice()));
//        mManager.addFeedDots(10);
//        mManager.addTextLeftRight("返利：", NumUtils.getDoubleStr(menuBean.getRebatePrice()));
//        mManager.addFeedDots(10);
//
//        mManager.addBold(true);
//        mManager.addTextSizeDouble();
//        mManager.addTextLeftRight("实际付款：", NumUtils.getDoubleStr(menuBean.getActualPrice()));
//        mManager.addTextSizeNormal();
//        mManager.addBold(false);
    }

    private void printChangeShiftListByDeviceManager(PrintMenuBean menuBean) {
        for (PrintMenuBean.ChangeShiftListBean listBean : menuBean.getChangeShiftList()) {
            mManager.addTextsAutoWrap(new float[]{4, 1, 3},
                    new int[]{PrtCts.ALIGN_LEFT, PrtCts.ALIGN_CENTER, PrtCts.ALIGN_CENTER, PrtCts.ALIGN_RIGHT},
                    new String[]{listBean.getCollectionType(),
                            String.valueOf(listBean.getCollectionNum()),
                            NumUtils.getDoubleStr(listBean.getCollectionPrice())});
            mManager.addFeedDots(10);
        }
        mManager.addHorizontalLine(0);
        mManager.addFeedDots(10);
    }

    private String formatProductTitle(int width) {
        Log.e("@@@@@", width + "=======");

        //商品列表打印的title
        String[] title = {
                ResourcesUtils.getString(context, R.string.menus_product_name),
                ResourcesUtils.getString(context, R.string.menus_unit_num),
                ResourcesUtils.getString(context, R.string.menus_unit_price),
        };
        StringBuffer sb = new StringBuffer();
        int blank1 = width * 1 / 2 - String_length(title[0]);
        int blank2 = width * 1 / 4 - String_length(title[1]);

        sb.append(title[0]);
        sb.append(addblank(blank1));

        sb.append(title[1]);
        sb.append(addblank(blank2 * 2));

        sb.append(title[2]);

        return sb.toString();
    }

    private String formatShiftChangeTitle(int width) {
        Log.e("@@@@@", width + "=======");

        //交接班列表打印的title
        String[] title = {
                ResourcesUtils.getString(context, R.string.menus_pay_type),
                ResourcesUtils.getString(context, R.string.menus_pay_num),
                ResourcesUtils.getString(context, R.string.menus_pay_price),
        };
        StringBuffer sb = new StringBuffer();
        int blank1 = width / 2 - String_length(title[0]);
        int blank2 = (width / 4 - String_length(title[1]));

        sb.append(title[0]);
        sb.append(addblank(blank1));

        sb.append(title[1]);
        sb.append(addblank(blank2 * 2));

        sb.append(title[2]);

        return sb.toString();
    }

    private void printNewline(String str, int width, int fontsizeContent) throws RemoteException {
        List<String> strings = PrintUtils.getStrList(str, width);
        for (String string : strings) {
            printerService.printTextWithFont(string + "\n", "", fontsizeContent, null);
        }
    }

    private void printProductList(PrintMenuBean printMenuBean, int fontsizeContent, String divide2, int width) throws RemoteException {
        int blank2;
        int blank3;
        int maxNameWidth = width;

        StringBuffer sb = new StringBuffer();
        for (ShopCartBean.ProductListBean productListBean : printMenuBean.getProductList()) {
            sb.setLength(0);
            //商品名称
            String name = productListBean.getProductName();
            String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";

            sb.append(name.length() > maxNameWidth ? name1 : name);
            if (name.length() > maxNameWidth) {
                printNewline(name.substring(maxNameWidth), maxNameWidth, fontsizeContent);
            }
            printerService.printTextWithFont(sb.toString() + "\n", "", fontsizeContent, null);
            sb.setLength(0);

            sb.append(addblank(width * 1 / 2));//数量前面的空格
            //数量
            int blankNum = 0;
            if (productListBean.getIsInBulk() > 0 || !productListBean.getIsStandard().equals("Y")) {
                sb.append(productListBean.getProductNum());
                blankNum = width * 1 / 4 - String.valueOf(productListBean.getProductNum()).length();
            } else {
                sb.append("*").append(productListBean.getProductNum());
                blankNum = width * 1 / 4 - ("*" + productListBean.getProductNum()).length();
            }
            sb.append(addblank(blankNum));//数量的空格
            //小计
            double totalPrice = productListBean.getProductPrice() * productListBean.getProductNum();
            int blankPrice = width * 1 / 4 - NumUtils.getDoubleStr(totalPrice).length();
            sb.append(addblank(blankPrice));//小计的空格
            sb.append(NumUtils.getDoubleStr(totalPrice));
            printerService.printTextWithFont(sb.toString(), "", fontsizeContent, null);
        }
        printerService.printTextWithFont("\n" + divide2, "", fontsizeContent, null);

        //底部
        sb.setLength(0);
        int blankAllPrice = 1;
        if ((String_length("商品金额：¥" + NumUtils.getDoubleStr(printMenuBean.getTotalPrice())) +
                String_length("件数：" + printMenuBean.getTotalNum())) < width) {
            blankAllPrice = width
                    - String_length("商品金额：¥" + NumUtils.getDoubleStr(printMenuBean.getTotalPrice()))
                    - String_length("件数：" + printMenuBean.getTotalNum());
        }
        sb.append("商品金额：¥").append(NumUtils.getDoubleStr(printMenuBean.getTotalPrice()));
        sb.append(addblank(4));
        sb.append("件数：").append(printMenuBean.getTotalNum()).append("\n");

        sb.append("促销立减：").append("-¥").append(NumUtils.getDoubleStr(printMenuBean.getPromotionPrice())).append("\n");
        sb.append("商家返利：").append("-¥").append(NumUtils.getDoubleStr(printMenuBean.getRebatePrice())).append("\n");
        sb.append("实收金额：").append("¥").append(NumUtils.getDoubleStr(printMenuBean.getActualPrice())).append("\n");
        sb.append("支付方式：").append(printMenuBean.getPayType()).append("\n");
        sb.append("找零：").append("¥").append(NumUtils.getDoubleStr(printMenuBean.getGiveChange())).append("\n");

        printerService.printTextWithFont(sb.toString() + "\n", "", fontsizeContent, null);
        sb.setLength(0);
    }

    private void printChangeShiftList(PrintMenuBean printMenuBean, int fontsizeContent, String divide2, int width) throws RemoteException {

        StringBuffer sb = new StringBuffer();
        for (PrintMenuBean.ChangeShiftListBean changeShiftListBean : printMenuBean.getChangeShiftList()) {
            sb.setLength(0);

            String name = changeShiftListBean.getCollectionType();

            int blankName = width / 2 - String_length(name);

            sb.append(name);
            sb.append(addblank(blankName));

            int blankNum = width * 1 / 4 - ("" + changeShiftListBean.getCollectionNum()).length();
            sb.append(changeShiftListBean.getCollectionNum());
            sb.append(addblank(blankNum));

            int blankPrice = width * 1 / 4 - NumUtils.getDoubleStr(changeShiftListBean.getCollectionPrice()).length();
            sb.append(addblank(blankPrice));//小计的空格
            sb.append(NumUtils.getDoubleStr(changeShiftListBean.getCollectionPrice()));

            printerService.printTextWithFont(sb.toString() + "\n", "", fontsizeContent, null);
        }
        printerService.printTextWithFont(divide2, "", fontsizeContent, null);
        sb.setLength(0);
    }

    private String formatData(Date nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return time.format(nowTime);
    }

    /**
     * 添加空格
     *
     * @param count
     * @return
     */
    private String addblank(int count) {
        String st = "";
        if (count < 0) {
            count = 0;
        }
        for (int i = 0; i < count; i++) {
            st = st + " ";
        }
        return st;
    }

    private static final byte ESC = 0x1B;// Escape

    /**
     * 字体加粗
     */
    private byte[] boldOn() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        result[2] = 0xF;
        return result;
    }

    /**
     * 取消字体加粗
     */
    private byte[] boldOff() {
        byte[] result = new byte[3];
        result[0] = ESC;
        result[1] = 69;
        return result;
    }

    private boolean isZh() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language.endsWith("zh");
    }

    private int String_length(String rawString) {
        return rawString.replaceAll("[\\u4e00-\\u9fa5]", "SH").length();
    }


}
