package com.hby.cashier.view_model;

import android.app.Application;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;


import com.alibaba.fastjson.JSONObject;
import com.hby.cashier.app.AppConfig;
import com.hby.cashier.app.DataKey;
import com.hby.cashier.bean.LitePalConfigurationBean;
import com.hby.cashier.bean.LitePalUserBean;
import com.hby.cashier.bean.LoginBean;
import com.hby.cashier.http.RequestConfig;
import com.hby.cashier.ui.MainActivity;
import com.hby.cashier.utils.DeviceUtils;
import com.hby.cashier.utils.EncryptionUtils;
import com.hby.cashier.utils.NetworkUtils;
import com.hyy.mvvm.base.BaseViewModel;
import com.hyy.mvvm.base.SingleLiveEvent;
import com.hyy.mvvm.bean.BaseBean;
import com.hyy.mvvm.binding.BindingAction;
import com.hyy.mvvm.binding.BindingCommand;
import com.hyy.mvvm.data.DemoRepository;
import com.hyy.mvvm.http.HttpUtilsRequestCallBack;
import com.hyy.mvvm.utils.DialogUtils;
import com.hyy.mvvm.utils.JsonUtils;
import com.hyy.mvvm.utils.SPUtils;
import com.hyy.mvvm.utils.ToastUtils;
import com.hyy.mvvm.utils.Utils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.DownloadProgressCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpHeaders;

import org.litepal.LitePal;

import java.io.File;
import java.util.List;
import java.util.UUID;


/**
 * Created by goldze on 2017/7/17.
 */

public class LoginViewModel extends BaseViewModel<DemoRepository> {
    //    //用户名的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    //    //密码的绑定
    public ObservableField<String> password = new ObservableField<>("");
    //用户名清除按钮的显示隐藏绑定
    public ObservableInt clearBtnVisibility = new ObservableInt();
    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //密码开关观察者
        public SingleLiveEvent<Boolean> pSwitchEvent = new SingleLiveEvent<>();

        //登录监听
        public SingleLiveEvent loginClickEvent = new SingleLiveEvent<>();
        //
        public SingleLiveEvent<String> callBackEvent = new SingleLiveEvent<>();
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
    }

    public LoginViewModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
//        //从本地取得数据绑定到View层
        userName.set(SPUtils.getInstance().getString(DataKey.KEY_LOGIN_ACCOUNT));
        password.set(SPUtils.getInstance().getString(DataKey.KEY_LOGIN_PASS));
    }

    //    //密码显示开关  (你可以尝试着狂按这个按钮,会发现它有防多次点击的功能)
//    public BindingCommand passwordShowSwitchOnClickCommand = new BindingCommand(new BindingAction() {
//        @Override
//        public void call() {
//            //让观察者的数据改变,逻辑从ViewModel层转到View层，在View层的监听则会被调用
//            uc.pSwitchEvent.setValue(uc.pSwitchEvent.getValue() == null || !uc.pSwitchEvent.getValue());
//        }
//    });
//    //用户名输入框焦点改变的回调事件
//    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<>(new BindingConsumer<Boolean>() {
//        @Override
//        public void call(Boolean hasFocus) {
//            if (hasFocus) {
//                clearBtnVisibility.set(View.VISIBLE);
//            } else {
//                clearBtnVisibility.set(View.INVISIBLE);
//            }
//        }
//    });
    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
