package com.lange.support.mvp;


/**
 * {MVP BasePresenter}
 * @author ${USER}
 * @version 1.0
 */
public interface  BasePresenter <V extends BaseView>{
    void attachView(V view);

    void detachView();
}
