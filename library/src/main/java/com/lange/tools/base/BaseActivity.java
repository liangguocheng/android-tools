package com.lange.tools.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.lange.tools.manager.AppManager;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ProgressDialog progressDialog;
    private Unbinder butterKnifeUnBinder;

    protected abstract @LayoutRes
    int getLayoutRes();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //布局之前
        beforeLayout();
        //设置布局
        setContentView(getLayoutRes());
        //butterknife
        butterKnifeUnBinder = ButterKnife.bind(this);
        //EventBus
        if (enableEventBus()) {
            EventBus.getDefault().register(this);
        }
        //activity栈
        AppManager.getInstance().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getInstance().removeActivity(this);
        if (butterKnifeUnBinder != null) {
            butterKnifeUnBinder.unbind();
        }
        if (enableEventBus()) {
            EventBus.getDefault().register(this);
        }
    }

    /**
     * 是否使用eventBus
     *
     * @return
     */
    protected boolean enableEventBus() {
        return false;
    }


    /**
     * 设置layout之前
     */
    protected void beforeLayout() {
    }

    /**
     * 全屏
     */
    protected void requestFullScreen() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 返回按钮
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
