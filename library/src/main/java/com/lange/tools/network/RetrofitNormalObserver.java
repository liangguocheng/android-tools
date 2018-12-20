package com.lange.tools.network;

import android.net.ParseException;

import com.google.gson.JsonParseException;
import com.lange.tools.R;
import com.lange.tools.util.LogUtils;
import com.lange.tools.util.ToastUtils;
import org.json.JSONException;

import java.net.ConnectException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 针对业务需求，可重写success方法进一步封装返回业务结果的
 * Created by liangguocheng on 2018/12/19.
 */

public abstract class RetrofitNormalObserver<T> implements Observer<T> {

    /**
     * 请求响应成功
     *
     * @param responseBody
     */
    protected abstract void success(T responseBody);

    /**
     * 请求响应失败
     */
    protected void fail() {
    }

    private Disposable mDisposable;

    protected void disposable() {
        if (mDisposable != null && mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {
        mDisposable = d;
    }

    @Override
    public void onNext(T t) {
        success(t);
    }

    @Override
    public void onError(Throwable e) {
        LogUtils.e(e.getLocalizedMessage());
        Throwable throwable = e;
        //获取最根源的异常
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        if (e instanceof ConnectException) {
            //均视为网络错误
            ToastUtils.showShort(R.string.network_http_exception);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            //均视为解析错误
            ToastUtils.showShort(R.string.network_json_parseexception);
        } else {
            //服务器异常
            ToastUtils.showShort(R.string.network_server_exception);
        }
        fail();
    }

    @Override
    public void onComplete() {

    }
}
