package com.lange.support;

import com.lange.support.base.BaseApplication;
import com.lange.support.http.ClientHelper;

/**
 * {描述}
 *
 * @author GUOCHENG LIANG
 * @version 1.0
 * @date 2019/1/26
 */
public class App extends BaseApplication {


    @Override
    public void onCreate() {
        super.onCreate();
        ClientHelper.Builder builder = new ClientHelper.Builder().setContext(this).isOpenLog(true).setLogTagName("okhttp").setRequestTimeOut(3000).setRequestBaseUrl("http://gank.io/api/");
        builder.build();
    }
}
