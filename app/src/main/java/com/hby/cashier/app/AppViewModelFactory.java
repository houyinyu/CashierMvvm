package com.hby.cashier.app;

import android.annotation.SuppressLint;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.hby.cashier.view_model.LoginViewModel;
import com.hby.cashier.view_model.MainViewModel;
import com.hby.cashier.view_model.SalesRecordModel;
import com.hyy.mvvm.app.Injection;
import com.hyy.mvvm.base.BaseActivity;
import com.hyy.mvvm.data.DemoRepository;


/**
 * 使用自定义的ViewModelFactory来创建ViewModel
 * Created by goldze on 2019/3/26.
 */
public class AppViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    @SuppressLint("StaticFieldLeak")
    private static volatile AppViewModelFactory INSTANCE;
    private final Application mApplication;
    private final DemoRepository mRepository;

    public static AppViewModelFactory getInstance(Application application, BaseActivity activity) {
        if (INSTANCE == null) {
            synchronized (AppViewModelFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new AppViewModelFactory(application, Injection.provideDemoRepository(activity));
                }
            }
        }
        return INSTANCE;
    }

    @VisibleForTesting
    public static void destroyInstance() {
        INSTANCE = null;
    }

    private AppViewModelFactory(Application application, DemoRepository repository) {
        this.mApplication = application;
        this.mRepository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        //如果用AppViewModelFactory的方法创建需要在这里判断
        if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(mApplication, mRepository);
        }
//        if (modelClass.isAssignableFrom(MainViewModel.class)) {
//            return (T) new MainViewModel(mApplication, mRepository);
//        }
//        if (modelClass.isAssignableFrom(SalesRecordModel.class)) {
//            return (T) new SalesRecordModel(mApplication, mRepository);
//        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
