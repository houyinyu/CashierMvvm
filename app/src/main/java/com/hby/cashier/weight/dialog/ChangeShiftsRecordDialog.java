package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.hby.cashier.R;
import com.hby.cashier.adapter.DialogChangeShiftsAdapter;
import com.hby.cashier.adapter.DialogChangeShiftsRecordAdapter;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.DialogChangeShiftBean;
import com.hby.cashier.bean.LitePalChangeShiftRecordBean;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.bean.PrintMenuBean;
import com.hby.cashier.printer.PrinterServiceView;
import com.hby.cashier.ui.SalesProductActivity;
import com.hby.cashier.utils.EnumUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


/**
 * 交接班记录弹框
 */
public class ChangeShiftsRecordDialog extends Dialog {

    private Context context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public void cancel() {
        }

        public abstract void confirm();
    }

    private DialogClickListener clickListener;

    public ChangeShiftsRecordDialog setOnDialogClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public ChangeShiftsRecordDialog setOnDialogCancelListener(OnCancelListener cancelListener) {
        this.setOnCancelListener(cancelListener);
        return this;
    }

    public ChangeShiftsRecordDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }


    /**
     * 设置整个弹出框的视图
     */
    private RecyclerView timeRecycler;
    private TextView startTimeBtn;
    private TextView endTimeBtn;

    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_change_shifts_record_layout, null);
        ImageView dismissImage = mView.findViewById(R.id.dialogChangeShiftsRecord_dismissImage);
        TextView cashierUser = mView.findViewById(R.id.dialogChangeShiftsRecord_cashier);
        timeRecycler = mView.findViewById(R.id.dialogChangeShiftsRecord_timeRecycler);
        TextView productBtn = mView.findViewById(R.id.dialogChangeShiftsRecord_productBtn);
        TextView printBtn = mView.findViewById(R.id.dialogChangeShiftsRecord_printBtn);

        cashierUser.setText(SPUtils.getInstance().getString(DataKey.KEY_USER_NAME));

        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPos < 0) {
                    ToastUtils.showShort("请选择需要查看的交接班记录");
                    return;
                }
                LitePalChangeShiftRecordBean recordBean = recordList.get(selectPos);
                Intent intent = new Intent(context, SalesProductActivity.class);
                intent.putExtra("recordJson", JsonUtils.Object2Json(recordBean));
                context.startActivity(intent);
            }
        });
        printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectPos < 0) {
                    ToastUtils.showShort("请选择需要打印的交接班记录");
                    return;
                }
                LitePalChangeShiftRecordBean recordBean = recordList.get(selectPos);
                String printJson = recordBean.getPrintJson();
                long loginTimeStamp = recordBean.getLoginTimeStamp();
                long nowTimeStamp = recordBean.getOutTimeStamp();

                List<DialogChangeShiftBean> changeShiftList = JSON.parseArray(printJson, DialogChangeShiftBean.class);
                List<PrintMenuBean.ChangeShiftListBean> changeShiftPrintList = new ArrayList<>();
                for (int i = 0; i < changeShiftList.size(); i++) {
                    PrintMenuBean.ChangeShiftListBean printBean = new PrintMenuBean.ChangeShiftListBean();
                    printBean.setCollectionType(EnumUtils.getPaymentMethodStr(changeShiftList.get(i).getOrdPaymentMethod()));
                    printBean.setCollectionNum(changeShiftList.get(i).getTotalNum());
                    printBean.setCollectionPrice(changeShiftList.get(i).getTotalPrice());
                    changeShiftPrintList.add(printBean);
                }
                PrinterServiceView printView = PrinterServiceView.getInstance();
                String jsonData = printView.buildChangeShiftData(loginTimeStamp, nowTimeStamp, changeShiftPrintList);
                printView.paySuccessToPrinter(jsonData, "");
            }
        });

        setTimeAdapter();

        super.setContentView(mView);
    }


    /**
     * 交接班-收银记录
     */
    private List<LitePalChangeShiftRecordBean> recordList = new ArrayList<>();
    private DialogChangeShiftsAdapter recordAdapter = null;
    private int selectPos = -1;

    private void setTimeAdapter() {
        recordList = LitePal.findAll(LitePalChangeShiftRecordBean.class);

        DialogChangeShiftsRecordAdapter recordAdapter = new DialogChangeShiftsRecordAdapter(context, recordList);
        timeRecycler.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
        timeRecycler.setAdapter(recordAdapter);

        recordAdapter.addChildClickViewIds(R.id.itemDialogChangeShiftsRecord_checkLayout);
        recordAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                switch (view.getId()) {
                    case R.id.itemDialogChangeShiftsRecord_checkLayout:
                        for (int i = 0; i < recordList.size(); i++) {
                            selectPos = position;
                            recordList.get(i).setSelect(i == position);
                        }
                        recordAdapter.setList(recordList);
                        break;
                    default:
                }
            }
        });

    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用

        lp.width = (int) (d.widthPixels * 0.5);
        lp.height = (int) (d.heightPixels * 0.8);
        dialogWindow.setAttributes(lp);
    }

}

