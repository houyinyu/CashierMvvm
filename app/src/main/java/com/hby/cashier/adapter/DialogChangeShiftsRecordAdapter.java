package com.hby.cashier.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.hby.cashier.R;
import com.hby.cashier.bean.LitePalChangeShiftRecordBean;
import com.hby.cashier.utils.TimeUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * 功能介绍:
 * 调用方式:交接班记录弹框的列表
 * 作   者: Hyy - 825129541@qq.com
 * 创建电脑: admin
 * 创建时间: 2020/12/2 11:41
 * 最后编辑: 2020/12/2 - Hyy
 *
 * @author HouYinYu
 */
public class DialogChangeShiftsRecordAdapter extends BaseQuickAdapter<LitePalChangeShiftRecordBean, BaseViewHolder> {
    private Context context;

    public DialogChangeShiftsRecordAdapter(Context context, @Nullable List<LitePalChangeShiftRecordBean> data) {
        super(R.layout.item_dialog_change_shifts_record, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, LitePalChangeShiftRecordBean bean) {
        holder.setText(R.id.itemDialogChangeShiftsRecord_startTime, TimeUtils.getTime(bean.getLoginTimeStamp()))
                .setText(R.id.itemDialogChangeShiftsRecord_endTime, TimeUtils.getTime(bean.getOutTimeStamp()));

        holder.setImageResource(R.id.itemDialogChangeShiftsRecord_checkImage, R.drawable.icon_box_check_false);
        if (bean.isSelect()) {
            holder.setImageResource(R.id.itemDialogChangeShiftsRecord_checkImage, R.drawable.icon_box_check_true);
        }
    }
}
