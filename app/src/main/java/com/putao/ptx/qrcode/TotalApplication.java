package com.putao.ptx.qrcode;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.uuzuche.lib_zxing.activity.ZXingLibrary;

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

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(getApplicationContext());

        ZXingLibrary.initDisplayOpinion(getApplicationContext());
    }
}
