package com.hby.cashier.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.LitePalHangingOrderBean;
import com.hby.cashier.utils.NumUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:取单左侧列表
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class TakeOrderLeftAdapter extends BaseQuickAdapter<LitePalHangingOrderBean, BaseViewHolder> {
    private Context context;

    public TakeOrderLeftAdapter(Context context, @Nullable List<LitePalHangingOrderBean> data) {
        super(R.layout.item_take_order_left_order, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LitePalHangingOrderBean bean) {
        ConstraintLayout parentView = holder.getView(R.id.itemTakeOrderLeftOrder_parentView);
        View lineView = holder.getView(R.id.itemTakeOrderLeftOrder_lineView);
        TextView orderNoText = holder.getView(R.id.itemTakeOrderLeftOrder_orderNoText);
        TextView orderNo = holder.getView(R.id.itemTakeOrderLeftOrder_orderNo);
        TextView orderPriceText = holder.getView(R.id.itemTakeOrderLeftOrder_orderPriceText);
        TextView orderPrice = holder.getView(R.id.itemTakeOrderLeftOrder_orderPrice);
        TextView timeText = holder.getView(R.id.itemTakeOrderLeftOrder_timeText);

        holder.setText(R.id.itemTakeOrderLeftOrder_orderNo, bean.getOrderNo())
                .setText(R.id.itemTakeOrderLeftOrder_orderPrice, "￥" + NumUtils.getDoubleStr(bean.getTotalPrice()))
                .setText(R.id.itemTakeOrderLeftOrder_timeText, bean.getCreateTime());


        holder.setGone(R.id.itemTakeOrderLeftOrder_lineView, true);
        if (holder.getAdapterPosition() != 0) {
            holder.setGone(R.id.itemTakeOrderLeftOrder_lineView, false);
        }
        parentView.setBackgroundResource(R.drawable.shape_all_white);
        orderNoText.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        orderNo.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        orderPriceText.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        orderPrice.setTextColor(ContextCompat.getColor(context, R.color.color_1a));
        timeText.setTextColor(ContextCompat.getColor(context, R.color.color_999));

        if (bean.isSelect()) {
            parentView.setBackgroundResource(R.drawable.shape_all_orange);
            orderNoText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            orderNo.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            orderPriceText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            orderPrice.setTextColor(ContextCompat.getColor(context, R.color.color_white));
            timeText.setTextColor(ContextCompat.getColor(context, R.color.color_white));
        }
    }
}
