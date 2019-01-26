package com.lange.support.mvp;


import android.content.Context;

/**
 * {MVP BasePresenterImpl}
 *
 * @author ${USER}
 * @version 1.0
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
