package com.hby.cashier.view_model;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.hby.cashier.app.AppConfig;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.CreateOrderBean;
import com.hby.cashier.bean.LitePalConfigurationBean;
import com.hby.cashier.bean.LitePalOrderDetailsBean;
import com.hby.cashier.utils.LogUtils;
import com.hyy.mvvm.base.BaseViewModel;
import com.hyy.mvvm.base.SingleLiveEvent;
import com.hyy.mvvm.binding.BindingAction;
import com.hyy.mvvm.binding.BindingCommand;
import com.hyy.mvvm.data.DemoRepository;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;


public class MainViewModel extends BaseViewModel<DemoRepository> {

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //顶部数据同步监听
        public SingleLiveEvent dataSynchronizationEvent = new SingleLiveEvent<>();
        //顶部锁屏监听
        public SingleLiveEvent lockScreenEvent = new SingleLiveEvent<>();
        //顶部最小化监听
        public SingleLiveEvent minimizeUIEvent = new SingleLiveEvent<>();

        //清空按钮监听
        public SingleLiveEvent clearShopCartEvent = new SingleLiveEvent<>();
        //挂单按钮监听
        public SingleLiveEvent hangingOrderEvent = new SingleLiveEvent<>();
        //取单按钮监听
        public SingleLiveEvent takeOrderEvent = new SingleLiveEvent<>();
        //收款按钮监听
        public SingleLiveEvent collectionEvent = new SingleLiveEvent<>();

        //右侧无码商品监听
        public SingleLiveEvent codeLessEvent = new SingleLiveEvent<>();
        //右侧去皮监听
        public SingleLiveEvent peelEvent = new SingleLiveEvent<>();
        //右侧置零监听
        public SingleLiveEvent setZeroEvent = new SingleLiveEvent<>();
        //右侧交接班监听
        public SingleLiveEvent changeShiftsEvent = new SingleLiveEvent<>();
        //右侧交接班记录监听
        public SingleLiveEvent changeShiftsRecordEvent = new SingleLiveEvent<>();
        //右侧销售统计监听
        public SingleLiveEvent salesStatisticsEvent = new SingleLiveEvent<>();
        //右侧销售记录监听
        public SingleLiveEvent salesRecordEvent = new SingleLiveEvent<>();

        //副屏banner保存图片后出发
        public SingleLiveEvent bannerImageEvent = new SingleLiveEvent<>();
    }

    public MainViewModel(@NonNull Application application) {
        super(application);
        getLoginData();
    }

    public MainViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    //数据同步点击事件
    public BindingCommand dataSynchronizationClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.dataSynchronizationEvent.call();
        }
    });
    //锁屏点击事件
    public BindingCommand lockScreenClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.lockScreenEvent.call();
        }
    });
    //最小化点击事件
    public BindingCommand minimizeUIClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.minimizeUIEvent.call();
        }
    });

    //左侧 清空 点击事件
    public BindingCommand clearClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.clearShopCartEvent.call();
        }
    });

    //取单点击事件
    public BindingCommand takeOrderClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.takeOrderEvent.call();
        }
    });

    //左侧 挂单 点击事件
    public BindingCommand hangingOrderClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.hangingOrderEvent.call();
        }
    });
    //左侧 收款 点击事件
    public BindingCommand collectionClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.collectionEvent.call();
        }
    });

    //右侧 无码商品 点击事件
    public BindingCommand codelessProductClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.codeLessEvent.call();
        }
    });
    //右侧 去皮 点击事件
    public BindingCommand peelClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.peelEvent.call();
        }
    });
    //右侧 置零 点击事件
    public BindingCommand setZeroClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.setZeroEvent.call();
        }
    });
    //右侧 交接班 点击事件
    public BindingCommand changeShiftsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.changeShiftsEvent.call();
        }
    });
    //右侧 交接班记录 点击事件
    public BindingCommand changeShiftsRecordClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.changeShiftsRecordEvent.call();
        }
    });
    //右侧 销售记录 点击事件
    public BindingCommand salesRecordsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.salesRecordEvent.call();
        }
    });
    //右侧 销售统计 点击事件
    public BindingCommand salesStatisticsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.salesStatisticsEvent.call();
        }
    });


    /**
     * 获取登录的配置信息
     */
    private void getLoginData() {
        File downloadPath =
                new File(AppConfig.CASHIER_SAVE_PATH, AppConfig.IMAGE_FILE_PATH);
        if (downloadPath.exists()) {
            LogUtils.i("*******************************删除了图片文件夹:");
            downloadPath.delete();
        }
        String userMid = SPUtils.getInstance().getString(DataKey.KEY_USER_MID);
        //根据登录用户的mid查询当前登录的店铺信息
        List<LitePalConfigurationBean> selectConfigurationList =
                LitePal.where("symid = ?", userMid).find(LitePalConfigurationBean.class);
        if (selectConfigurationList.size() > 0) {
            getLoginImage(selectConfigurationList.get(0).getSyImgAd1(), "bannerImage1");
            getLoginImage(selectConfigurationList.get(0).getSyImgAd2(), "bannerImage2");
            getLoginImage(selectConfigurationList.get(0).getSyImgAd3(), "bannerImage3");
            getLoginImage(selectConfigurationList.get(0).getSyImgAd4(), "bannerImage4");
            getLoginImage(selectConfigurationList.get(0).getSyImgAd5(), "bannerImage5");
        }
        uc.bannerImageEvent.call();
    }

    /**
     * 保存当前用户的banner图
     */
    private void getLoginImage(String imageUrl, String fileName) {
        if (imageUrl == null || TextUtils.isEmpty(imageUrl)) {
            LogUtils.i("*******************************图片没有配置:" + fileName);
            return;
        }
        File downloadPath =
                new File(AppConfig.CASHIER_SAVE_PATH, AppConfig.IMAGE_FILE_PATH + "/");

        EasyHttp.downLoad(AppConfig.qiUrl(imageUrl))
                .savePath(downloadPath.getPath())
                .saveName(fileName + ".png")//不设置默认名字是时间戳生成的
                .execute(new DownloadProgressCallBack<String>() {
                    @Override
                    public void update(long bytesRead, long contentLength, boolean done) {
                        int progress = (int) (bytesRead * 100 / contentLength);
                        if (done) {//下载完成

                        }
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onComplete(String path) {
                        //下载完成，path：下载文件保存的完整路径
                        LogUtils.i("保存了banner图片：" + fileName);
                    }

                    @Override
                    public void onError(ApiException e) {
                        //下载失败
                    }
                });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
