package com.hby.cashier.weight;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.utils.DensityUtils;
import com.hyy.mvvm.base.BaseActivity;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:单选popup
 * 调用方式:
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/11/20 14:14
 * 最后编辑: 2020/11/20 - Hyy
 *
 * @author HouYinYu
 */
public class SingleChosePopupWindow extends PopupWindow {
    private BaseActivity context;
    private List<String> list;
    private PopupCategoryAdapter adapter;

    public SingleChosePopupWindow(BaseActivity context, List<String> list) {
        super(context);
        this.context = context;
        this.list = list;

        int screenW = DensityUtils.getScreenW(context);
        int crevice = DensityUtils.dip2px(context, 12) * 3;
        int popupH = LinearLayout.LayoutParams.WRAP_CONTENT;

        if (list.size() > 5) {
            popupH = DensityUtils.dip2px(context, 32) * 5 + DensityUtils.dip2px(context, 14);
        }
        this.setWidth((screenW - crevice) / 2);
        this.setHeight(popupH);

        this.setFocusable(true);
        this.setAnimationStyle(R.style.AnimPopup);
        this.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.shape_e6_line));
    }

    public SingleChosePopupWindow initView() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_single_category, null);
        this.setContentView(contentView);
        RecyclerView categoryRecycler = contentView.findViewById(R.id.popupSingle_recycler);

        //设置Adapter数据
        if (adapter == null) {
            adapter = new PopupCategoryAdapter(list);
            categoryRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
            categoryRecycler.setAdapter(adapter);
        } else {
            adapter.setList(list);
        }
        return this;
    }

    public SingleChosePopupWindow setDefaultW(View showView) {
        this.setWidth(DensityUtils.getViewWidth(showView));
        return this;
    }

    public void show(View showView, Listener listener) {
        this.showAsDropDown(showView, 0, DensityUtils.dip2px(context, 1));

        adapter.addChildClickViewIds(R.id.itemPopupSingleCategoryText);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                listener.onItemClickListener(position);
                dismiss();
            }
        });
    }

    private class PopupCategoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public PopupCategoryAdapter(@Nullable List<String> data) {
            super(R.layout.item_popup_single_category, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, String s) {
            View lineView = holder.findView(R.id.itemPopupSingleCategoryLine);
            holder.setText(R.id.itemPopupSingleCategoryText, s);
            holder.setGone(R.id.itemPopupSingleCategoryLine,true);
            if (holder.getAdapterPosition()!=0){
                holder.setGone(R.id.itemPopupSingleCategoryLine,false);
            }
        }
    }

    public interface Listener {
        void onItemClickListener(int position);
    }

}
