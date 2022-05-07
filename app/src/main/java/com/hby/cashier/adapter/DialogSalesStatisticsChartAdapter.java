package com.hby.cashier.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.views.DrawRingView;
import com.hby.cashier.weight.dialog.SalesStatisticsDialog;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能介绍:
 * 调用方式:销售统计的饼状图Adapter
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class DialogSalesStatisticsChartAdapter extends BaseQuickAdapter<SalesStatisticsDialog.ChartBean, BaseViewHolder> {
    private Context context;

    public DialogSalesStatisticsChartAdapter(Context context, @Nullable List<SalesStatisticsDialog.ChartBean> data) {
        super(R.layout.item_dialog_sales_statistics_chart_layout, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, SalesStatisticsDialog.ChartBean bean) {
        DrawRingView ringView = holder.findView(R.id.itemDialogSalesStatistics_ringView);
//        TextView priceText = holder.findView(R.id.itemDialogSalesStatistics_priceText);
//
//        priceText.setText(NumUtils.getDoubleStr(bean.getPayPrice()));

        setRingView(ringView, bean);

    }

    private void setRingView(DrawRingView ringView, SalesStatisticsDialog.ChartBean bean) {
        ringView.setArcPercentColor(ContextCompat.getColor(context, bean.getShowColor()))
                .setPercentData(bean.getPercentage() * 100)
                .setMinText(bean.getPayType())
                .show();
    }

}
