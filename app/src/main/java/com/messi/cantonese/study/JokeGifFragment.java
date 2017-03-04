package com.messi.cantonese.study;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.avos.avoscloud.okhttp.FormEncodingBuilder;
import com.avos.avoscloud.okhttp.RequestBody;
import com.messi.cantonese.study.adapter.JokeGifAdapter;
import com.messi.cantonese.study.dao.JokeBody;
import com.messi.cantonese.study.dao.JokeContent;
import com.messi.cantonese.study.dao.JokeRoot;
import com.messi.cantonese.study.http.LanguagehelperHttpClient;
import com.messi.cantonese.study.http.UICallback;
import com.messi.cantonese.study.impl.FragmentProgressbarListener;
import com.messi.cantonese.study.util.JsonParser;
import com.messi.cantonese.study.util.LogUtil;
import com.messi.cantonese.study.util.ScreenUtil;
import com.messi.cantonese.study.util.Settings;
import com.messi.cantonese.study.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class JokeGifFragment extends BaseFragment implements OnClickListener{

	private ListView listview;
	private JokeGifAdapter mAdapter;
	private List<JokeContent> mBDJContent;
	private View footerview;
	private JokeBody pagebean;
	private int currentPage = 1;
	
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
		View view = inflater.inflate(R.layout.joke_budejie_fragment, container, false);
		initViews(view);
		initFooterview(inflater);
		requestData();
		return view;
	}
	
	private void initViews(View view){
		mBDJContent = new ArrayList<JokeContent>();
		listview = (ListView) view.findViewById(R.id.listview);
		initSwipeRefresh(view);
		mAdapter = new JokeGifAdapter(getContext(), mBDJContent);
		setListOnScrollListener();
	}
	
	public void setListOnScrollListener(){
		listview.setOnScrollListener(new OnScrollListener() {  
            private int lastItemIndex;//当前ListView中最后一个Item的索引  
            @Override  
            public void onScrollStateChanged(AbsListView view, int scrollState) { 
                if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastItemIndex == mAdapter.getCount() - 1) {  
                	requestData();
                }
            }  
            @Override  
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {  
                lastItemIndex = firstVisibleItem + visibleItemCount - 2;  
            }  
        });
	}

	private void initFooterview(LayoutInflater inflater){
		footerview = inflater.inflate(R.layout.footerview, null, false);
		listview.addFooterView(footerview);
		hideFooterview();
		listview.setAdapter(mAdapter);
	}
	
	private void showFooterview(){
		footerview.setVisibility(View.VISIBLE);  
		footerview.setPadding(0, ScreenUtil.dip2px(getContext(), 15), 0, ScreenUtil.dip2px(getContext(), 15));
	}
	
	private void hideFooterview(){
		footerview.setVisibility(View.GONE);
		footerview.setPadding(0, -footerview.getHeight(), 0, 0);  
	}
	
	@Override
	public void onSwipeRefreshLayoutRefresh() {
		currentPage = 1;
		requestData();
	}
	
	private void requestData(){
		showProgressbar();
		RequestBody formBody = new FormEncodingBuilder()
				.add("showapi_appid", Settings.showapi_appid)
				.add("showapi_sign", Settings.showapi_secret)
				.add("showapi_timestamp", String.valueOf(System.currentTimeMillis()))
				.add("showapi_res_gzip", "1")
				.add("maxResult", "20")
				.add("page", String.valueOf(currentPage))
				.build();
		LogUtil.DefalutLog("currentPage---"+currentPage);
		LanguagehelperHttpClient.post(Settings.JokeGifUrl, formBody, new UICallback(getActivity()){
			@Override
			public void onResponsed(String responseString){
				try {
					LogUtil.DefalutLog("responseString---"+responseString);
					if(JsonParser.isJson(responseString)){
						JokeRoot mRoot = JSON.parseObject(responseString, JokeRoot.class);
						if(mRoot != null && mRoot.getShowapi_res_code() == 0 && mRoot.getShowapi_res_body() != null){
							pagebean = mRoot.getShowapi_res_body();
							if(pagebean.getCurrentPage() == 1){
								mBDJContent.clear();
							}
							if(pagebean.getContentlist() != null){
								mBDJContent.addAll(pagebean.getContentlist());
							}
							currentPage = pagebean.getCurrentPage();
							currentPage++;
							showFooterview();
							mAdapter.notifyDataSetChanged();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFailured() {
				ToastUtil.diaplayMesShort(getContext(),getActivity().getResources().getString(R.string.network_error));
			}
			@Override
			public void onFinished() {
				onSwipeRefreshLayoutFinish();
				hideProgressbar();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		
	}
	
	
	
}
