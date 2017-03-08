package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.SpokenEndlishCategoryActivity;
import com.messi.cantonese.study.SpokenEnglishCategorySecondaryActivity;
import com.messi.cantonese.study.util.AVOUtil;
import com.messi.cantonese.study.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenEnglishPractiseTypeListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;

    public RcSpokenEnglishPractiseTypeListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        name.setText( mAVObject.getString(AVOUtil.EvaluationCategory.ECName) );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(mAVObject);
            }
        });
    }

    private void onItemClick(AVObject mAVObject){
        Intent intent = new Intent(context,SpokenEnglishCategorySecondaryActivity.class);
        intent.putExtra(AVOUtil.EvaluationDetail.ECCode, mAVObject.getString(AVOUtil.EvaluationCategory.ECCode));
        context.startActivity(intent);
    }

}
