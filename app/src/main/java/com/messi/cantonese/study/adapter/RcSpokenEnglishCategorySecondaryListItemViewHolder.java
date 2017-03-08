package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.bean.UserSpeakBean;
import com.messi.cantonese.study.impl.SpokenEnglishPlayListener;
import com.messi.cantonese.study.util.AVOUtil;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.ScreenUtil;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenEnglishCategorySecondaryListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final LinearLayout item;
    private final TextView name;
    private final TextView des;
    private final TextView user_speak_score;
    private Context context;
    private List<AVObject> avObjects;
    private SpokenEnglishPlayListener mListener;

    public RcSpokenEnglishCategorySecondaryListItemViewHolder(View convertView, List<AVObject> avObjects,
                                                              SpokenEnglishPlayListener mListener) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        item = (LinearLayout) convertView.findViewById(R.id.item);
        name = (TextView) convertView.findViewById(R.id.name);
        des = (TextView) convertView.findViewById(R.id.des);
        user_speak_score = (TextView) convertView.findViewById(R.id.user_speak_score);
        this.avObjects = avObjects;
        this.mListener = mListener;
    }

    public void render(final AVObject mAVObject) {
        String temp = mAVObject.getString(AVOUtil.EvaluationDetail.EDContent);
        UserSpeakBean mBean = (UserSpeakBean)mAVObject.get(KeyUtil.UserSpeakBean);
        if(temp.contains("#")){
            String[] strs = temp.split("#");
            des.setVisibility(View.VISIBLE);
            des.setText( strs[1] );
            if(mBean != null && !TextUtils.isEmpty(mBean.getContent())){
                user_speak_score.setVisibility(View.VISIBLE);
                user_speak_score.setText(mBean.getScore());
                user_speak_score.setTextColor(context.getResources().getColor(mBean.getColor()));
                name.setText(mBean.getContent());
            }else {
                user_speak_score.setText("");
                user_speak_score.setVisibility(View.GONE);
                name.setText( strs[0] );
            }
        }else{
            if(mBean != null && !TextUtils.isEmpty(mBean.getContent())){
                user_speak_score.setVisibility(View.VISIBLE);
                user_speak_score.setText(mBean.getScore());
                user_speak_score.setTextColor(context.getResources().getColor(mBean.getColor()));
                name.setText(mBean.getContent());
            }else {
                user_speak_score.setText("");
                user_speak_score.setVisibility(View.GONE);
                name.setText( temp );
            }
            des.setVisibility(View.GONE);
            user_speak_score.setVisibility(View.GONE);
        }
        if(!mAVObject.get(KeyUtil.PracticeItemIndex).equals("1")){
            name.setTextColor(context.getResources().getColor(R.color.text_black));
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            cover.setPadding(ScreenUtil.dip2px(context,10),ScreenUtil.dip2px(context,12),
                    ScreenUtil.dip2px(context,10),ScreenUtil.dip2px(context,12));
        }else {
            name.setTextColor(context.getResources().getColor(R.color.material_color_light_blue));
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            cover.setPadding(ScreenUtil.dip2px(context,10),ScreenUtil.dip2px(context,30),
                    ScreenUtil.dip2px(context,10),ScreenUtil.dip2px(context,30));
        }

        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(getAdapterPosition());
            }
        });
    }

    public void onItemClick(int position) {
        if(mListener != null){
            mListener.playOrStop(position);
        }
    }

}
