package com.lange.support.base;

import android.app.Application;

import com.lange.support.utils.SupportUtils;
import com.lange.support.utils.ToastUtils;

/**
 * {Application 基类}
 *
 * @author GUOCHENG LIANG
 * @version 1.0
 * @date 2019/1/26
 */
public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        //单例
        mInstance=this;
        //初始化工具类
        SupportUtils.init(this);

    }
}
