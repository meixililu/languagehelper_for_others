package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.messi.cantonese.study.R;
import com.messi.cantonese.study.bean.UserSpeakBean;
import com.messi.cantonese.study.impl.PractisePlayUserPcmListener;


/**
 * Created by luli on 10/23/16.
 */

public class RcPracticeListItemViewHolder extends RecyclerView.ViewHolder {

    private PractisePlayUserPcmListener mPractisePlayUserPcmListener;
    private final FrameLayout cover;
    private final TextView user_speak_content;
    private final TextView user_speak_score;
    private final ImageView voice_play_img;
    private Context context;

    public RcPracticeListItemViewHolder(View convertView,PractisePlayUserPcmListener mPractisePlayUserPcmListener) {
        super(convertView);
        this.context = convertView.getContext();
        this.mPractisePlayUserPcmListener = mPractisePlayUserPcmListener;
        cover = (FrameLayout) convertView.findViewById(R.id.cover);
        user_speak_content = (TextView) convertView.findViewById(R.id.user_speak_content);
        user_speak_score = (TextView) convertView.findViewById(R.id.user_speak_score);
        voice_play_img = (ImageView) convertView.findViewById(R.id.voice_play_img);
    }

    public void render(final UserSpeakBean mBean) {
        if(getLayoutPosition() == 0){
            voice_play_img.setVisibility(View.VISIBLE);
        }else {
            voice_play_img.setVisibility(View.GONE);
        }
        user_speak_content.setText(mBean.getContent());
        user_speak_score.setText(mBean.getScore());
        int color = context.getResources().getColor(mBean.getColor());
        user_speak_score.setTextColor(color);
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getLayoutPosition() == 0){
                    if(mPractisePlayUserPcmListener != null){
                        mPractisePlayUserPcmListener.playOrStop();
                    }
                }
            }
        });
    }

}
