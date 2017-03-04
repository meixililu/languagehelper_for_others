package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.WebViewActivity;
import com.messi.cantonese.study.dao.TXNewsItem;
import com.messi.cantonese.study.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcNewsListItemViewHolder extends RecyclerView.ViewHolder {

    private final FrameLayout layout_cover;
    private final TextView title;
    private final TextView type_name;
    private final TextView source_name;
    private final LinearLayout list_item_img_parent;
    private final SimpleDraweeView list_item_img;
    private Context context;

    public RcNewsListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        layout_cover = (FrameLayout) convertView.findViewById(R.id.layout_cover);
        list_item_img_parent = (LinearLayout) convertView.findViewById(R.id.list_item_img_parent);
        title = (TextView) convertView.findViewById(R.id.title);
        type_name = (TextView) convertView.findViewById(R.id.type_name);
        source_name = (TextView) convertView.findViewById(R.id.source_name);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
    }

    public void render(final TXNewsItem mAVObject) {
        final NativeADDataRef mNativeADDataRef = (NativeADDataRef) mAVObject.getmNativeADDataRef();
        if(mNativeADDataRef == null){
            title.setText( mAVObject.getTitle() );
            source_name.setText( mAVObject.getDescription() );
            if(!TextUtils.isEmpty(mAVObject.getPicUrl())){
                list_item_img_parent.setVisibility(View.VISIBLE);
                list_item_img.setVisibility(View.VISIBLE);
                list_item_img.setImageURI(mAVObject.getPicUrl());
            }else{
                list_item_img_parent.setVisibility(View.GONE);
                list_item_img.setVisibility(View.GONE);
            }
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toDetailActivity(mAVObject);
                }
            });
        }else{
            title.setText( mNativeADDataRef.getSubTitle() );
            type_name.setText(mNativeADDataRef.getTitle());
            source_name.setText("VoiceAds广告");
            list_item_img_parent.setVisibility(View.VISIBLE);
            list_item_img.setVisibility(View.VISIBLE);
            list_item_img.setImageURI(mNativeADDataRef.getImage());
            layout_cover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNativeADDataRef.onClicked(v);
                }
            });
        }
    }

    private void toDetailActivity(TXNewsItem mAVObject){
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, " ");
        intent.putExtra(KeyUtil.ToolbarBackgroundColorKey, R.color.news_title_bg);
        intent.putExtra(KeyUtil.URL, mAVObject.getUrl());
        context.startActivity(intent);
    }

}
