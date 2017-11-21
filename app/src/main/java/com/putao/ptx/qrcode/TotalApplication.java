package com.putao.ptx.qrcode;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.support.multidex.MultiDex;

import com.jusenr.toolslibrary.AndroidTools;
import com.jusenr.toolslibrary.utils.AppUtils;
import com.umeng.analytics.MobclickAgent;

/**
 * Description:
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/10/24
 * Time       : 11:08
 * Project    ：zxing_qrcode_demo.
 */
public class TotalApplication extends Application {
    public static final String TAG = TotalApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(getApplicationContext());

//        if (AppUtils.isNamedProcess(getApplicationContext(), getPackageName())) {
        //androidtools init
        AndroidTools.init(getApplicationContext(), BuildConfig.LOG_TAG);
        //UMeng init
        ApplicationInfo info = AppUtils.getApplicationInfo(getApplicationContext());
        String umeng_appkey = info.metaData.getString("UMENG_APPKEY");
        String umeng_channel = info.metaData.getString("UMENG_CHANNEL");
        MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(getApplicationContext(), umeng_appkey, umeng_channel);
        MobclickAgent.startWithConfigure(config);
        MobclickAgent.setDebugMode(BuildConfig.INNER_TEST);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.openActivityDurationTrack(false);
//        }
    }
}
