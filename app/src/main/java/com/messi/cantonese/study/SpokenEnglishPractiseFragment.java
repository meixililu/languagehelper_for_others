package com.messi.cantonese.study;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.karumi.headerrecyclerview.HeaderSpanSizeLookup;
import com.messi.cantonese.study.adapter.RcSpokenEndlishPracticeTypeListAdapter;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;
import com.messi.cantonese.study.util.ADUtil;
import com.messi.cantonese.study.util.AVOUtil;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.ToastUtil;
import com.messi.cantonese.study.util.XFYSAD;
import com.messi.cantonese.study.views.DividerGridItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class SpokenEnglishPractiseFragment extends BaseFragment {

    private static final int NUMBER_OF_COLUMNS = 2;
    private RecyclerView category_lv;
    private RcSpokenEndlishPracticeTypeListAdapter mAdapter;
    private List<AVObject> avObjects;
    private XFYSAD mXFYSAD;
    private int skip = 0;
    private boolean loading;
    private boolean hasMore = true;
    private GridLayoutManager layoutManager;


    public static SpokenEnglishPractiseFragment getInstance() {
        SpokenEnglishPractiseFragment fragment = new SpokenEnglishPractiseFragment();
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mProgressbarListener = (FragmentProgressbarListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentProgressbarListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        LogUtil.DefalutLog("SpokenEnglishPractiseFragment-onCreateView");
        View view = inflater.inflate(R.layout.spoken_english_practice_fragment, container, false);
        initSwipeRefresh(view);
        initViews(view);
        return view;
    }

    private void initViews(View view) {
        avObjects = new ArrayList<AVObject>();
        category_lv = (RecyclerView) view.findViewById(R.id.studycategory_lv);
        mXFYSAD = new XFYSAD(getActivity(), ADUtil.SecondaryPage);
        mAdapter = new RcSpokenEndlishPracticeTypeListAdapter(mXFYSAD);
        mAdapter.setItems(avObjects);
        mAdapter.setHeader(new Object());
        mAdapter.setFooter(new Object());
        hideFooterview();
        layoutManager = new GridLayoutManager(getContext(), NUMBER_OF_COLUMNS);
        HeaderSpanSizeLookup headerSpanSizeLookup = new HeaderSpanSizeLookup(mAdapter, layoutManager);
        layoutManager.setSpanSizeLookup(headerSpanSizeLookup);
        category_lv.setLayoutManager(layoutManager);
        category_lv.addItemDecoration(new DividerGridItemDecoration(1));
        category_lv.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener(){
        category_lv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = layoutManager.getChildCount();
                int total = layoutManager.getItemCount();
                int firstVisibleItem = layoutManager.findFirstCompletelyVisibleItemPosition();
                LogUtil.DefalutLog("visible:"+visible+"---total:"+total+"---firstVisibleItem:"+firstVisibleItem);
                if(!loading && hasMore){
                    if ((visible + firstVisibleItem) >= total){
                        if(isHasLoadData){
                            new QueryTask().execute();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        LogUtil.DefalutLog("SpokenEnglishPractiseFragment-loadDataOnStart");
        skip = 0;
        new QueryTask().execute();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        super.onSwipeRefreshLayoutRefresh();
        hideFooterview();
        skip = 0;
        new QueryTask().execute();
    }

    private class QueryTask extends AsyncTask<Void, Void,  List<AVObject>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = true;
            showProgressbar();
        }

        @Override
        protected List<AVObject> doInBackground(Void... params) {
            AVQuery<AVObject> query = new AVQuery<AVObject>(AVOUtil.EvaluationCategory.EvaluationCategory);
            query.whereEqualTo(AVOUtil.EvaluationCategory.ECIsValid, "1");
            query.orderByDescending(AVOUtil.EvaluationCategory.ECOrder);
            query.skip(skip);
            query.limit(30);
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
                    ToastUtil.diaplayMesShort(getContext(), "没有了！");
                    hasMore = false;
                    hideFooterview();
                }else if(avObject.size() > 0){
                    if (skip == 0) {
                        avObjects.clear();
                    }
                    avObjects.addAll(avObject);
                    if(avObject.size() == 30){
                        skip += 30;
                        showFooterview();
                    }else if(avObject.size() < 30){
                        hasMore = false;
                        hideFooterview();
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        }

    }

    private void hideFooterview(){
        mAdapter.hideFooter();
    }

    private void showFooterview(){
        mAdapter.showFooter();
    }
}
