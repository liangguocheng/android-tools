package com.lange.support.http;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;


import com.lange.support.http.cookie.QFCookieJar;
import com.lange.support.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author JUNLONG CHAN
 * @version 1.0
 */
public abstract class ClientHelper {

    private static ClientHelper sClientHelper;

    private OkHttpClient mOkHttpClient;

    private static Context mContext;

    private static boolean isOpenLog = false;

    private static String sLogTagName;

    private static int mRequestTimeOut = 15000;

    private static String mBaseUrl = "";

    private static String authorization = "";

    private static void init(Builder builder) {
        mContext = builder.context;
        isOpenLog = builder.isOpenLog;
        sLogTagName = builder.logTagName;
        mRequestTimeOut = builder.requestTimeOut;
        mBaseUrl = builder.baseUrl;
        authorization = builder.authorization;
    }

    /**
     * 官方提倡做成单例
     *
     * @return
     */
    public static void init() {
        if (sClientHelper == null) {
            sClientHelper = new ClientHelper() {
                @Override
                public void initService(Retrofit retrofit) {

                }
            };
        }
    }

    public static void reset() {
        sClientHelper = new ClientHelper() {
            @Override
            public void initService(Retrofit retrofit) {

            }
        };
    }

    public static class Builder {
        private Context context;
        private boolean isOpenLog = false;
        private String logTagName = "HTTP_REQUEST";
        private int requestTimeOut = 15000;
        private String baseUrl = "";
        private String authorization = "";

        public Builder() {
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder isOpenLog(boolean isOpenLog) {
            this.isOpenLog = isOpenLog;
            return this;
        }

        public Builder setLogTagName(String logTagName) {
            this.logTagName = logTagName;
            return this;
        }

        public Builder setRequestTimeOut(int timeOut) {
            this.requestTimeOut = timeOut;
            return this;
        }

        public Builder setRequestBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder setAuthorization(String authorization) {
            this.authorization = authorization;
            return this;
        }

        public void build() {
            init(this);
        }
    }

    public ClientHelper() {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
        okHttpClientBuilder.connectTimeout(mRequestTimeOut, TimeUnit.MILLISECONDS);
        okHttpClientBuilder.readTimeout(mRequestTimeOut, TimeUnit.MILLISECONDS);
        //Cookie自动管理
        okHttpClientBuilder.cookieJar(new QFCookieJar(mContext));
        //支持https
        okHttpClientBuilder.sslSocketFactory(createSSLSocketFactory());
        okHttpClientBuilder.hostnameVerifier(new TrustAllHostnameVerifier());
        okHttpClientBuilder.addInterceptor(new HeaderInterceptor());

        if (isOpenLog) {
            okHttpClientBuilder.addInterceptor(new LogInterceptor());
        }

        mOkHttpClient = okHttpClientBuilder.build();
        initRetrofit();
    }

    /**
     * 初始化Retrofit
     */
    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(mBaseUrl)//
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//
                .addConverterFactory(GsonConverterFactory.create())//
                .client(mOkHttpClient)//
                .build();
        initService(retrofit);
    }

    /**
     * 日志拦截器
     */
    private class LogInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            StringBuffer logStringBuffer = new StringBuffer();
            logStringBuffer.append(request.tag().toString() + "\n");
            logStringBuffer.append("headers:\n" + request.headers().toString() + "\n");
            if (request.body() instanceof FormBody) {
                FormBody body = (FormBody) request.body();
                JSONObject object = new JSONObject();
                try {
                    for (int i = 0; i < body.size(); i++) {
                        object.put(body.name(i), body.value(i));
                    }
                    logStringBuffer.append("params:\n" + object.toString() + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            Response response = chain.proceed(chain.request());
            okhttp3.MediaType mediaType = response.body().contentType();
            if (mediaType == null) {
                return response;
            }
            if ("application".equals(mediaType.type().toString())) {
                //注意这里调用response.body().string()后,response会被回收,所以不能直接返回response
                String content = response.body().string();
                logStringBuffer.append("response:\n" + content + "\n");
                LogUtils.d(sLogTagName, logStringBuffer.toString());
                return response.newBuilder().body(okhttp3.ResponseBody.create(mediaType, content)).build();
            } else {
                LogUtils.d(sLogTagName, logStringBuffer.toString());
                return response;
            }
        }
    }

    /**
     * 请求头拦截器
     */
    private class HeaderInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
//                    .addHeader("token", QFCookieJar.getToken())//
            if (authorization != null && !authorization.isEmpty()) {
                Request request = chain.request()//
                        .newBuilder()//
                        .addHeader("Authorization", authorization)//
                        .addHeader("X-Requested-With", "XMLHttpRequest")//
                        // 请求来源标识
                        .addHeader("platform", "ANDROID")//
                        .addHeader("deviceId", DeviceHelper.getUniqueId(mContext))//
                        .addHeader("deviceName", DeviceHelper.getDeviceName())//
                        // 请求系统版本
                        .addHeader("OSVersion", android.os.Build.VERSION.RELEASE)//
                        .addHeader("User-Agent", "OkHttp Headers.java")//
                        .addHeader("Content-type", "application/json")//
                        .build();
                return chain.proceed(request);
            } else {
                Request request = chain.request()//
                        .newBuilder()//
                        .addHeader("token", QFCookieJar.getToken())//
                        .addHeader("X-Requested-With", "XMLHttpRequest")//
                        // 请求来源标识
                        .addHeader("platform", "ANDROID")//
                        .addHeader("deviceId", DeviceHelper.getUniqueId(mContext))//
                        .addHeader("deviceName", DeviceHelper.getDeviceName())//
                        // 请求系统版本
                        .addHeader("OSVersion", android.os.Build.VERSION.RELEASE)//
                        .addHeader("User-Agent", "OkHttp Headers.java")//
                        .addHeader("Content-type", "application/json")//
                        .build();
                return chain.proceed(request);
            }
        }
    }

    /**
     * 默认信任所有的证书
     * TODO 最好加上证书认证，主流App都有自己的证书
     *
     * @return
     */
    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {

        SSLSocketFactory sSLSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllManager()}, new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return sSLSocketFactory;
    }

    private static class TrustAllManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public abstract void initService(Retrofit retrofit);

}
