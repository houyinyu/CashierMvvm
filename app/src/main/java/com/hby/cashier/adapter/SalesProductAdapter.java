package com.hby.cashier.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.utils.NumUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:交接班记录查看的销售商品
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class SalesProductAdapter extends BaseQuickAdapter<LitePalOrderDetailsBean.OrderProductAoListBean, BaseViewHolder> {
    private Context context;

    public SalesProductAdapter(Context context, @Nullable List<LitePalOrderDetailsBean.OrderProductAoListBean> data) {
        super(R.layout.item_sales_product, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LitePalOrderDetailsBean.OrderProductAoListBean bean) {
        holder.setText(R.id.itemSalesProduct_productName,bean.getOrdProductName())
                .setText(R.id.itemSalesProduct_specName,bean.getOrdProductSpecName())
                .setText(R.id.itemSalesProduct_unitName,bean.getOrdProductUnitName())
                .setText(R.id.itemSalesProduct_unitPrice, "￥" +NumUtils.getDoubleStr(bean.getOrdProductPrice()))
                .setText(R.id.itemSalesProduct_productNum,String.valueOf(bean.getOrdProductNum()))
                .setText(R.id.itemSalesProduct_totalPrice,"￥" +NumUtils.getDoubleStr(bean.getOrdProductPrice()*bean.getOrdProductNum()));


        holder.setGone(R.id.itemSalesProduct_weigh, true);
        if (bean.getIsInBulk()>0){
            holder.setGone(R.id.itemSalesProduct_weigh, false);
        }
        LinearLayout parentLayout = holder.getView(R.id.itemSalesProduct_layout);
        parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_f5));
        if (holder.getAdapterPosition() % 2 == 0) {
            //偶数
            parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
        }
    }
}
