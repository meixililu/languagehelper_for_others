package com.messi.cantonese.study;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

import com.avos.avoscloud.AVAnalytics;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ShareUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MoreActivity extends BaseActivity implements OnClickListener {

    @BindView(R.id.costom_share_layout)
    FrameLayout costom_share_layout;
    @BindView(R.id.comments_layout)
    FrameLayout comments_layout;
    @BindView(R.id.help_layout)
    FrameLayout help_layout;
    @BindView(R.id.about_layout)
    FrameLayout about_layout;
    @BindView(R.id.invite_layout)
    FrameLayout invite_layout;
    @BindView(R.id.qrcode_layout)
    FrameLayout qrcode_layout;
    @BindView(R.id.setting_layout)
    FrameLayout setting_layout;
    @BindView(R.id.collected_layout)
    FrameLayout collected_layout;
    private SharedPreferences mSharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_more));
        mSharedPreferences = Settings.getSharedPreferences(this);
        costom_share_layout.setOnClickListener(this);
        comments_layout.setOnClickListener(this);
        help_layout.setOnClickListener(this);
        about_layout.setOnClickListener(this);
        invite_layout.setOnClickListener(this);
        qrcode_layout.setOnClickListener(this);
        setting_layout.setOnClickListener(this);
        collected_layout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_layout:
                toActivity(SettingActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_tosettingpg_btn");
                break;
            case R.id.collected_layout:
                toActivity(CollectedActivity.class, null);
                AVAnalytics.onEvent(this, "index_pg_to_collectedpg");
                break;
            case R.id.costom_share_layout:
                toActivity(ImgShareActivity.class, null);
                break;
            case R.id.comments_layout:
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("market://details?id=com.messi.languagehelper"));
                    MoreActivity.this.startActivity(intent);
                    AVAnalytics.onEvent(MoreActivity.this, "more_pg_tocommendpg_btn");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.help_layout:
                toActivity(HelpActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_tohelppg_btn");
                break;
            case R.id.about_layout:
                toActivity(AboutActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_toaboutpg_btn");
                break;
            case R.id.invite_layout:
                ShareUtil.shareText(MoreActivity.this, MoreActivity.this.getResources().getString(R.string.invite_friends_prompt));
                AVAnalytics.onEvent(this, "more_pg_invite_btn", "邀请小伙伴", 1);
                break;
            case R.id.qrcode_layout:
                toActivity(QRCodeShareActivity.class, null);
                AVAnalytics.onEvent(MoreActivity.this, "more_pg_qrcode_btn");
                break;
            default:
                break;
        }
    }

}
