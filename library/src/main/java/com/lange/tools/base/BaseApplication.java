package com.lange.tools.base;

import android.app.Application;
import android.support.annotation.Nullable;

import com.lange.tools.BuildConfig;
import com.lange.tools.common.Tools;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

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
        Tools.init(this);
        Tools.openToolsLog(BuildConfig.DEBUG);
        //初始化日志框架
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .methodOffset(7)
                .tag("Logger")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy) {
            @Override
            public boolean isLoggable(int priority, @Nullable String tag) {
                return BuildConfig.DEBUG;
            }
        });
        Logger.d("~App 启动完成~");
    }

    public static BaseApplication getInstance() {
        return instance;
    }
}
