package com.hby.cashier.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.app.AppConfig;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.bean.ProductGroupBean;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.LogUtils;
import com.hby.cashier.utils.NumUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.List;

/**
 * 功能介绍:右侧商品列表
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class RightProductAdapter extends BaseQuickAdapter<LitePalProductBean, BaseViewHolder> {
    private Context context;

    public RightProductAdapter(Context context, @Nullable List<LitePalProductBean> data) {
        super(R.layout.item_right_product, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LitePalProductBean bean) {
        holder.setText(R.id.itemRightProduct_productName, bean.getProName())
                .setText(R.id.itemRightProduct_specText, bean.getSpecification())
                .setText(R.id.itemRightProduct_unitText, bean.getUnitName())
                .setText(R.id.itemRightProduct_priceText, "￥" + NumUtils.getProductPrice(bean));

        holder.setGone(R.id.itemRightProduct_brandName, true);
        holder.setGone(R.id.itemRightProduct_promotionText, true);//活动
        holder.setGone(R.id.itemRightProduct_weigh, true);//称重商品
        ImageView imageView = holder.getView(R.id.itemRightProduct_productImage);

        Glide.with(context)
                .load(AppConfig.qiUrl(bean.getImgUrl()))
                .apply(new RequestOptions()
                        .placeholder(R.drawable.product_image_empty)
                        .error(R.drawable.product_image_empty))
                .into(imageView);

        if (bean.getProBrandName() != null && !TextUtils.isEmpty(bean.getProBrandName())) {
            holder.setGone(R.id.itemRightProduct_brandName, false);
            holder.setText(R.id.itemRightProduct_brandName, "[" + bean.getProBrandName() + "]");
        }

        if (bean.getIsInBulk() > 0) {
            //是称重商品
            holder.setGone(R.id.itemRightProduct_weigh, false);
        }

        //活动
        if (bean.getEventType() > 0) {
            holder.setGone(R.id.itemRightProduct_promotionText, false);
            holder.setText(R.id.itemRightProduct_promotionText, EnumUtils.getEventStr(bean.getEventType()));
        }
    }


}
