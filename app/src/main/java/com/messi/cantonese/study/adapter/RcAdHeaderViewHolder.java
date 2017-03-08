package com.messi.cantonese.study.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.messi.cantonese.study.util.XFYSAD;

/**
 * Created by luli on 10/23/16.
 */

public class RcAdHeaderViewHolder extends RecyclerView.ViewHolder {

    private XFYSAD mXFYSAD;
    public View headerView;

    public RcAdHeaderViewHolder(View itemView) {
        super(itemView);
        this.headerView = itemView;
    }

    public void setXFYSAD(XFYSAD mXFYSAD) {
        this.mXFYSAD = mXFYSAD;
        mXFYSAD.setParentView(headerView);
    }

    public void start(){
        if(mXFYSAD != null){
            mXFYSAD.showAD();
        }
    }

}
