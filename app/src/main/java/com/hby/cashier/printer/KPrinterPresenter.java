package com.hby.cashier.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSON;
import com.hby.cashier.R;
import com.hby.cashier.bean.PrintMenuBean;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.printer.bean.SunmiLink;
import com.hby.cashier.utils.BitmapUtils;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.NumUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.sunmi.extprinterservice.ExtPrinterService;
import com.sunmi.sunmiopenservice.SunmiOpenServiceWrapper;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Created by zhicheng.liu on 2018/4/4
 * address :liuzhicheng@sunmi.com
 * description :
 */

public class KPrinterPresenter {
    private Context context;
    private static final String TAG = "KPrinterPresenter";
    private ExtPrinterService mPrinter;
    String unic = "GBK";

    public KPrinterPresenter(Context context, ExtPrinterService printerService) {
        this.context = context;
        this.mPrinter = printerService;
    }

    public void print(String json, String payMode) {
        PrintMenuBean printMenuBean = JsonUtils.parseJson(json, PrintMenuBean.class);
        int fontsizeTitle = 1;
        int fontsizeContent = 0;
        int fontsizeFoot = 1;
        String divideStar = "************************************************" + "";
        String divideLine = "-----------------------------------------------" + "";
        try {
            if (mPrinter.getPrinterStatus() != 0) {
                return;
            }
            mPrinter.lineWrap(1);
            int width = divideLine.length() * 5 / 12;
            String productListTitle = formatTitle(width);
            mPrinter.setAlignMode(1);
            mPrinter.setFontZoom(fontsizeTitle, fontsizeTitle);
            mPrinter.sendRawData(boldOn());
//            mPrinter.printText(printMenuBean.getTitleName());

            mPrinter.flush();
            mPrinter.setAlignMode(0);
            mPrinter.setFontZoom(fontsizeContent, fontsizeContent);
            mPrinter.sendRawData(boldOff());

            //星线
            mPrinter.printText(divideStar);
            //图片加店铺
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.print_logo);
            if (bitmap.getWidth() > 384) {
                int newHeight = (int) (1.0 * bitmap.getHeight() * 384 / bitmap.getWidth());
                bitmap = BitmapUtils.scale(bitmap, 384, newHeight);
            }
            mPrinter.printBitmap(bitmap, 2);
            mPrinter.flush();
            mPrinter.printText(printMenuBean.getShopName());
            mPrinter.flush();
            if (printMenuBean.getPrintType() == 0) {
                //商品单据
//                mPrinter.printText("单号：" + printMenuBean.getOrderCode());
            } else {
                //交接班单据
                mPrinter.printText("收银时间：");
                mPrinter.printText(printMenuBean.getStartTime());
                mPrinter.printText("至：");
                mPrinter.printText(printMenuBean.getEndTime());
                mPrinter.flush();
                mPrinter.printText("收银员：" + printMenuBean.getCashierUser());
            }
            mPrinter.flush();
            //打印时间
            mPrinter.printText("打印时间：" + printMenuBean.getPrintTime());
            mPrinter.flush();
            //星线
            mPrinter.printText(divideStar);
            mPrinter.flush();
            if (printMenuBean.getPrintType() == 0) {
                //商品列表数据
                mPrinter.printText(productListTitle);
                mPrinter.printText(divideLine);
                printProductList(printMenuBean, fontsizeContent, divideLine, width);
            } else {
                //交接班列表数据
                mPrinter.printText(divideLine);
                printChangeShiftList(printMenuBean, fontsizeContent, divideLine, width);
            }
            //星线
            mPrinter.printText(divideStar);
            mPrinter.lineWrap(1);
            //商品打印添加
            if (printMenuBean.getPrintType() == 0) {
//                mPrinter.printText("店铺地址：" + printMenuBean.getShopAddress());
                mPrinter.flush();
                mPrinter.printText("收银员：" + printMenuBean.getCashierUser());
                mPrinter.flush();
                mPrinter.printText("联系电话：" + printMenuBean.getContactPhone());
                mPrinter.flush();
                mPrinter.printText("客户需知：" + printMenuBean.getRemark());
            }
            mPrinter.flush();
            mPrinter.lineWrap(3);
            mPrinter.cutPaper(0, 0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatTitle(int width) {
        Log.e("@@@@@", width + "=======");

        //商品列表打印的title
        String[] title = {
                ResourcesUtils.getString(context, R.string.menus_product_name),
                ResourcesUtils.getString(context, R.string.menus_unit_num),
                ResourcesUtils.getString(context, R.string.menus_unit_price),
                ResourcesUtils.getString(context, R.string.menus_total_price),
        };
        StringBuffer sb = new StringBuffer();
        int blank1 = width - String_length(title[0]);
        int blank2 = width - String_length(title[1]);
        int blank3 = width - String_length(title[2]);

        sb.append(title[0]);
        sb.append(addblank(blank1));

        sb.append(title[1]);
        sb.append(addblank(blank2));

        sb.append(title[2]);
        sb.append(addblank(blank3));

        sb.append(title[3]);
        return sb.toString();
    }

    private void printNewline(String str, int width) throws Exception {
        List<String> strings = PrintUtils.getStrList(str, width);
        for (String string : strings) {
            mPrinter.printText(string);
            mPrinter.flush();
        }
    }

    private void printProductList(PrintMenuBean printMenuBean, int fontsizeContent, String divide2, int width) throws Exception {
        int blank1;
        int blank2;

        int maxNameWidth = isZh() ? (width - 2) / 2 : (width - 2);
        StringBuffer sb = new StringBuffer();
        for (ShopCartBean.ProductListBean listBean : printMenuBean.getProductList()) {
            sb.setLength(0);
            //名称
            String name = listBean.getProductName();

            String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";


            blank1 = width - String_length(name.length() > maxNameWidth ? name1 : name) + 1;
            blank2 = width - 2;

            sb.append(name.length() > maxNameWidth ? name1 : name);
            sb.append(addblank(blank1));

            //数量
            sb.append(listBean.getProductNum());
            sb.append(addblank(blank2));

            //单价
            sb.append(NumUtils.getDoubleStr(listBean.getProductPrice()));
            sb.append(addblank(blank2));//单价的空格

            //金额
            sb.append(NumUtils.getDoubleStr(listBean.getProductNum() * listBean.getProductPrice()));
            mPrinter.printText(sb.toString() + "");
            mPrinter.flush();
            if (name.length() > maxNameWidth) {
                printNewline(name.substring(maxNameWidth), maxNameWidth);
            }

        }
        mPrinter.printText(divide2);
        mPrinter.flush();

        //底部
        sb.setLength(0);
        sb.append("总数：").append(printMenuBean.getTotalNum());
        mPrinter.flush();
        sb.append("总金额：").append(printMenuBean.getTotalPrice());
        mPrinter.flush();
        sb.append("促销：").append(printMenuBean.getPromotionPrice());
        mPrinter.flush();
        sb.append("返利：").append(printMenuBean.getRebatePrice());
        mPrinter.printText(sb.toString());


        mPrinter.sendRawData(boldOn());
        mPrinter.printText("实际付款：" + printMenuBean.getActualPrice());
        mPrinter.sendRawData(boldOff());
        mPrinter.flush();
        sb.setLength(0);
    }

    private void printChangeShiftList(PrintMenuBean menuBean, int fontsizeContent, String divide2, int width) throws Exception {
        int blank1;
        int blank2;

        int maxNameWidth = isZh() ? (width - 2) / 2 : (width - 2);
        StringBuffer sb = new StringBuffer();
        for (PrintMenuBean.ChangeShiftListBean listBean : menuBean.getChangeShiftList()) {
            sb.setLength(0);
            String name = listBean.getCollectionType();

            String name1 = name.length() > maxNameWidth ? name.substring(0, maxNameWidth) : "";


            blank1 = width - String_length(name.length() > maxNameWidth ? name1 : name) + 1;
            blank2 = width - 2;

            sb.append(name.length() > maxNameWidth ? name1 : name);
            sb.append(addblank(blank1));

            sb.append(listBean.getCollectionNum());
            sb.append(addblank(blank2));

            sb.append(listBean.getCollectionPrice());
            mPrinter.printText(sb.toString() + "");
            mPrinter.flush();
            if (name.length() > maxNameWidth) {
                printNewline(name.substring(maxNameWidth), maxNameWidth);
            }

        }
        mPrinter.printText(divide2);
        mPrinter.flush();
        sb.setLength(0);
    }


    private String formatData(Date nowTime) {
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return time.format(nowTime);
    }

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
        result[2] = 0;
        return result;
    }

    private boolean isZh() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.endsWith("zh"))
            return true;
        else
            return false;
    }

    private byte[] mCmd = new byte[24];

    public synchronized int setCharSize(int hsize, int vsize) {
        int Width = 0;
        if (hsize == 0) {
            Width = 0;
        }
        if (hsize == 1) {
            Width = 16;
        }
        if (hsize == 2) {
            Width = 32;
        }
        if (hsize == 3) {
            Width = 48;
        }
        if (hsize == 4) {
            Width = 64;
        }
        if (hsize == 5) {
            Width = 80;
        }
        if (hsize == 6) {
            Width = 96;
        }

        if (hsize == 7) {
            Width = 112;
        }

        if (Width <= 0) {
            Width = 0;
        }

        if (Width >= 112) {
            Width = 112;
        }

        if (vsize <= 0) {
            vsize = 0;
        }

        if (vsize >= 7) {
            vsize = 7;
        }

        int Mul = Width + vsize;
        this.mCmd[0] = 29;
        this.mCmd[1] = 33;
        this.mCmd[2] = (byte) Mul;

        return /*this.mPrinter.writeIO(this.mCmd, 0, 3, 2000)*/1;
    }


    private int String_length(String rawString) {
        return rawString.replaceAll("[\\u4e00-\\u9fa5]", "SH").length();
    }
}
