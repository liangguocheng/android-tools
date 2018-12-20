package com.lange.tool.network;


import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiService {

    String BASE_URL = "http://13.210.208.154/API/";

    @POST("SendMessage")
    @FormUrlEncoded
    Observable<ResponseBody> SendMessage(@Field("test") String test);

}
