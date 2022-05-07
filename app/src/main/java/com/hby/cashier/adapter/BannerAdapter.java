package com.hby.cashier.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.hby.cashier.app.AppConfig;
import com.hby.cashier.utils.LogUtils;

import java.io.File;
import java.util.List;

/**
 * 功能介绍 :
 * 调用方式 :
 * 作   者 :Hyy
 * 创建时间 :2022/4/19
 */
public class BannerAdapter extends PagerAdapter {

    private List<String> bannerList;

    public BannerAdapter(List<String> bannerList) {
        this.bannerList = bannerList;
    }

    @Override
    public int getCount() {
//        return bannerList.size();
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView bannerImageView = new ImageView(container.getContext());
        int realPosition = position % bannerList.size(); // 获取要加载数据的真实位
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bannerImageView.setLayoutParams(lp);
        bannerImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//        Glide.with(container.getContext()).load(bannerList.get(position)).into(bannerImageView);
        Glide.with(container.getContext()).load(bannerList.get(realPosition)).into(bannerImageView);
        container.addView(bannerImageView);
        return bannerImageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}

