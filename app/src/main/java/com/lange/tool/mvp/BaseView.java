package com.lange.tool.mvp;

import android.content.Context;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public interface BaseView {
     Context getContext();

     void showHUD();

     void showHUD(String msg);

     void dismissHUD();
}
