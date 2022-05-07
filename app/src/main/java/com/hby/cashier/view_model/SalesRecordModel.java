package com.hby.cashier.view_model;

import android.app.Application;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.hby.cashier.utils.LogUtils;
import com.hyy.mvvm.base.BaseViewModel;
import com.hyy.mvvm.base.SingleLiveEvent;
import com.hyy.mvvm.binding.BindingAction;
import com.hyy.mvvm.binding.BindingCommand;
import com.hyy.mvvm.data.DemoRepository;
import com.hyy.mvvm.utils.ToastUtils;


public class SalesRecordModel extends BaseViewModel<DemoRepository> {

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {
        //页面关闭监听
        public SingleLiveEvent activityFinishEvent = new SingleLiveEvent<>();

        //左侧radioGroup监听
        public SingleLiveEvent<Integer> groupSelectEvent = new SingleLiveEvent<>();
        //左侧开始时间选择监听
        public SingleLiveEvent startTimeEvent = new SingleLiveEvent<>();
        //左侧结束时间选择监听
        public SingleLiveEvent endTimeEvent = new SingleLiveEvent<>();
        //左侧搜索监听
        public SingleLiveEvent searchEvent = new SingleLiveEvent<>();

        //右侧未支付（刷新监听）
        public SingleLiveEvent refOrderDetailsEvent = new SingleLiveEvent<>();
        //右侧继续收银监听
        public SingleLiveEvent keepCashierEvent = new SingleLiveEvent<>();
        //右侧反结算监听
        public SingleLiveEvent returnOrderEvent = new SingleLiveEvent<>();
    }

    public SalesRecordModel(@NonNull Application application) {
        super(application);
    }

    public SalesRecordModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    //页面关闭监听
    public BindingCommand activityFinishClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.activityFinishEvent.call();
        }
    });

    //左侧radioGroup监听
    public void onGroupChangeClick(RadioGroup radioGroup, int checkID) {
        uc.groupSelectEvent.setValue(checkID);
    }

    //左侧开始时间选择监听
    public BindingCommand salesRecordStartTimeClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.startTimeEvent.call();
        }
    });
    //左侧结束时间选择监听
    public BindingCommand salesRecordEndTimeClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.endTimeEvent.call();
        }
    });
    //左侧搜索监听
    public BindingCommand salesRecordSearchClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.searchEvent.call();
        }
    });

    //右侧未支付
    public BindingCommand refOrderDetailsClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.refOrderDetailsEvent.call();
        }
    });
    //右侧继续收银监听
    public BindingCommand keepCashierClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.keepCashierEvent.call();
        }
    });
    //右侧反结算监听
    public BindingCommand returnOrderClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.returnOrderEvent.call();
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
