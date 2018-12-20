package com.lange.tool.test;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lange.tool.R;
import com.lange.tool.mvp.MVPBaseActivity;
import com.lange.tools.util.LogUtils;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TestActivity extends MVPBaseActivity<TestContract.View, TestPresenter> implements TestContract.View {

    @BindView(R.id.iv_test)
    ImageView imageView;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Glide.with(this)
                .load("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1545271817&di=afe175b4ca7005b4bc89462a8f3623cf&src=http://b.hiphotos.baidu.com/zhidao/pic/item/14ce36d3d539b600d3924a1feb50352ac65cb73e.jpg")
                .placeholder(R.mipmap.ic_launcher_round)
                .into(imageView);
    }

    @OnClick(R.id.tv_test)
    public void test(){
        LogUtils.d("test");
    }
}
