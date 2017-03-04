package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.ViewImageActivity;
import com.messi.cantonese.study.dao.JokeContent;
import com.messi.cantonese.study.util.KeyUtil;

/**
 * Created by luli on 10/23/16.
 */

public class RcJokePictureListItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView name;
    private final SimpleDraweeView list_item_img;
    private Context context;

    public RcJokePictureListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        name = (TextView) convertView.findViewById(R.id.name);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
    }

    public void render(final JokeContent mAVObject) {
        if(mAVObject.getmNativeADDataRef() == null){
            name.setText( mAVObject.getTitle() );
            list_item_img.setImageURI(Uri.parse(mAVObject.getImg()));
            list_item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(mAVObject);
                }
            });
        }else{
            final NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
            name.setText(mNativeADDataRef.getTitle() + "  (VoiceAds广告)");
            list_item_img.setImageURI(Uri.parse(mNativeADDataRef.getImage()));
            list_item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNativeADDataRef.onClicked(v);
                }
            });
        }
    }

    private void onItemClick(JokeContent mAVObject) {
        Intent intent = new Intent(context, ViewImageActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getTitle());
        intent.putExtra(KeyUtil.BigImgUrl, mAVObject.getImg());
        context.startActivity(intent);
    }

}
