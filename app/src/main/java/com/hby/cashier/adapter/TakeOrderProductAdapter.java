package com.hby.cashier.adapter;

import android.content.Context;
import android.widget.LinearLayout;

import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.utils.NumUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:取单右侧侧商品列表
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class TakeOrderProductAdapter extends BaseQuickAdapter<ShopCartBean.ProductListBean, BaseViewHolder> {
    private Context context;

    public TakeOrderProductAdapter(Context context, @Nullable List<ShopCartBean.ProductListBean> data) {
        super(R.layout.item_take_order_product, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ShopCartBean.ProductListBean bean) {
        holder.setText(R.id.itemTakeOrderRightProduct_productName, bean.getProductName())
                .setText(R.id.itemTakeOrderRightProduct_specName, bean.getSpecName())
                .setText(R.id.itemTakeOrderRightProduct_unitName, bean.getUnitName())
                .setText(R.id.itemTakeOrderRightProduct_productNum, String.valueOf(bean.getProductNum()))
                .setText(R.id.itemTakeOrderRightProduct_unitPrice, "￥" +  NumUtils.getDoubleStr(bean.getProductPrice()))
                .setText(R.id.itemTakeOrderRightProduct_totalPrice, "￥" + NumUtils.getDoubleStr(bean.getProductPrice() * bean.getProductNum()));

        holder.setGone(R.id.itemTakeOrderRightProduct_weighImage, true);
        if (bean.getIsInBulk()>0){
            holder.setGone(R.id.itemTakeOrderRightProduct_weighImage, false);
        }
        LinearLayout parentLayout = holder.getView(R.id.itemTakeOrderRightProduct_layout);
        parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_f5));
        if (holder.getAdapterPosition() % 2 == 0) {
            //偶数
            parentLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.color_white));
        }
    }
}
