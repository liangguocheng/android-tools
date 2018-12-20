package com.lange.tool.test;

import android.content.Context;

import com.lange.tool.mvp.BasePresenter;
import com.lange.tool.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TestContract {
    interface View extends BaseView {
        
    }

    interface  Presenter extends BasePresenter<View> {
        
    }
}
