package com.messi.cantonese.study.adapter;

import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iflytek.cloud.SpeechSynthesizer;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.dao.record;
import com.messi.cantonese.study.util.ViewUtil;

import java.util.List;

public class RcCollectTranslateListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, record, Object> {

	private List<record> beans;
	private SpeechSynthesizer mSpeechSynthesizer;
	private SharedPreferences mSharedPreferences;
	private RcCollectTranslateLiatItemViewHolder mItemViewHolder;

	public RcCollectTranslateListAdapter(SpeechSynthesizer mSpeechSynthesizer, SharedPreferences mSharedPreferences,
										 List<record> beans){
		this.beans = beans;
		this.mSpeechSynthesizer = mSpeechSynthesizer;
		this.mSharedPreferences = mSharedPreferences;
	}

	@Override
	protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater inflater = getLayoutInflater(parent);
		View characterView = inflater.inflate(R.layout.listview_item_recent_used, parent, false);
		return new RcCollectTranslateLiatItemViewHolder(characterView,beans,mSpeechSynthesizer,mSharedPreferences,this);
	}

	@Override
	protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
		record mAVObject = getItem(position);
		mItemViewHolder = (RcCollectTranslateLiatItemViewHolder)holder;
		mItemViewHolder.render(mAVObject);
	}

	@Override
	protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
		return new RcLmFooterViewHolder(ViewUtil.getListFooterView(parent.getContext()));
	}

	@Override
	protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
		super.onBindFooterViewHolder(holder, position);
	}

	private LayoutInflater getLayoutInflater(ViewGroup parent) {
		return LayoutInflater.from(parent.getContext());
	}

	public void addEntity(int position, record entity) {
		beans.add(position, entity);
		notifyItemInserted(position);
	}

	public void stopPlay(){
		if(mItemViewHolder != null){
			mItemViewHolder.stopPlay();
		}
	}

}
