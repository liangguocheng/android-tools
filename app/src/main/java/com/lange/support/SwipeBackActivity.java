package com.lange.support;

import com.lange.support.base.BaseActivity;
import com.lange.support.utils.LogUtils;

/**
 * {描述}
 *
 * @author GUOCHENG LIANG
 * @version 1.0
 * @date 2019/1/26
 */
public class SwipeBackActivity extends BaseActivity {
    @Override
    protected int getLayout() {
        return R.layout.activity_swipeback;
    }

    @Override
    protected void init() {
        super.init();
        LogUtils.d("init");
    }

    @Override
    protected void initView() {
        LogUtils.d("initView");

    }

    @Override
    protected void loadData() {
        LogUtils.d("loadData");

    }
}
