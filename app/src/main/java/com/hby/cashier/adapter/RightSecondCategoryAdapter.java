package com.hby.cashier.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.LitePalCategoryBean;
import com.hby.cashier.utils.DensityUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:右侧二级分类列表
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class RightSecondCategoryAdapter extends BaseQuickAdapter<LitePalCategoryBean, BaseViewHolder> {
    private Context context;

    public RightSecondCategoryAdapter(Context context, @Nullable List<LitePalCategoryBean> data) {
        super(R.layout.item_right_second_category, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LitePalCategoryBean bean) {
        ConstraintLayout parentLayout = holder.findView(R.id.itemRightSecondCategory_parentLayout);
        TextView showText = holder.findView(R.id.itemRightSecondCategory_text);
        showText.setText(bean.getDptclassName());
        setViewW(parentLayout);

        holder.setBackgroundResource(R.id.itemRightSecondCategory_parentLayout, R.drawable.shape_all_orange_light);
        showText.setTextColor(ContextCompat.getColor(context, R.color.color_4d));
        if (bean.isSelect()) {
            holder.setBackgroundResource(R.id.itemRightSecondCategory_parentLayout, R.drawable.shape_all_white);
            showText.setTextColor(ContextCompat.getColor(context, R.color.color_orange));
        }
    }

    private void setViewW(ConstraintLayout parentLayout) {
        int parentW = DensityUtils.getScreenW((Activity) context) * 5 / 8;
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) parentLayout.getLayoutParams();
        layoutParams.width = parentW / 5;
        parentLayout.setLayoutParams(layoutParams);
    }
}
