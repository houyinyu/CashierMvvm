package com.hby.cashier.weight.dialog;

import android.app.Dialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.hby.cashier.R;
import com.hby.cashier.adapter.RightProductAdapter;
import com.hby.cashier.bean.LitePalProductBean;
import com.hby.cashier.printer.PrinterServiceView;
import com.hby.cashier.utils.EnumUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 商品搜索出来如果有多个商品就用弹窗显示
 */
public class ProductSearchDialog extends Dialog {

    private Context context;
    private LayoutInflater inflater;


    public abstract static class DialogClickListener {
        public abstract void itemClick(LitePalProductBean productBean);
    }

    private DialogClickListener clickListener;

    public ProductSearchDialog setOnItemClickListener(DialogClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    public ProductSearchDialog(Context context) {
        super(context, R.style.DialogTheme);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        setCustomView();
    }


    /**
     * 设置整个弹出框的视图
     */
    private TextView searchTextView;
    private RecyclerView productRecycler;

    public void setCustomView() {
        View mView = inflater.inflate(R.layout.dialog_search_product, null);
        searchTextView = mView.findViewById(R.id.dialogSearchProduct_searchText);
        ImageView dismissImage = mView.findViewById(R.id.dialogSearchProduct_dismissImage);
        productRecycler = mView.findViewById(R.id.dialogSearchProduct_productRecycler);
        dismissImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        super.setContentView(mView);
    }


    public ProductSearchDialog setSearchText(String searchText) {
        searchTextView.setText("搜索 - " + searchText);
        return this;
    }

    public ProductSearchDialog setProductList(List<LitePalProductBean> productList) {
        RightProductAdapter productAdapter = new RightProductAdapter(context, productList);
        productRecycler.setLayoutManager(new GridLayoutManager(context, 3));
        productRecycler.setAdapter(productAdapter);
        productAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (clickListener != null) clickListener.itemClick(productList.get(position));
                dismiss();
            }
        });
        return this;
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

        lp.width = (int) (d.widthPixels * 0.6);
        lp.height = (int) (d.heightPixels * 0.7);
        dialogWindow.setAttributes(lp);
    }

}

