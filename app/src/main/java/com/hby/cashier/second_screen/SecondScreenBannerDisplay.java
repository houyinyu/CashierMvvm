package com.hby.cashier.second_screen;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.view.Display;
import android.view.SurfaceView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.hby.cashier.R;
import com.hby.cashier.adapter.BannerAdapter;
import com.hby.cashier.app.AppConfig;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.LitePalConfigurationBean;
import com.hby.cashier.utils.LogUtils;
import com.hyy.mvvm.utils.SPUtils;

import org.litepal.LitePal;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 有副屏banner图的时候显示
 * mail  gaolulin@sunmi.com
 */

public class SecondScreenBannerDisplay extends BasePresentation {


    public SecondScreenBannerDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }


    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_screen_banner_layout);
        viewPager = (ViewPager) findViewById(R.id.screenBanner_viewPager);
        initBannerView();
    }

    private void initBannerView() {
        File imagePath =
                new File(AppConfig.CASHIER_SAVE_PATH, AppConfig.IMAGE_FILE_PATH);
        List<String> imagePaths = getFilesAllName(imagePath.getPath());

        viewPager.setAdapter(new BannerAdapter(imagePaths));
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(40);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        startPager();
    }


    public static List<String> getFilesAllName(String path) {
        //传入指定文件夹的路径
        File file = new File(path);
        File[] files = file.listFiles();
        List<String> imagePaths = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            imagePaths.add(files[i].getPath());
        }
        return imagePaths;
    }

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 0) {
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                handler.sendEmptyMessageDelayed(0, 3000);
            }
            return false;
        }
    });


    private void startPager() {
        handler.removeCallbacksAndMessages(null);
        handler.sendEmptyMessageDelayed(0, 3000);
    }


    @Override
    public void onSelect(boolean isShow) {

    }
}
