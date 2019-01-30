package com.lange.support;

import com.lange.support.http.ClientHelper;

import retrofit2.Retrofit;

/**
 * {描述}
 *
 * @author GUOCHENG LIANG
 * @version 1.0
 * @date 2019/1/30
 */
public class HttpHelper extends ClientHelper {

    private static HttpHelper sClientHelper;

    public static HttpHelper getInstance() {
        ClientHelper.init();
        if (sClientHelper == null) {
            sClientHelper = new HttpHelper();
        }
        return sClientHelper;
    }

    public static void reset() {
        ClientHelper.reset();
        sClientHelper = new HttpHelper();
    }

    @Override
    public void initService(Retrofit retrofit) {
        this.mApiService = retrofit.create(ApiService.class);
    }

    private ApiService mApiService;


    public ApiService getApiService() {
        return mApiService;
    }
}
