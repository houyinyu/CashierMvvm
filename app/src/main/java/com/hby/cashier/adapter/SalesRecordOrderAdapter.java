package com.hby.cashier.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.RecordOrderResultBean;
import com.hby.cashier.utils.EnumUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:销售记录左侧列表
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class SalesRecordOrderAdapter extends BaseQuickAdapter<RecordOrderResultBean.ResultObjectDTO.RowsDTO, BaseViewHolder> {
    private Context context;

    public SalesRecordOrderAdapter(Context context, @Nullable List<RecordOrderResultBean.ResultObjectDTO.RowsDTO> data) {
        super(R.layout.item_sales_record_left_order, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, RecordOrderResultBean.ResultObjectDTO.RowsDTO bean) {
        ConstraintLayout parentView = holder.getView(R.id.itemSalesRecordLeftOrder_parentView);
        View lineView = holder.getView(R.id.itemSalesRecordLeftOrder_lineView);
        TextView orderNoText = holder.getView(R.id.itemSalesRecordLeftOrder_orderNoText);
        TextView orderNo = holder.getView(R.id.itemSalesRecordLeftOrder_orderNo);
        TextView orderPriceText = holder.getView(R.id.itemSalesRecordLeftOrder_orderPriceText);
        TextView orderPrice = holder.getView(R.id.itemSalesRecordLeftOrder_orderPrice);
        TextView timeText = holder.getView(R.id.itemSalesRecordLeftOrder_timeText);
        TextView returnOrderText = holder.getView(R.id.itemSalesRecordLeftOrder_returnOrderText);
        TextView voidText = holder.getView(R.id.itemSalesRecordLeftOrder_voidText);

        holder.setText(R.id.itemSalesRecordLeftOrder_orderNo, bean.getOrdNo())
                .setText(R.id.itemSalesRecordLeftOrder_orderPrice, "￥" + bean.getOrdPrice())
                .setText(R.id.itemSalesRecordLeftOrder_timeText, bean.getCreateTime());
        //退单
        holder.setGone(R.id.itemSalesRecordLeftOrder_returnOrderText, true);
        if (bean.getIsBack() == 1) {
            holder.setGone(R.id.itemSalesRecordLeftOrder_returnOrderText, false);
        }
        //作废
        holder.setGone(R.id.itemSalesRecordLeftOrder_voidText, true);
        if (bean.getOrdStatus() == EnumUtils.OrderType.ORDER_VOID) {
            holder.setGone(R.id.itemSalesRecordLeftOrder_voidText, false);
        }

        holder.setGone(R.id.itemSalesRecordLeftOrder_lineView, true);
        if (holder.getAdapterPosition() != 0) {
            holder.setGone(R.id.itemSalesRecordLeftOrder_lineView, false);
        }
        parentView.setBackgroundResource(R.drawable.shape_all_white);
        orderNoText.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        orderNo.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        orderPriceText.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        orderPrice.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        timeText.setTextColor(ContextCompat.getColor(context, R.color.color_999));

        returnOrderText.setBackgroundResource(R.drawable.shape_all_orange_1);
        returnOrderText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
        voidText.setBackgroundResource(R.drawable.shape_all_orange_1);
        voidText.setTextColor(ContextCompat.getColor(context, R.color.color_white));

        if (bean.isCheck()) {
            parentView.setBackgroundResource(R.drawable.shape_all_orange);
            orderNoText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            orderNo.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            orderPriceText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            orderPrice.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            timeText.setTextColor(ContextCompat.getColor(context, R.color.color_white));

            returnOrderText.setBackgroundResource(R.drawable.shape_all_white_1);
            returnOrderText.setTextColor(ContextCompat.getColor(context, R.color.color_orange));
            voidText.setBackgroundResource(R.drawable.shape_all_white_1);
            voidText.setTextColor(ContextCompat.getColor(context, R.color.color_orange));
        }
    }
}
