package com.lange.support.mvp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


/**
 * {MVP BaseView}
 *
 * @author ${USER}
 * @version 1.0
 */
public interface BaseView {
    Context getContext();

    /**
     * 结束自身
     */
    void finish();

    /**
     * 土司提示
     */
    void showToast(String tips);

    /**
     * 土司提示
     */
    void showToast(int tips);

    /**
     * 土司提示
     */
    void showSnackbar(View view, String tips);

    /**
     * 土司提示
     */
    void showSnackbar(View view, int tips);

    /**
     * 显示进度对话框
     */
    void showProgressDialog();

    /**
     * 显示进度对话框
     */
    void dismissProgressDialog();

    /**
     * 跳转页面
     */
    void goToActivity(Class<?> cls);

    /**
     * 跳转页面
     */
    void goToActivity(Intent intent);

    /**
     * 跳转页面，带回调
     */
    void goToActivityForResult(Class<?> cls, int requestCode);

    /**
     * 跳转页面，带回调
     */
    void goToActivityForResult(Intent intent, int requestCode);

}
