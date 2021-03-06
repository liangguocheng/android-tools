package com.lange.support.mvp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lange.support.R;
import com.lange.support.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.ParameterizedType;


/**
 * {MVP View模块}
 *
 * @author ${USER}
 * @version 1.0
 */
public abstract class MVPBaseActivity<V extends BaseView, T extends BasePresenterImpl<V>> extends BaseActivity implements BaseView {
    public T mPresenter;
    private ProgressDialog mDialog;

    @Override
    protected void init() {
        super.init();
        mPresenter = getInstance(this, 1);
        mPresenter.attachView((V) this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.detachView();
    }

    @Override
    public Context getContext() {
        return this;
    }

    public <T> T getInstance(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 吐司
     *
     * @param msg 输出内容
     */
    @Override
    public void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 吐司
     *
     * @param res 输出内容资源id
     */
    @Override
    public void showToast(int res) {
        Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
    }


    /**
     * 吐司
     *
     * @param msg 输出内容
     */
    @Override
    public void showSnackbar(View view, String msg) {
        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        setSnackbarMessageTextColor(snackbar, Color.WHITE);
        snackbar.show();
    }

    /**
     * 吐司
     *
     * @param res 输出内容资源id
     */
    @Override
    public void showSnackbar(View view, int res) {
        Snackbar snackbar = Snackbar.make(view, res, Snackbar.LENGTH_SHORT);
        setSnackbarMessageTextColor(snackbar, Color.WHITE);
        snackbar.show();
    }

    public static void setSnackbarMessageTextColor(Snackbar snackbar, int color) {
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(color);
    }

    /**
     * 发送event
     */
    @Override
    public void postEvent(Object event) {
        EventBus.getDefault().post(event);
    }

    /**
     * 注册EventBus
     */
    public void registerEventBus() {
        // 防止用户切换是重复注册
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 反注册EventBus
     */
    public void unRegisterEventBus() {
        EventBus.getDefault().unregister(this);
    }

    /**
     * Activity跳转
     */
    @Override
    public void goToActivity(Class<?> cls) {
        if (cls != null) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), cls);
            if (mExtras != null) {
                intent.putExtras(mExtras);
                mExtras = null;
            }
            startActivity(intent);
        }
    }

    @Override
    public void goToActivity(Intent intent) {
        startActivity(intent);
    }

    @Override
    public void goToActivityForResult(Class<?> cls, int requestCode) {
        if (cls != null) {
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(), cls);
            if (mExtras != null) {
                intent.putExtras(mExtras);
                mExtras = null;
            }
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void goToActivityForResult(Intent intent, int requestCode) {
        if (mExtras != null) {
            intent.putExtras(mExtras);
            mExtras = null;
        }
        startActivityForResult(intent, requestCode);
    }

    private Bundle mExtras;


    /**
     * 显示加载框
     */
    @Override
    public void showProgressDialog() {
        if (mDialog == null) {
            mDialog = new ProgressDialog(this);
            mDialog.setCancelable(false);
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setMessage(getString(R.string.http_loading));
        }
        if (mDialog != null && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    /**
     * 取消加载框
     */
    @Override
    public void dismissProgressDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }
}
