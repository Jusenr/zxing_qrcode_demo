package com.putao.ptx.qrcode.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.widget.TextView;

import com.putao.ptx.qrcode.R;
import com.putao.ptx.qrcode.base.BaseActivity;

public class ScanResultActivity extends BaseActivity {
    public static final String KEY_CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        String content = getIntent().getStringExtra(KEY_CONTENT);

        TextView tv_content = (TextView) findViewById(R.id.tv_content);
        tv_content.setText(content);
        //超链接相关字符识别
        tv_content.setAutoLinkMask(Linkify.ALL);
        tv_content.setMovementMethod(LinkMovementMethod.getInstance());

    }

    public static void luncher(Context context, String content) {
        Intent intent = new Intent(context, ScanResultActivity.class);
        intent.putExtra(KEY_CONTENT, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
