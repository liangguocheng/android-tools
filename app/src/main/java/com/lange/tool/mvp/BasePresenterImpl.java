package com.lange.tool.mvp;

import android.content.Context;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class BasePresenterImpl<V extends BaseView> implements BasePresenter<V> {
    protected V mView;
    protected Context mContext;

    @Override
    public void attachView(V view) {
        mView = view;
        mContext = view.getContext();
    }

    @Override
    public void detachView() {
        mView = null;
    }
}
