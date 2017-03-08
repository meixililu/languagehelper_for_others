package com.messi.cantonese.study;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYFullScreenAd;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.http.OkHttpUrlLoader;
import com.messi.cantonese.study.util.ADUtil;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ShortCut;
import com.messi.cantonese.study.wxapi.WXEntryActivity;

import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoadingActivity extends AppCompatActivity implements OnClickListener {

    @BindView(R.id.ad_source)
    TextView ad_source;
    @BindView(R.id.forward_img)
    ImageView forward_img;
    @BindView(R.id.ad_img)
    SimpleDraweeView ad_img;
    private SharedPreferences mSharedPreferences;
    private IFLYFullScreenAd fullScreenAd;
    private Handler mHandler;
    private boolean isAdExposure;
    private boolean isAdClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        try {
            TransparentStatusbar();
            setContentView(R.layout.loading_activity);
            ButterKnife.bind(this);
            init();
            lazyInit();
        } catch (Exception e) {
            onError();
            e.printStackTrace();
        }
    }

    private void lazyInit() {
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                addToShowAdTimes();
                ShortCut.addShortcut(LoadingActivity.this, mSharedPreferences);
                Glide.get(LoadingActivity.this).register(GlideUrl.class, InputStream.class,
                        new OkHttpUrlLoader.Factory(LanguagehelperHttpClient.initClient(LoadingActivity.this)));
            }
        });
    }

    private void TransparentStatusbar() {
        if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
            //透明状态栏 透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private void init() {
        mSharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        mHandler = new Handler();
        forward_img = (ImageView) findViewById(R.id.forward_img);
        forward_img.setOnClickListener(this);
        if (ADUtil.isShowAd(this)) {
            IFLYNativeAd nativeAd = new IFLYNativeAd(this,ADUtil.KaiPingYSAD, mListener);
            nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
            nativeAd.loadAd(1);
        } else {
            onError();
        }
        startTask();
    }

    IFLYNativeListener mListener = new IFLYNativeListener() {
        @Override
        public void onAdFailed(AdError error) { // 广告请求失败
            onError();
        }
        @Override
        public void onADLoaded(List<NativeADDataRef> lst) { // 广告请求成功
            onAdReceive();
            setADData(lst);
        }
        @Override
        public void onCancel() { // 下载类广告，下载提示框取消
            toNextPage();
        }
        @Override
        public void onConfirm() { // 下载类广告，下载提示框确认
            toNextPage();
        }
    };

    private void setADData(List<NativeADDataRef> lst){
        if(lst != null && lst.size() > 0){
            final NativeADDataRef mNativeADDataRef = lst.get(0);
            if(mNativeADDataRef != null){
                ad_img.setImageURI(mNativeADDataRef.getImage());
                mNativeADDataRef.onExposured(ad_img);
                ad_img.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickAd();
                        mNativeADDataRef.onClicked(view);
                    }
                });
                ad_source.setText(mNativeADDataRef.getAdSourceMark()+"广告");
            }
        }
    }

    private void onAdReceive(){
        ad_source.setVisibility(View.VISIBLE);
        isAdExposure = true;
        if(mHandler != null){
            mHandler.postDelayed(mRunnableFinal, 4000);
        }
    }

    private void onClickAd() {
        isAdClicked = true;
        cancleRunable();
        forward_img.setVisibility(View.VISIBLE);
        AVAnalytics.onEvent(LoadingActivity.this, "ad_click_kaiping");
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mHandler.removeCallbacks(m3Runnable);
            mHandler.removeCallbacks(mRunnableFinal);
            toNextPage();
        }
    };

    private Runnable mRunnableFinal = new Runnable() {
        @Override
        public void run() {
            LogUtil.DefalutLog("LoadingActivity---mRunnableFinal");
            toNextPage();
        }
    };

    private Runnable m3Runnable = new Runnable() {
        @Override
        public void run() {
            LogUtil.DefalutLog("LoadingActivity---m3Runnable---isAdExposure:" + isAdExposure);
            if (!isAdExposure) {
                mHandler.removeCallbacks(mRunnableFinal);
                toNextPage();
            }
        }
    };

    private void onError() {
        mHandler.postDelayed(mRunnable, 800);
    }

    private void startTask() {
        mHandler.postDelayed(m3Runnable, 3500);
    }

    @OnClick({R.id.forward_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forward_img:
                toNextPage();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtil.DefalutLog("LoadingActivity---onActivityResult");
    }

    private void addToShowAdTimes() {
        int IsCanShowAD = mSharedPreferences.getInt(KeyUtil.IsCanShowAD_Loading, 0);
        IsCanShowAD++;
        Settings.saveSharedPreferences(mSharedPreferences, KeyUtil.IsCanShowAD_Loading, IsCanShowAD);
    }

    private void toNextPage() {
        try {
            Intent intent = new Intent(LoadingActivity.this, WXEntryActivity.class);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancleRunable() {
        if (m3Runnable != null) {
            mHandler.removeCallbacks(mRunnableFinal);
            mHandler.removeCallbacks(m3Runnable);
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isAdClicked) {
            toNextPage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.DefalutLog("LoadingActivity---onDestroy---destroyAd");
    }
}
