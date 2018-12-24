package com.lange.tools.manager;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.lange.tools.api.CommonApi;
import com.lange.tools.model.PgyUpgradeModel;
import com.lange.tools.network.RetrofitClient;
import com.lange.tools.network.RetrofitNormalObserver;
import com.lange.tools.util.AppUtils;
import com.vector.update_app.HttpManager;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 封装apk更新下载
 * Created by liangguocheng on 2018/12/20.
 */

public class UpgradeManager {

    /**
     * 判断版本号然后进行更新
     *
     * @param activity
     * @param serverVersionCode
     * @param versionDescript
     * @param isForceUpgrade
     * @param apkUrl
     */
    public static void upgrade(Activity activity,
                               String serverVersionCode,
                               String versionDescript,
                               boolean isForceUpgrade,
                               String apkUrl) {
        new UpdateAppManager
                .Builder()
                //必须设置，当前Activity
                .setActivity(activity)
                .setTargetPath(activity.getExternalCacheDir().getAbsolutePath())
                //必须设置，更新地址(使用百度地址来进行请求)
                .setUpdateUrl("http://www.baidu.com/")
                //必须设置，实现httpManager接口的对象
                .setHttpManager(new UpdateAppHttpUtil())
                .setPost(false)
                .build().checkNewApp(new UpdateCallback() {
            /**
             * 解析json,自定义协议
             *
             * @param json 服务器返回的json
             * @return UpdateAppBean
             */
            @Override
            protected UpdateAppBean parseJson(String json) {
                UpdateAppBean updateAppBean = new UpdateAppBean();
                String localVersionName = AppUtils.getAppVersionName();
                updateAppBean
                        //（必须）是否更新Yes,No
                        .setUpdate((localVersionName.compareTo(serverVersionCode) < 0 ? "Yes" : "No"))
                        //（必须）新版本号，
                        .setNewVersion(serverVersionCode)
                        //（必须）下载地址
                        .setApkFileUrl(apkUrl)
                        //（必须）更新内容
                        .setUpdateLog(versionDescript)
                        //大小，不设置不显示大小，可以不设置
                        //.setTargetSize(jsonObject.optString("target_size"))
                        //是否强制更新，可以不设置
                        .setConstraint(isForceUpgrade);
                return updateAppBean;
            }


            /**
             * 网络请求之前
             */
            @Override
            public void onBefore() {

            }

            /**
             * 网路请求之后
             */
            @Override
            public void onAfter() {

            }

            /**
             * 没有新版本
             */
            @Override
            public void noNewApp(String error) {

            }
        });
    }


    /**
     * 通过蒲公英接口检查更新
     *
     * @param activity
     * @param apiKey
     * @param appKey
     */
    public static void checkPgyerUpdate(Activity activity, String apiKey, String appKey) {
        RetrofitClient.getInstance(CommonApi.class).pgyerCheckUpgrade("https://www.pgyer.com/apiv2/app/check/",apiKey, appKey)
                .compose(RetrofitClient.compose())
                .subscribe(new RetrofitNormalObserver<PgyUpgradeModel>() {

                    @Override
                    protected void success(PgyUpgradeModel responseBody) {
                        if (responseBody.getCode() == 0) {
                            PgyUpgradeModel.Data model = responseBody.getData();
                            upgrade(activity, model.getBuildVersion(), model.getBuildUpdateDescription(),
                                    false, model.getDownloadURL());
                        }
                    }
                });
    }


    static class UpdateAppHttpUtil implements HttpManager {

        /**
         * 异步get
         *
         * @param url      get请求地址
         * @param params   get参数
         * @param callBack 回调
         */
        @Override
        public void asyncGet(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
            OkHttpUtils.get()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Response response, Exception e, int id) {
                            callBack.onError(validateError(e, response));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            callBack.onResponse(response);
                        }
                    });
        }

        /**
         * 异步post
         *
         * @param url      post请求地址
         * @param params   post请求参数
         * @param callBack 回调
         */
        @Override
        public void asyncPost(@NonNull String url, @NonNull Map<String, String> params, @NonNull final Callback callBack) {
            OkHttpUtils.post()
                    .url(url)
                    .params(params)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Response response, Exception e, int id) {
                            callBack.onError(validateError(e, response));
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            callBack.onResponse(response);
                        }
                    });

        }

        /**
         * 下载
         *
         * @param url      下载地址
         * @param path     文件保存路径
         * @param fileName 文件名称
         * @param callback 回调
         */
        @Override
        public void download(@NonNull String url, @NonNull String path, @NonNull String fileName, @NonNull final FileCallback callback) {
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute(new FileCallBack(path, fileName) {
                        @Override
                        public void inProgress(float progress, long total, int id) {
                            callback.onProgress(progress, total);
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e, int id) {
                            callback.onError(validateError(e, response));
                        }

                        @Override
                        public void onResponse(File response, int id) {
                            callback.onResponse(response);

                        }

                        @Override
                        public void onBefore(Request request, int id) {
                            super.onBefore(request, id);
                            callback.onBefore();
                        }
                    });

        }
    }
}
