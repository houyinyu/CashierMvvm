package com.hby.cashier.weight.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.hby.cashier.R;
import com.hby.cashier.adapter.DialogSalesStatisticsChartAdapter;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderReturnBean;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.bean.SalesStatisticsBean;
import com.hby.cashier.http.RequestConfig;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.TimeUtils;
import com.hby.cashier.view_model.TakeOrderModel;
import com.hby.cashier.weight.SingleChosePopupWindow;
import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.base.BaseViewModel;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.http.HttpUtils;
import com.hyy.mvvm.utils.DialogUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * 销售统计弹框
 */
public class SalesStatisticsDialog extends Dialog {

    private Context context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm();
    }

    private DialogClickListener clickListener;

    public SalesStatisticsDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public SalesStatisticsDialog setOnDialogCancelListener(OnCancelListener cancelListener) {
        this.setOnCancelListener(cancelListener);
        return this;
    }

    public SalesStatisticsDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }

    private BaseViewModel viewModel;

    public SalesStatisticsDialog setModel(BaseViewModel viewModel) {
        this.viewModel = viewModel;
        return this;
    }

    /**
     * 设置整个弹出框的视图
     */
    private RecyclerView chartRecycler;
    private TextView startTimeBtn;
    private TextView endTimeBtn;
    private TextView salesPrice;
    private TextView returnPrice;

    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_sales_statistics_layout, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogSalesStatistics_dismissImage);
        TextView cycleBtn = mView.findViewById(R.id.dialogSalesStatistics_cycleBtn);
        startTimeBtn = mView.findViewById(R.id.dialogSalesStatistics_startTimeBtn);
        endTimeBtn = mView.findViewById(R.id.dialogSalesStatistics_endTimeBtn);
        TextView searchBtn = mView.findViewById(R.id.dialogSalesStatistics_searchBtn);
        salesPrice = mView.findViewById(R.id.dialogSalesStatistics_salesPrice);
        returnPrice = mView.findViewById(R.id.dialogSalesStatistics_returnPrice);
        chartRecycler = mView.findViewById(R.id.dialogSalesStatistics_chartRecycler);

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        cycleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(cycleBtn);
            }
        });
        startTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseStartTimePicker(startTimeBtn);
            }
        });
        endTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseEndTimePicker(endTimeBtn);
            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(startTime)) {
                    ToastUtils.showShort("请选择开始时间");
                    return;
                }
                if (TextUtils.isEmpty(endTime)) {
                    ToastUtils.showShort("请选择结束时间");
                    return;
                }
                getStatisticsData();
            }
        });
        super.setContentView(mView);
    }


    public SalesStatisticsDialog getData() {
        getStatisticsData();
        return this;
    }


    private void getStatisticsData() {
        viewModel.showDialog();
        String sessionID = SPUtils.getInstance().getString(DataKey.KEY_SESSION_ID);
        String deviceID = SPUtils.getInstance().getString(DataKey.KEY_DEVICE_ID);
        JSONObject jsonData = new JSONObject();
        jsonData.put("stime", startTime);
        jsonData.put("etime", endTime);
        jsonData.put("syDeviceId", "dev_" + deviceID);
        HttpUtils.post(RequestConfig.request_orderStatistics)
                .headers("sessionID", sessionID)
                .upJson(jsonData.toJSONString())
                .execute(new SimpleCallBack<String>() {

                    @Override
                    public void onCompleted() {
                        super.onCompleted();
                        viewModel.dismissDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        viewModel.dismissDialog();
                    }

                    @Override
                    public void onSuccess(String data) {
                        BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                        if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                            SalesStatisticsBean statisticsBean = JsonUtils.parseJson(data, SalesStatisticsBean.class);
                            if (statisticsBean != null && statisticsBean.getResultObject() != null) {
                                setData(statisticsBean.getResultObject());
                            }
                        } else {
                            DialogUtils.singleDialog((Activity) context, "提示", baseBean.getMessage(), "确定", null)
                                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialog) {
                                        }
                                    });
                        }
                    }
                });
    }

    private void setData(SalesStatisticsBean.ResultObjectDTO data) {
        salesPrice.setText("￥" + data.getOrdPrice());
        returnPrice.setText("￥" + data.getOrdBackPrice());
        percentageList = new ArrayList<>();
        percentageList.add(data.getWx());
        percentageList.add(data.getAli());
        percentageList.add(data.getCash());
        percentageList.add(data.getFree());
        setChartAdapter();
    }


    /**
     * 圆环Adapter
     */
    private String[] payType = new String[]{"微信", "支付宝", "现金", "免单"};
    private Integer[] showColor = new Integer[]{R.color.color_green, R.color.color_blue, R.color.color_yellow, R.color.color_orange};
    private List<Double> percentageList = new ArrayList<>();

    private void setChartAdapter() {
        List<ChartBean> chartList = new ArrayList<>();
        if (percentageList.size() == 0) {
            percentageList.add(0.0);
            percentageList.add(0.0);
            percentageList.add(0.0);
            percentageList.add(0.0);
        }
        for (int i = 0; i < 4; i++) {
            ChartBean chartBean = new ChartBean();
            chartBean.setPayType(payType[i]);
            chartBean.setPercentage(percentageList.get(i));
            chartBean.setShowColor(showColor[i]);
            chartList.add(chartBean);
        }

        DialogSalesStatisticsChartAdapter productAdapter = new DialogSalesStatisticsChartAdapter(context, chartList);
        chartRecycler.setLayoutManager(new GridLayoutManager(context, 4));
        chartRecycler.setNestedScrollingEnabled(false);
        chartRecycler.setAdapter(productAdapter);
    }

    public class ChartBean {
        private String payType;
        private double payPrice;
        private double percentage;
        private int showColor;

        public String getPayType() {
            return payType;
        }

        public void setPayType(String payType) {
            this.payType = payType;
        }

        public double getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(double payPrice) {
            this.payPrice = payPrice;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        public int getShowColor() {
            return showColor;
        }

        public void setShowColor(int showColor) {
            this.showColor = showColor;
        }
    }

    /**
     * 本周、本月、本年
     */
    private List<String> strList = new ArrayList<>();
    private String[] strs = new String[]{"本周", "本月", "本年", "自定义"};

    private void showPopup(TextView showView) {
        strList = new ArrayList();
        for (int i = 0; i < strs.length; i++) {
            strList.add(strs[i]);
        }
        SingleChosePopupWindow popupWindow = new SingleChosePopupWindow((BaseActivity) context, strList);
        popupWindow.initView().setDefaultW(showView).show(showView, new SingleChosePopupWindow.Listener() {
            @Override
            public void onItemClickListener(int position) {
                showView.setText(strList.get(position));
                String startStr = "";
                String endStr = "";
                startTimeBtn.setEnabled(false);
                endTimeBtn.setEnabled(false);
                startTimeBtn.setBackgroundResource(R.drawable.shape_all_e6);
                endTimeBtn.setBackgroundResource(R.drawable.shape_all_e6);

                if (strList.get(position).equals("自定义")) {
                    startTimeBtn.setEnabled(true);
                    endTimeBtn.setEnabled(true);
                    startTimeBtn.setBackgroundResource(R.drawable.shape_e6_line);
                    endTimeBtn.setBackgroundResource(R.drawable.shape_e6_line);
                } else {
                    switch (strList.get(position)) {
                        case "本周":
                            startStr = TimeUtils.getTimeOfWeekStart("yyyy-MM-dd");
                            endStr = TimeUtils.getTimeOfWeekEnd("yyyy-MM-dd");
                            break;
                        case "本月":
                            startStr = TimeUtils.getTimeOfMonthStart("yyyy-MM-dd");
                            endStr = TimeUtils.getTimeOfMonthEnd("yyyy-MM-dd");
                            break;
                        case "本年":
                            startStr = TimeUtils.getTimeOfYearStart("yyyy-MM-dd");
                            endStr = TimeUtils.getTimeOfYearEnd("yyyy-MM-dd");
                            break;
                    }
                    startTime = startStr + " 00:00:00";
                    endTime = endStr + " 23:59:59";
                    startTimeBtn.setText(startTime);
                    endTimeBtn.setText(endTime);

                    getStatisticsData();
                }
            }
        });

    }

    /**
     * 时间选择
     */
    private String startTime = "";
    private String endTime = "";

    private void choseStartTimePicker(TextView timeText) {
        TimeUtils.showDatePickerDialog(context, "请选择开始时间", new TimeUtils.OnDatePickerListener() {

            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                String timeShow = year + "-" + month + "-" + dayOfMonth + " " + "00:00:00";
                if (!TextUtils.isEmpty(endTime) && TimeUtils.subtractStamp(timeShow, endTime) < 0) {
                    ToastUtils.showShort("开始时间必须小于结束时间");
                    return;
                }
                startTime = timeShow;
                timeText.setText(timeShow);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    private void choseEndTimePicker(TextView timeText) {
        TimeUtils.showDatePickerDialog(context, "请选择结束时间", new TimeUtils.OnDatePickerListener() {

            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                String timeShow = year + "-" + month + "-" + dayOfMonth + " " + "00:00:00";

                if (!TextUtils.isEmpty(startTime) && TimeUtils.subtractStamp(startTime, timeShow) < 0) {
                    ToastUtils.showShort("结束时间必须大于开始时间");
                    return;
                }

                endTime = timeShow;
                timeText.setText(timeShow);
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用

        lp.width = (int) (d.widthPixels * 0.7);
        lp.height = (int) (d.heightPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

}

