package com.hby.cashier.adapter;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.EventMessage;
import com.hby.cashier.bean.ShopCartBean;
import com.hby.cashier.ui.MainActivity;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.NumUtils;
import com.hby.cashier.weight.dialog.ShopCartNumDialog;
import com.hyy.mvvm.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:左侧购物车商品列表
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class ShopCartAdapter extends BaseQuickAdapter<ShopCartBean, BaseViewHolder> {
    private MainActivity context;

    public ShopCartAdapter(MainActivity context, @Nullable List<ShopCartBean> data) {
        super(R.layout.item_shop_cart_event, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ShopCartBean bean) {
        TextView eventText = holder.getView(R.id.itemShopCartEvent_eventText);
        RecyclerView productRecycler = holder.getView(R.id.itemShopCartEvent_recycler);


        holder.setGone(R.id.itemShopCartEvent_eventText, true);
        if (bean.getEventType() != 0) {
            //有活动
            holder.setGone(R.id.itemShopCartEvent_eventText, false);
            holder.setText(R.id.itemShopCartEvent_eventText, EnumUtils.getEventStr(bean.getEventType()));
        }

        //设置商品数据
        ShopCartProductAdapter productAdapter = new ShopCartProductAdapter(bean.getProductList());
        productRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        productRecycler.setNestedScrollingEnabled(false);
        productRecycler.setAdapter(productAdapter);
    }


    /**
     * 显示活动的Adapter
     */
    private class ShopCartProductAdapter extends BaseQuickAdapter<ShopCartBean.ProductListBean, BaseViewHolder> {

        public ShopCartProductAdapter(@Nullable List<ShopCartBean.ProductListBean> data) {
            super(R.layout.item_shop_cart_product, data);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder holder, ShopCartBean.ProductListBean bean) {
            holder.setText(R.id.itemShopCartProduct_productName, bean.getProductName())
                    .setText(R.id.itemShopCartProduct_unitPrice, "￥" + bean.getProductPrice())
                    .setText(R.id.itemShopCartProduct_numText, String.valueOf(bean.getProductNum()))
                    .setText(R.id.itemShopCartProduct_specName, bean.getSpecName())
                    .setText(R.id.itemShopCartProduct_unitName, "单位：" + bean.getUnitName())
                    .setText(R.id.itemShopCartProduct_totalPrice, "小计：￥" + NumUtils.getDoubleStr(bean.getProductPrice() * bean.getProductNum()));

            ImageButton subtractBtn = holder.getView(R.id.itemShopCartProduct_subtractBtn);
            TextView numText = holder.getView(R.id.itemShopCartProduct_numText);
            ImageButton addBtn = holder.getView(R.id.itemShopCartProduct_addBtn);

            subtractBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getIsInBulk() > 0) {
                        bean.setProductNum(0);
                        context.showShopLayout();
                        return;
                    }
                    bean.setProductNum(bean.getProductNum() - 1);
                    context.showShopLayout();
                }
            });

            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getIsInBulk() > 0) {
                        return;
                    }
                    bean.setProductNum(bean.getProductNum() + 1);
                    context.showShopLayout();
                }
            });

            numText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (bean.getIsInBulk() > 0) {
                        return;
                    }
                    ShopCartNumDialog dialog = new ShopCartNumDialog(context);
                    dialog.setCustomView();
                    dialog.setIsDecimal(!bean.getIsStandard().equals("Y") && bean.getIsMinUnit().equals("N"));
                    dialog.setIsStandard(!bean.getIsStandard().equals("Y"));
                    dialog.setTitleStr(bean.getProductName());
                    dialog.setEditStr(String.valueOf(bean.getProductNum()));
                    dialog.show();
                    dialog.setOnOkClickListener(new ShopCartNumDialog.OkClickListener() {
                        @Override
                        public void onConfirm(String remark) {
                            if (TextUtils.isEmpty(remark)) {
                                ToastUtils.showShort("请输入数量");
                                return;
                            }
                            double productNum = Double.parseDouble(remark);
                            if (productNum <= 0) return;
                            numText.setText(remark);
                            bean.setProductNum(productNum);
                            context.showShopLayout();
                            dialog.dismiss();
                            EventBus.getDefault().post(new EventMessage("hideKeyBoardMessage"));
                        }

                        @Override
                        public void onCancel() {
                            EventBus.getDefault().post(new EventMessage("hideKeyBoardMessage"));
                        }
                    });
                }
            });

        }
    }

}
