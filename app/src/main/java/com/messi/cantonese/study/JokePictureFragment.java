package com.messi.cantonese.study;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.iflytek.voiceads.AdError;
import com.iflytek.voiceads.AdKeys;
import com.iflytek.voiceads.IFLYNativeAd;
import com.iflytek.voiceads.IFLYNativeListener;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.cantonese.study.adapter.RcJokePictureListAdapter;
import com.messi.cantonese.study.dao.JokeBody;
import com.messi.cantonese.study.dao.JokeContent;
import com.messi.cantonese.study.dao.JokeRoot;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.http.UICallback;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;
import com.messi.cantonese.study.util.ADUtil;
import com.messi.cantonese.study.util.JsonParser;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.NumberUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class JokePictureFragment extends BaseFragment implements OnClickListener {

    private RecyclerView listview;
    private RcJokePictureListAdapter mAdapter;
    private List<JokeContent> mBDJContent;
    private View footerview;
    private JokeBody pagebean;
    private int currentPage = 1;
    private boolean loading;
    private boolean hasMore = true;
    private IFLYNativeAd nativeAd;
    private JokeContent mADObject;
    private LinearLayoutManager mLinearLayoutManager;

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
        View view = inflater.inflate(R.layout.joke_picture_fragment, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void loadDataOnStart() {
        super.loadDataOnStart();
        loadAD();
        requestData();
    }

    private void initViews(View view) {
        mBDJContent = new ArrayList<JokeContent>();
        listview = (RecyclerView) view.findViewById(R.id.listview);
        initSwipeRefresh(view);
        mAdapter = new RcJokePictureListAdapter();
        mAdapter.setItems(mBDJContent);
        mAdapter.setFooter(new Object());
        hideFooterview();
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        listview.setLayoutManager(mLinearLayoutManager);
        listview.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getContext())
                        .colorResId(R.color.text_tint)
                        .sizeResId(R.dimen.list_divider_size)
                        .marginResId(R.dimen.padding_margin, R.dimen.padding_margin)
                        .build());
        listview.setAdapter(mAdapter);
        setListOnScrollListener();
    }

    public void setListOnScrollListener() {
        listview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visible  = mLinearLayoutManager.getChildCount();
                int total = mLinearLayoutManager.getItemCount();
                int firstVisibleItem = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if(!loading && hasMore){
                    if ((visible + firstVisibleItem) >= total){
                        loadAD();
                        requestData();
                    }
                }
                isADInList(recyclerView,firstVisibleItem,visible);
            }
        });
    }

    private void isADInList(RecyclerView view, int first, int vCount){
        try{
            if(mBDJContent.size() > 2){
                for(int i=first;i< (first+vCount);i++){
                    if(i < mBDJContent.size() && i > 0){
                        JokeContent mAVObject = mBDJContent.get(i);
                        if(mAVObject != null && mAVObject.getmNativeADDataRef() != null){
                            if(!mAVObject.isHasShowAD()){
                                NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
                                mNativeADDataRef.onExposured(view.getChildAt(i%vCount));
                                mAVObject.setHasShowAD(true);
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void showFooterview() {
        mAdapter.showFooter();
    }

    private void hideFooterview() {
        mAdapter.hideFooter();
    }

    @Override
    public void onSwipeRefreshLayoutRefresh() {
        currentPage = 1;
        loadAD();
        requestData();
    }

    private void requestData() {
        loading = true;
        showProgressbar();
        final RequestBody formBody = new FormEncodingBuilder()
                .add("showapi_appid", Settings.showapi_appid)
                .add("showapi_sign", Settings.showapi_secret)
                .add("showapi_timestamp", String.valueOf(System.currentTimeMillis()))
                .add("showapi_res_gzip", "1")
                .add("time", "2015-01-10")
                .add("page", String.valueOf(currentPage))
                .build();
        LogUtil.DefalutLog("currentPage---" + currentPage);
        LanguagehelperHttpClient.post(Settings.JokePictureUrl, formBody, new UICallback(getActivity()) {
            @Override
            public void onResponsed(String responseString) {
                try {
                    LogUtil.DefalutLog("onResponsed");
                    loading = false;
                    if (JsonParser.isJson(responseString)) {
                        JokeRoot mRoot = JSON.parseObject(responseString, JokeRoot.class);
                        if (mRoot != null && mRoot.getShowapi_res_code() == 0 && mRoot.getShowapi_res_body() != null) {
                            pagebean = mRoot.getShowapi_res_body();
                            if (pagebean.getCurrentPage() == 1) {
                                mBDJContent.clear();
                            }
                            if (pagebean.getContentlist() != null) {
                                mBDJContent.addAll(pagebean.getContentlist());
                            }
                            currentPage = pagebean.getCurrentPage();
                            currentPage++;
                            showFooterview();
                            if(addAD()){
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailured() {
                ToastUtil.diaplayMesShort(getContext(), getActivity().getResources().getString(R.string.network_error));
            }

            @Override
            public void onFinished() {
                onSwipeRefreshLayoutFinish();
                hideProgressbar();
            }
        });
    }

    private void loadAD(){
        nativeAd = new IFLYNativeAd(getContext(), ADUtil.SecondaryPage, new IFLYNativeListener() {
            @Override
            public void onConfirm() {
            }
            @Override
            public void onCancel() {
            }
            @Override
            public void onAdFailed(AdError arg0) {
                LogUtil.DefalutLog("onAdFailed---"+arg0.getErrorCode()+"---"+arg0.getErrorDescription());
            }
            @Override
            public void onADLoaded(List<NativeADDataRef> adList) {
                LogUtil.DefalutLog("onADLoaded---");
                if(adList != null && adList.size() > 0){
                    NativeADDataRef nad = adList.get(0);
                    mADObject = new JokeContent();
                    mADObject.setmNativeADDataRef(nad);
                    if(!loading){
                        addAD();
                    }
                }
            }
        });
        nativeAd.setParameter(AdKeys.DOWNLOAD_ALERT, "true");
        nativeAd.loadAd(1);
    }

    private boolean addAD(){
        if(mADObject != null && mBDJContent != null && mBDJContent.size() > 0){
            int index = mBDJContent.size() - 20 + NumberUtil.randomNumberRange(2, 4);
            if(index < 0){
                index = 0;
            }
            LogUtil.DefalutLog("Index:"+index);
            mBDJContent.add(index,mADObject);
            mAdapter.notifyDataSetChanged();
            mADObject = null;
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onClick(View v) {

    }


}
