package com.putao.ptx.qrcode.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jusenr.qrcode.activity.CaptureFragment;
import com.jusenr.qrcode.util.CodeUtils;
import com.putao.ptx.qrcode.R;
import com.putao.ptx.qrcode.base.BaseActivity;

/**
 * Description: 定制化显示扫描界面
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/10/24
 * Time       : 10:21
 * Project    ：zxing_qrcode_demo.
 */
public class ScanActivity extends BaseActivity {
    /**
     * 选择系统图片Request Code
     */
    public static final int REQUEST_IMAGE = 112;


    private CaptureFragment captureFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        setTitle("扫一扫");

        captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.layout_camera);
        captureFragment.setAnalyzeCallback(analyzeCallback);
//        captureFragment.setPlayBeep(false);
//        captureFragment.setVibrate(false);
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();

        initView();
    }

    public static boolean isOpen = false;

    private void initView() {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear1);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOpen) {
                    CodeUtils.isLightEnable(true);
                    isOpen = true;
                } else {
                    CodeUtils.isLightEnable(false);
                    isOpen = false;
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_photo_album:
                CodeUtils.openAlbum(this, REQUEST_IMAGE);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                try {
                    CodeUtils.analyzeBitmap(this, uri, analyzeCallback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 二维码解析回调函数
     */
    CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
        @Override
        public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
            if (!TextUtils.isEmpty(result)) {
                if (result.contains("child-name")) {
                    Toast.makeText(getApplicationContext(), "太撸了，再来一次！", Toast.LENGTH_SHORT).show();
                    captureFragment.reStartPreview();
                    return;
                }

                ScanResultActivity.luncher(getApplicationContext(), result);

//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//            bundle.putString(CodeUtils.RESULT_STRING, result);
//            resultIntent.putExtras(bundle);
//            ScanActivity.this.setResult(RESULT_OK, resultIntent);

                ScanActivity.this.finish();//离开扫描页面必须finish()
            } else {
                captureFragment.reStartPreview();
            }
        }

        @Override
        public void onAnalyzeFailed() {
            Toast.makeText(getApplicationContext(), "解析二维码失败！", Toast.LENGTH_SHORT).show();
            captureFragment.reStartPreview();

//            Intent resultIntent = new Intent();
//            Bundle bundle = new Bundle();
//            bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
//            bundle.putString(CodeUtils.RESULT_STRING, "");
//            resultIntent.putExtras(bundle);
//            ScanActivity.this.setResult(RESULT_OK, resultIntent);
//            ScanActivity.this.finish();//离开扫描页面必须finish()
        }
    };
}
