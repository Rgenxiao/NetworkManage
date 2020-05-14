package com.example.networkmanage;

import android.app.Application;

import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.NoHttp;

public class App extends Application {

    public static PersistentCookieJar cookieJar;
    @Override
    public void onCreate() {
        super.onCreate();

       /* InitializationConfig config = InitializationConfig.newBuilder(getApplicationContext())
                // 全局连接服务器超时时间，单位毫秒，默认10s。
                .connectionTimeout(30 * 1000)
                // 全局等待服务器响应超时时间，单位毫秒，默认10s。
                .readTimeout(30 * 1000)
                // 全局重试次数，配置后每个请求失败都会重试x次。
                .retry(2)
                .build();
        NoHttp.initialize(config);*/
    }
}
