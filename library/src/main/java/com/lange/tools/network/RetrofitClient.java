package com.lange.tools.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lange.tools.common.Tools;
import com.orhanobut.logger.Logger;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.Retrofit.Builder;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //请求超时时间
    private static final long CONNECT_TIME_OUT = 30;
    private static final long READ_TIME_OUT = 3;
    private static final long WRITE_TIME_OUT = 3;
    //缓冲api接口类
    private static Map<String, Object> apiServiceCacheMap = new HashMap<>();

    /**
     * 创建api接口类
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> t) {
        //判断是否已存在
        T apiCache = (T) apiServiceCacheMap.get(t.getName());
        if (apiCache != null) {
            return apiCache;
        }
        //基础接口地址
        String base_url = getFieldValueByFieldName("BASE_URL", t);
        if (Tools.string().isEmpty(base_url)) {
            Logger.e("api接口类中必须含有BASE_URL成员属性");
            return null;
        }
        //接口请求日志
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);
        Retrofit retrofit = new Builder()
                .baseUrl(base_url)
                .client(new OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .connectTimeout(CONNECT_TIME_OUT, TimeUnit.SECONDS)
                        .readTimeout(READ_TIME_OUT, TimeUnit.MINUTES)
                        .writeTimeout(WRITE_TIME_OUT, TimeUnit.MINUTES)
                        .build())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(buildGson()))
                .build();
        T api = retrofit.create(t);
        //缓存api
        apiServiceCacheMap.put(t.getName(), api);
        return api;
    }

    private static Gson buildGson() {
        return new GsonBuilder().serializeNulls().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
    }

    public static <T> ObservableTransformer<T, T> compose() {
        return new ObservableTransformer<T, T>() {
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param cls
     * @return
     */
    private static String getFieldValueByFieldName(String fieldName, Class cls) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            field.setAccessible(true);
            return (String) field.get(cls);
        } catch (Exception e) {
            return null;
        }
    }

}
