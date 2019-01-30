package com.lange.support;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import rx.Observable;

/**
 * {描述}
 *
 * @author GUOCHENG LIANG
 * @version 1.0
 * @date 2019/1/30
 */
public interface ApiService {

    @GET("today")
    Observable<ResponseBody> test();
}
