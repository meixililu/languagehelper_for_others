package com.messi.cantonese.study;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.messi.cantonese.study.util.SDCardUtil;
import com.messi.cantonese.study.util.ViewUtil;

import java.io.File;
import java.io.IOException;

public class QRCodeShareActivity extends BaseActivity implements OnClickListener {

    private FrameLayout share_btn_cover;
    private LinearLayout share_parent_layout;
    private TextView wechat_long_click;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.qrcode_share_layout);
            init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.invite_friends_qrcode));
        share_parent_layout = (LinearLayout) findViewById(R.id.share_parent_layout);
        wechat_long_click = (TextView) findViewById(R.id.wechat_long_click);
        share_btn_cover = (FrameLayout) findViewById(R.id.share_btn_cover);
        share_btn_cover.setOnClickListener(this);
    }

    private void shareWithImg() throws IOException {
        wechat_long_click.setVisibility(View.VISIBLE);
        Bitmap bitmap = ViewUtil.getBitmapFromViewSmall(share_parent_layout);
        if (bitmap != null) {
            String imgPath = SDCardUtil.saveBitmap(this, bitmap, "qrcode_img.png");
            wechat_long_click.setVisibility(View.GONE);
            share_parent_layout.requestLayout();
            File file = new File(imgPath);
            if (file != null && file.exists() && file.isFile()) {
                Uri uri = Uri.fromFile(file);
                if (uri != null) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("image/png");
                        intent.putExtra(Intent.EXTRA_STREAM, uri);
                        intent.putExtra(Intent.EXTRA_SUBJECT, this.getResources().getString(R.string.share));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        this.startActivity(Intent.createChooser(intent, this.getResources().getString(R.string.share)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.share_btn_cover:
                try {
                    shareWithImg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AVAnalytics.onEvent(QRCodeShareActivity.this, "qrcode_pg_sharebtn");
                break;
            default:
                break;
        }
    }

}
