package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.messi.cantonese.study.PracticeForListActivity;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.util.AVOUtil;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.wxapi.WXEntryActivity;

import java.util.List;

/**
 * Created by luli on 10/23/16.
 */

public class RcSpokenEnglishCategoryListItemViewHolder extends RecyclerView.ViewHolder {

    private final View cover;
    private final TextView name;
    private Context context;
    private List<AVObject> beanList;


    public RcSpokenEnglishCategoryListItemViewHolder(View convertView,List<AVObject> beanList) {
        super(convertView);
        this.context = convertView.getContext();
        this.beanList = beanList;
        cover = (View) convertView.findViewById(R.id.layout_cover);
        name = (TextView) convertView.findViewById(R.id.name);
    }

    public void render(final AVObject mAVObject) {
        String itemNameOld = mAVObject.getString(AVOUtil.EvaluationDetail.EDContent);
        if(itemNameOld.contains("#")){
            itemNameOld = itemNameOld.replace("#","\n");
        }
        name.setText( itemNameOld );
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                onItemClick(getAdapterPosition());
            }
        });
    }

    private void onItemClick(int index){
        Intent intent = new Intent(context,PracticeForListActivity.class);
        WXEntryActivity.dataMap.put(KeyUtil.DataMapKey, beanList);
        intent.putExtra(KeyUtil.ActionbarTitle, "  ");
        intent.putExtra(KeyUtil.IndexKey, index);
        context.startActivity(intent);
    }

}
