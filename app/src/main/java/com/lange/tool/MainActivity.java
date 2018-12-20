package com.lange.tool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lange.tool.events.EventBusBean;
import com.lange.tool.network.ApiService;
import com.lange.tool.test.TestActivity;
import com.lange.tool.web.WebActivity;
import com.lange.tools.base.BaseActivity;
import com.lange.tools.base.BaseWebActivity;
import com.lange.tools.manager.GlideCacheManager;
import com.lange.tools.manager.UpgradeManager;
import com.lange.tools.network.RetrofitClient;
import com.lange.tools.network.RetrofitLoadingObserver;
import com.lange.tools.network.RetrofitNormalObserver;
import com.lange.tools.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.OnClick;
import okhttp3.ResponseBody;

public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean enableEventBus() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.btn_network_without_dialog)
    public void netWorkWithoutDialog() {
        RetrofitClient
                .getInstance(ApiService.class)
                .SendMessage("test")
                .compose(RetrofitClient.<ResponseBody>compose())
                .subscribe(new RetrofitNormalObserver<ResponseBody>() {
                    @Override
                    protected void success(ResponseBody responseBody) {

                    }
                });
    }

    @OnClick(R.id.btn_network_with_dialog)
    public void netWorkWithDialog() {
        RetrofitClient
                .getInstance(ApiService.class)
                .SendMessage("test")
                .compose(RetrofitClient.<ResponseBody>compose())
                .subscribe(new RetrofitLoadingObserver<ResponseBody>(this) {
                    @Override
                    protected void success(ResponseBody responseBody) {

                    }
                });
    }

    @OnClick(R.id.btn_mvp)
    public void mvp() {
        startActivity(new Intent(this, TestActivity.class));
    }

    @OnClick(R.id.btn_eventbus)
    public void eventbus() {
        EventBus.getDefault().post(new EventBusBean<String>(EventBusBean.TAG_TEST, "Hello EventBus!"));
    }

    @Subscribe
    public void eventBus(EventBusBean<String> bean) {
        if (bean.getTag() == EventBusBean.TAG_TEST) {
            ToastUtils.showShort(bean.getModel());
        }
    }

    @OnClick(R.id.btn_upgrade)
    public void upgrade() {
        UpgradeManager.upgrade(this,"1.5","有新版本发布了~",false,"https://www.wandoujia.com/apps/com.android.chrome/binding?source=web_seo_baidu_binded");
    }

    @OnClick(R.id.btn_glide)
    public void glide() {
        new MaterialDialog.Builder(this)
                .title("缓存提示")
                .content("当前图片缓存占用内存："+ GlideCacheManager.getInstance().getCacheSize(this))
                .negativeText("取消")
                .positiveText("清除缓存")
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        GlideCacheManager.getInstance().clearImageAllCache(MainActivity.this);
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_webview)
    public void webview(){
        Intent intent = new Intent(this, WebActivity.class);
        intent.putExtra(BaseWebActivity.URL_TAG,"http://www.jd.com");
        startActivity(intent);
    }
}