//            uc.loginClickEvent.call();
            login();
        }
    });

    /**
     *
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        showDialog();
        String sessionID = "UserSession_" + EncryptionUtils.md5(userName.get(), "hby2021");
        if (NetworkUtils.isNetworkConnected(getApplication())) {
            JSONObject dataOb = new JSONObject();
            dataOb.put("user_login_account", userName.get());
            dataOb.put("user_login_password", password.get());
            dataOb.put("isSystemLogin", "syslogin");
            model.post(RequestConfig.request_login)
                    .setHeader(new HttpHeaders("sessionID", sessionID))
                    .setPostData(dataOb.toString())
                    .showLoading(false)
                    .postRequest(new HttpUtilsRequestCallBack() {
                        @Override
                        public void onFinish() {
                            super.onFinish();
                            dismissDialog();
                        }

                        @Override
                        public void onError(String error) {
                            super.onError(error);
                            dismissDialog();
                        }

                        @Override
                        public void onSucceed(String data) {
                            dismissDialog();
                            BaseBean baseBean = JsonUtils.parseJson(data, BaseBean.class);
                            if (baseBean.getResult() != -1 && baseBean.getResult() != -2 && baseBean.getResult() != -4) {
                                LoginBean loginBean = JSONObject.parseObject(data, LoginBean.class);
                                LitePalUserBean userBean = new LitePalUserBean();
                                userBean.setUserName(loginBean.getResultObject().getUser().getUserName());
                                userBean.setUserId(loginBean.getResultObject().getUser().getId());
                                userBean.setMid(loginBean.getResultObject().getUser().getMid());
                                sharedDataSave(userBean, sessionID);
                            } else {
                                uc.callBackEvent.setValue(baseBean.getMessage());
                            }

                        }
                    });

            return;
        }

        //先查询用户列表
        List<LitePalUserBean> selectUserList = LitePal.findAll(LitePalUserBean.class);

        for (int i = 0; i < selectUserList.size(); i++) {
            if (userName.get().equals(selectUserList.get(i).getUserAccount()) &&
                    getPass().equals(selectUserList.get(i).getUserPwd())
            ) {
                sharedDataSave(selectUserList.get(i), sessionID);
                return;
            }
        }

        ToastUtils.showShort("请输入正确的账号密码");
    }

    /**
     * 保存登录相关数据
     */
    private void sharedDataSave(LitePalUserBean userBean, String sessionID) {
        //保存登录状态
        SPUtils.getInstance().put(DataKey.KEY_LOGIN, true);
        //保存账号和密码
        SPUtils.getInstance().put(DataKey.KEY_LOGIN_ACCOUNT, userName.get());
        SPUtils.getInstance().put(DataKey.KEY_LOGIN_PASS, password.get());
        //保存收银员姓名
        SPUtils.getInstance().put(DataKey.KEY_USER_NAME, userBean.getUserName());
        //保存当前登录用户的sessionID
        SPUtils.getInstance().put(DataKey.KEY_SESSION_ID, sessionID);
        //保存当前登录用户的MID
        SPUtils.getInstance().put(DataKey.KEY_USER_MID, userBean.getMid());
        //保存当前登录用户的ID
        SPUtils.getInstance().put(DataKey.KEY_USER_ID, userBean.getUserId());
        //保存当前登录的时间戳
        SPUtils.getInstance().put(DataKey.KEY_LOGIN_TIME_STAMP, System.currentTimeMillis());
        //保存当前登录的deviceID
        SPUtils.getInstance().put(DataKey.KEY_DEVICE_ID, DeviceUtils.getSN(getApplication()));

        //根据登录用户的mid查询当前登录的店铺信息
        List<LitePalConfigurationBean> selectConfigurationList =
                LitePal.where("symid = ?", userBean.getMid()).find(LitePalConfigurationBean.class);

        if (selectConfigurationList.size() > 0) {
            //保存店铺名称
            SPUtils.getInstance().put(DataKey.KEY_SHOP_NAME, selectConfigurationList.get(0).getShopName());
            //保存店铺ID
            SPUtils.getInstance().put(DataKey.KEY_SHOP_ID, selectConfigurationList.get(0).getSyMidShopid());
            //保存serviceID-用来生成订单
            SPUtils.getInstance().put(DataKey.KEY_SERVICE_ID, selectConfigurationList.get(0).getSyServerId());

            //保存数据同步频率
            SPUtils.getInstance().put(DataKey.KEY_DATA_SYNC, selectConfigurationList.get(0).getSyDeviceSyncFreq());
            //保存是否需要交接班
            SPUtils.getInstance().put(DataKey.KEY_IS_CHANGE, selectConfigurationList.get(0).getSySignOutIsTransfer());
            //是否需要无码商品
            SPUtils.getInstance().put(DataKey.KEY_IS_CODE_LESS, selectConfigurationList.get(0).getSyNotProductCode());
            //是否需要支付语音提示
            SPUtils.getInstance().put(DataKey.KEY_IS_VOICE, selectConfigurationList.get(0).getSyIsSpeak());
            //是否需要副屏
            SPUtils.getInstance().put(DataKey.KEY_IS_SECOND_SCREEN, selectConfigurationList.get(0).getSySecondScreenStatus());
        }
        startActivity(MainActivity.class);
    }

    private String getPass() {
        return EncryptionUtils.md5(password.get(), userName.get());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
