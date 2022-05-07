package com.hby.cashier.view_model;

import android.app.Application;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;

import com.hyy.mvvm.base.BaseViewModel;
import com.hyy.mvvm.base.SingleLiveEvent;
import com.hyy.mvvm.binding.BindingAction;
import com.hyy.mvvm.binding.BindingCommand;
import com.hyy.mvvm.data.DemoRepository;


public class InitializeModel extends BaseViewModel<DemoRepository> {

    //封装一个界面发生改变的观察者
    public UIChangeObservable uc = new UIChangeObservable();

    public class UIChangeObservable {

        //数据同步监听
        public SingleLiveEvent initializeEvent = new SingleLiveEvent<>();
    }

    public InitializeModel(@NonNull Application application) {
        super(application);
    }

    public InitializeModel(@NonNull Application application, DemoRepository repository) {
        super(application, repository);
    }

    //页面关闭监听
    public BindingCommand initializeClick = new BindingCommand(new BindingAction() {
        @Override
        public void call() {
            uc.initializeEvent.call();
        }
    });

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
