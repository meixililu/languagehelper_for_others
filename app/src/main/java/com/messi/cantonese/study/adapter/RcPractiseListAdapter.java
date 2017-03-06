package com.messi.cantonese.study.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.bean.UserSpeakBean;
import com.messi.cantonese.study.impl.PractisePlayUserPcmListener;

/**
 * Created by luli on 10/23/16.
 */

public class RcPractiseListAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, UserSpeakBean, Object> {

    private PractisePlayUserPcmListener mPractisePlayUserPcmListener;

    public RcPractiseListAdapter(PractisePlayUserPcmListener mPractisePlayUserPcmListener){
        this.mPractisePlayUserPcmListener = mPractisePlayUserPcmListener;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.practice_activity_lv_item, parent, false);
        return new RcPracticeListItemViewHolder(characterView,mPractisePlayUserPcmListener);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        UserSpeakBean mAVObject = getItem(position);
        RcPracticeListItemViewHolder itemViewHolder = (RcPracticeListItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
