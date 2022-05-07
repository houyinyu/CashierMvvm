package com.hby.cashier.utils.rxjava;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.hby.cashier.R;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 功能介绍:  <br/>
 * 调用方式: / <br/>
 * <p/>
 * 作   者: Hyy - 825129541@qq.com <br/>
 * 创建电脑: 82512  <br/>
 * 创建时间: 2019/11/2 11:19 <br/>
 * 最后编辑: 2019/11/2 - Hyy
 *
 * @author HouYinYu
 */
public class RxClick {


    /**
     * Rxjava使用的防抖事件
     *
     * @param seconds
     * @param view
     * @param throttleClick
     */
    public static void proxyOnClickListener(int seconds, final View view, final RxThrottleClick throttleClick) {

        ObservableOnSubscribe<View> subscribe = new ObservableOnSubscribe<View>() {
            @Override
            public void subscribe(final ObservableEmitter<View> emitter) throws Exception {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        emitter.onNext(view);
                    }
                });
            }
        };

        Observer<View> observer = new Observer<View>() {
            Disposable disposable;

            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(View view) {
                throttleClick.onClick(view);
            }

            @Override
            public void onError(Throwable e) {
                disposable.dispose();
            }

            @Override
            public void onComplete() {
                disposable.dispose();
            }
        };

        Observable
                .create(subscribe)
                .throttleFirst(seconds, TimeUnit.SECONDS)
                .subscribe(observer);
    }


    /**
     * 验证码倒计时
     *
     * @param getCodeView
     */
    public static Disposable startTime(Context context, TextView getCodeView, long maxNum, String defaultText,
                                       int defaultTextColor, int defaultBackDrawable, int nextTextColor, int nextBackDrawable) {
        final Disposable[] timeDisposable = new Disposable[1];
        getCodeView.setEnabled(false);
        getCodeView.setText(maxNum + "s重新获取");
        getCodeView.setTextColor(context.getResources().getColor(nextTextColor));
        getCodeView.setBackgroundResource(nextBackDrawable);

        Observable.interval(1, TimeUnit.SECONDS, Schedulers.io()).take(maxNum)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> maxNum - (aLong + 1)).subscribe(new Observer<Long>() {
            @Override
            public void onSubscribe(Disposable d) {
                timeDisposable[0] = d;
            }

            @Override
            public void onNext(Long aLong) {
                getCodeView.setText(aLong + "s重新获取");
            }

            @Override
            public void onError(Throwable e) {
                getCodeView.setEnabled(true);
                getCodeView.setText(defaultText);
                getCodeView.setTextColor(context.getResources().getColor(defaultTextColor));
                getCodeView.setBackgroundResource(defaultBackDrawable);
                if (timeDisposable[0] != null && !timeDisposable[0].isDisposed()) {
                    timeDisposable[0].dispose();
                }
            }

            @Override
            public void onComplete() {
                getCodeView.setEnabled(true);
                getCodeView.setText(defaultText);
                getCodeView.setTextColor(context.getResources().getColor(defaultTextColor));
                getCodeView.setBackgroundResource(defaultBackDrawable);
                if (timeDisposable[0] != null && !timeDisposable[0].isDisposed()) {
                    timeDisposable[0].dispose();
                }
            }
        });

        return timeDisposable[0];

    }
}
