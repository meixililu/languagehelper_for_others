package com.messi.cantonese.study.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVObject;
import com.karumi.headerrecyclerview.HeaderRecyclerViewAdapter;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.impl.SpokenEnglishPlayListener;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenEndlishCategorySecondaryAdapter extends HeaderRecyclerViewAdapter<RecyclerView.ViewHolder, Object, AVObject, Object> {

    private List<AVObject> avObjects;
    private SpokenEnglishPlayListener mListener;

    public RcSpokenEndlishCategorySecondaryAdapter(List<AVObject> avObjects, SpokenEnglishPlayListener mListener){
        this.avObjects = avObjects;
        this.mListener = mListener;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View characterView = inflater.inflate(R.layout.evaluation_list_item, parent, false);
        return new RcSpokenEnglishCategorySecondaryListItemViewHolder(characterView,avObjects,mListener);
    }

    @Override
    protected void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        AVObject mAVObject = getItem(position);
        RcSpokenEnglishCategorySecondaryListItemViewHolder itemViewHolder = (RcSpokenEnglishCategorySecondaryListItemViewHolder)holder;
        itemViewHolder.render(mAVObject);
    }

    @Override
    protected RecyclerView.ViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = getLayoutInflater(parent);
        View footerView = inflater.inflate(R.layout.footerview, parent, false);
        return new RcLmFooterViewHolder(footerView);
    }

    @Override
    protected void onBindFooterViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindFooterViewHolder(holder, position);
    }

    private LayoutInflater getLayoutInflater(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext());
    }

}
