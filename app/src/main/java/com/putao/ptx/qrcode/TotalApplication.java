package com.putao.ptx.qrcode;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.jusenr.toolslibrary.AndroidTools;
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
        MobclickAgent.setDebugMode(BuildConfig.INNER_TEST);
        MobclickAgent.setCatchUncaughtExceptions(true);
        MobclickAgent.openActivityDurationTrack(false);
//        }
    }
}
