package com.lange.tools.base;

import android.app.Application;

import com.lange.tools.util.LogUtils;
import com.lange.tools.util.Utils;

/**
 * Created by liangguocheng on 2018/12/18.
 */

public class BaseApplication extends Application {

    private static BaseApplication instance;


    @Override
    public void onCreate() {
        super.onCreate();
        //单例模式
        instance = this;
        //工具类
        Utils.init(this);
        //初始化日志框架
        LogUtils.d("app启动完成");
        //奔溃处理 发送异常信息邮件
        CrashHandler.init(this);
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
