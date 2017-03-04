package com.messi.cantonese.study;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.messi.cantonese.study.adapter.ChDicBushouPinyinAdapter;
import com.messi.cantonese.study.dao.ChDicBSPYDao;
import com.messi.cantonese.study.dao.ChDicBushouPinyinDao;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.http.UICallback;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChDicBushouPinyinActivity extends BaseActivity {

    @BindView(R.id.studycategory_lv)
    GridView studycategoryLv;
    public static final String bushou = "bushou";
    public static final String pinyin = "pinyin";
    private String type;//bushou,pinyin
    private String url;
    private ChDicBSPYDao mRoot;
    private ChDicBushouPinyinAdapter mAdapter;
    private List<ChDicBushouPinyinDao> mList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chdic_bushoupinyin_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.style6_color1);
        initSwipeRefresh();
        initViews();
        RequestAsyncTask();
    }

    private void initViews() {
        type = getIntent().getStringExtra(KeyUtil.CHDicType);
        if (TextUtils.isEmpty(type)) {
            finish();
        }
        if (type.equals(bushou)) {
            getSupportActionBar().setTitle(getResources().getString(R.string.ChDicBushouList));
            url = Settings.ChDicBushouUrl;
        } else {
            getSupportActionBar().setTitle(getResources().getString(R.string.ChDicPinyinList));
            url = Settings.ChDicPinyinUrl;
        }
        mList = new ArrayList<ChDicBushouPinyinDao>();
        mAdapter = new ChDicBushouPinyinAdapter(this, mList, type);
        studycategoryLv.setAdapter(mAdapter);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        RequestAsyncTask();
    }

    private void RequestAsyncTask() {
        showProgressbar();
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "59ef16d2ca4ee5b590bde3976a8bf45f")
                .build();
        LanguagehelperHttpClient.post(url, formBody, new UICallback(ChDicBushouPinyinActivity.this){
            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(ChDicBushouPinyinActivity.this, ChDicBushouPinyinActivity.this.getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                hideProgressbar();
                onSwipeRefreshLayoutFinish();
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onResponsed(String responseString) {
                if (!TextUtils.isEmpty(responseString)) {
                    LogUtil.DefalutLog("responseString:"+responseString);
                    mRoot = JSON.parseObject(responseString, ChDicBSPYDao.class);
                    if(mRoot != null && mRoot.getError_code() == 0){
                        mList.clear();
                        mList.addAll(mRoot.getResult());
                    }else{
                        ToastUtil.diaplayMesShort(ChDicBushouPinyinActivity.this, mRoot.getReason());
                    }
                } else {
                    ToastUtil.diaplayMesShort(ChDicBushouPinyinActivity.this, ChDicBushouPinyinActivity.this.getResources().getString(
                            R.string.network_error));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
