package com.lange.tools.api;

import com.lange.tools.model.PgyUpgradeModel;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by liangguocheng on 2018/12/24.
 */

public interface CommonApi {

    String BASE_URL = "https://www.google.com/";

    /**
     * 蒲公英检查更新
     *
     * @param apiKey
     * @param appKey
     * @return
     */
    @POST
    @FormUrlEncoded
    Observable<PgyUpgradeModel> pgyerCheckUpgrade(@Url String url, @Field("_api_key") String apiKey, @Field("appKey") String appKey);
}
