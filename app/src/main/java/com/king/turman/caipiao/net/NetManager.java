package com.king.turman.caipiao.net;

import com.king.turman.caipiao.CPApplication;
import com.king.turman.caipiao.net.customFactory.StringConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by diaoqf on 2017/12/7.
 */

public class NetManager {

    private static int cacheSize = 10 * 1024 * 1024; // 10 MiB
    private static Retrofit CommonClient = null;
    private static final String CACHE_CONTROL = "Cache-Control";
    private static final Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            int maxAge = 60; // 在线缓存在1分钟内可读取
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader(CACHE_CONTROL)
                    .header(CACHE_CONTROL, "public, max-age=" + maxAge)
                    .build();
        }
    };

    public static Retrofit getClient(){
        if (CommonClient == null) {
            CommonClient = new Retrofit.Builder()
                    .baseUrl(NetContents.BASE_HOST)
                    .client(getHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(StringConverterFactory.create())
                    .build();
        }
        return CommonClient;
    }

    private static OkHttpClient getHttpClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(NetContents.HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(NetContents.HTTP_WRITE_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(NetContents.HTTP_READ_TIMEOUT, TimeUnit.MILLISECONDS);

        builder.addNetworkInterceptor(REWRITE_CACHE_CONTROL_INTERCEPTOR);
        File httpCacheDirectory = new File(CPApplication.getContext().getCacheDir(), "FindPropertyCache");
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        builder.cache(cache);

        OkHttpClient client = builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();
        return client;
    }
}
