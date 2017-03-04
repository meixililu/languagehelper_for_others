package com.messi.cantonese.study;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.messi.cantonese.study.adapter.ChDicBushouPinyinListAdapter;
import com.messi.cantonese.study.dao.ChDicBSPYDetailResultDao;
import com.messi.cantonese.study.dao.ChDicBushouPinyinDetailDao;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.http.UICallback;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.ScreenUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChDicBushouPinyinListActivity extends BaseActivity {

    @BindView(R.id.studycategory_lv)
    ListView studycategoryLv;
    private View footerview;
    private String type;//bushou,pinyin
    private String url;
    private ChDicBSPYDetailResultDao mRoot;
    private ChDicBushouPinyinListAdapter mAdapter;
    private List<ChDicBushouPinyinDetailDao> mList;
    private int page = 1;
    private String pageszie = "10";
    private String word;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chdic_bushoupinyin_list_activity);
        ButterKnife.bind(this);
        setStatusbarColor(R.color.style6_color1);
        initSwipeRefresh();
        initViews();
        RequestAsyncTask();
    }

    private void initViews() {
        type = getIntent().getStringExtra(KeyUtil.CHDicType);
        word = getIntent().getStringExtra(KeyUtil.CHDicWord);
        if (TextUtils.isEmpty(word)) {
            finish();
        }
        getSupportActionBar().setTitle(word);
        if (type.equals(ChDicBushouPinyinActivity.bushou)) {
            url = Settings.ChDicBushouListUrl;
        } else {
            url = Settings.ChDicPinyinListUrl;
        }
        mList = new ArrayList<ChDicBushouPinyinDetailDao>();
        mAdapter = new ChDicBushouPinyinListAdapter(this, mList, type);
        initFooterview();
        studycategoryLv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    private void initFooterview(){
        footerview = LayoutInflater.from(this).inflate(R.layout.footerview, null, false);
        studycategoryLv.addFooterView(footerview);
        studycategoryLv.setAdapter(mAdapter);
        hideFooterview();
    }

    public void setListOnScrollListener(){
        studycategoryLv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastItemIndex;//当前ListView中最后一个Item的索引
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mAdapter.getCount() - 1) {
                    if (mRoot != null && mRoot.getResult() != null) {
                        if(page <= mRoot.getResult().getTotalpage()){
                            RequestAsyncTask();
                        }else {
                            hideFooterview();
                            ToastUtil.diaplayMesShort(ChDicBushouPinyinListActivity.this,"没有了");
                        }
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                lastItemIndex = firstVisibleItem + visibleItemCount - 2;
            }
        });
    }

    private void showFooterview(){
        footerview.setVisibility(View.VISIBLE);
        footerview.setPadding(0, ScreenUtil.dip2px(this, 15), 0, ScreenUtil.dip2px(this, 15));
    }

    private void hideFooterview(){
        footerview.setVisibility(View.GONE);
        footerview.setPadding(0, - (footerview.getHeight()+80), 0, 0);
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        page = 1;
        hideFooterview();
        mList.clear();
        mAdapter.notifyDataSetChanged();
        RequestAsyncTask();
    }

    private void RequestAsyncTask() {
        showProgressbar();
        RequestBody formBody = new FormEncodingBuilder()
                .add("key", "59ef16d2ca4ee5b590bde3976a8bf45f")
                .add("word", word)
                .add("page", String.valueOf(page))
                .add("pageszie", pageszie)
                .add("isjijie", "1")
                .add("isxiangjie", "1")
                .build();
        LanguagehelperHttpClient.post(url, formBody, new UICallback(ChDicBushouPinyinListActivity.this){
            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(ChDicBushouPinyinListActivity.this, ChDicBushouPinyinListActivity.this.getResources().getString(R.string.network_error));
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
                    mRoot = JSON.parseObject(responseString, ChDicBSPYDetailResultDao.class);
                    if(mRoot != null && mRoot.getError_code() == 0){
                        if(page == 1){
                            mList.clear();
                        }
                        page++;
                        mList.addAll(mRoot.getResult().getList());
                        showFooterview();
                        if(mList.size() < 10){
                            hideFooterview();
                        }
                    }else{
                        ToastUtil.diaplayMesShort(ChDicBushouPinyinListActivity.this, mRoot.getReason());
                    }
                } else {
                    ToastUtil.diaplayMesShort(ChDicBushouPinyinListActivity.this, ChDicBushouPinyinListActivity.this.getResources().getString(
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
