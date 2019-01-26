package com.lange.support;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.lange.support.base.BaseActivity;
import com.lange.support.constant.PermissionConstants;
import com.lange.support.utils.ActivityUtils;
import com.lange.support.utils.LogUtils;
import com.lange.support.utils.PermissionUtils;
import com.lange.support.utils.SnackbarUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        super.init();
        ButterKnife.bind(this);
        registerEventBus();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void loadData() {

    }

    @OnClick(R.id.btn_permission)
    public void permission() {
        PermissionUtils.permission(PermissionConstants.STORAGE, PermissionConstants.CAMERA).callback(new PermissionUtils.FullCallback() {
            @Override
            public void onGranted(List<String> permissionsGranted) {
                LogUtils.d("granted");
            }

            @Override
            public void onDenied(List<String> permissionsDeniedForever, List<String> permissionsDenied) {
                LogUtils.d("not granted");

            }
        }).request();
    }

    @OnClick(R.id.btn_swipeback)
    public void swipeBack() {
        ActivityUtils.startActivity(SwipeBackActivity.class);
    }

    @OnClick(R.id.btn_eventBus)
    public void eventBus() {
        postEvent("ssss");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribe(Object obj) {
        LogUtils.d(obj);
        SnackbarUtils.with(getWindow().getDecorView()).setMessage(String.valueOf(obj)).show();
    }
}
