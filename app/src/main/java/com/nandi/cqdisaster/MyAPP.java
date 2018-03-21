package com.nandi.cqdisaster;


import com.baidu.mapapi.SDKInitializer;
import com.nandi.cqdisaster.examine.database.DaoMaster;
import com.nandi.cqdisaster.examine.database.DaoSession;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.util.concurrent.TimeUnit;

import io.dcloud.application.DCloudApplication;
import okhttp3.OkHttpClient;

/**
 * Created by qingsong on 2018/1/30.
 */

public class MyAPP extends DCloudApplication {
    private static DaoSession daoSession;
    @Override
    public void onCreate() {
        super.onCreate();
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "lenve.db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDb());
        daoSession = daoMaster.newSession();
        SDKInitializer.initialize(getApplicationContext());

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("qs"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }
    public static DaoSession getDaoSession() {
        return daoSession;
    }
}
