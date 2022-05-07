package com.hby.cashier.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.DialogChangeShiftBean;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.utils.EnumUtils;
import com.hby.cashier.utils.NumUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:交接班弹框的记录列表
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class DialogChangeShiftsAdapter extends BaseQuickAdapter<DialogChangeShiftBean, BaseViewHolder> {
    private Context context;

    public DialogChangeShiftsAdapter(Context context, @Nullable List<DialogChangeShiftBean> data) {
        super(R.layout.item_dialog_change_shifts, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, DialogChangeShiftBean bean) {
        holder.setText(R.id.itemDialogChangeShifts_paymentMethodStr, EnumUtils.getPaymentMethodStr(bean.getOrdPaymentMethod()))
                .setText(R.id.itemDialogChangeShifts_totalNum, bean.getTotalNum() + "单")
                .setText(R.id.itemDialogChangeShifts_totalPrice, "￥" + NumUtils.getDoubleStr(bean.getTotalPrice()));
    }

}
