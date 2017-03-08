package com.messi.cantonese.study;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.messi.cantonese.study.adapter.RcSpokenEndlishCategoryAdapter;
import com.messi.cantonese.study.util.AVOUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SpokenEndlishCategoryActivity extends BaseActivity {

    private RecyclerView category_lv;
    private RcSpokenEndlishCategoryAdapter mAdapter;
    private List<AVObject> avObjects;
    private int skip = 0;
    private String code;
    private boolean loading;
    private boolean hasMore = true;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spoken_englis_category_activity);
        initSwipeRefresh();
        initViews();
        new QueryTask().execute();
    }

    private void initViews() {
        code = getIntent().getStringExtra(AVOUtil.EvaluationDetail.ECCode);
        LogUtil.DefalutLog("code:"+code);
        avObjects = new ArrayList<AVObject>();
        category_lv = (RecyclerView) findViewById(R.id.listview);
        mAdapter = new RcSpokenEndlishCategoryAdapter(avObjects);
        mAdapter.setItems(avObjects);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(this);
        category_lv.setLayoutManager(mLinearLayoutManager);
        category_lv.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (!loading && hasMore) {
                    if ((visible + firstVisibleItem) >= total) {
                        new QueryTask().execute();
                    }
                }
            }
        });
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        skip = 0;
        hideFooterview();
        avObjects.clear();
        mAdapter.notifyDataSetChanged();
        new QueryTask().execute();
    }

    private class QueryTask extends AsyncTask<Void, Void, List<AVObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EvaluationDetail.EvaluationDetail);
            query.whereEqualTo(AVOUtil.EvaluationDetail.EDIsValid, "1");
            query.whereEqualTo(AVOUtil.EvaluationDetail.ECCode, code);
            query.orderByDescending(AVOUtil.EvaluationDetail.EDCode);
            query.skip(skip);
            query.limit(20);
            try {
                return query.find();
            } catch (AVException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<AVObject> avObject) {
            super.onPostExecute(avObject);
            loading = false;
            hideProgressbar();
            onSwipeRefreshLayoutFinish();
            if(avObject != null && avObjects != null && mAdapter != null){
                if(avObject.size() == 0){
                    ToastUtil.diaplayMesShort(SpokenEndlishCategoryActivity.this, "没有了！");
                    hasMore = false;
                    hideFooterview();
                }else if(avObject.size() > 0){
                    if (skip == 0) {
                        avObjects.clear();
                    }
                    avObjects.addAll(avObject);
                    if(avObject.size() == 20){
                        skip += 20;
                        showFooterview();
                    }else if(avObject.size() < 20){
                        hasMore = false;
                        hideFooterview();
                    }
                }
                mAdapter.notifyDataSetChanged();
            } else {
                ToastUtil.diaplayMesShort(SpokenEndlishCategoryActivity.this, "加载失败，下拉可刷新");
            }


        }

    }

}
