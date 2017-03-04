package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.WebViewActivity;
import com.messi.cantonese.study.dao.BDJContent;
import com.messi.cantonese.study.util.KeyUtil;
import com.messi.cantonese.study.util.TimeUtil;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by luli on 10/23/16.
 */

public class RcJokeBudejieListItemViewHolder extends RecyclerView.ViewHolder {

    private SimpleDraweeView profileImage;
    private TextView name;
    private TextView time;
    private TextView des;
    private SimpleDraweeView list_item_img;
    private LinearLayout layout_cover;
    private JCVideoPlayerStandard videoplayer;
    private Context context;

    public RcJokeBudejieListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        profileImage = (SimpleDraweeView) convertView.findViewById(R.id.profile_image);
        name = (TextView) convertView.findViewById(R.id.name);
        time = (TextView) convertView.findViewById(R.id.time);
        des = (TextView) convertView.findViewById(R.id.des);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
        layout_cover = (LinearLayout) convertView.findViewById(R.id.layout_cover);
        videoplayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
    }

    public void render(final BDJContent mAVObject) {
        if(mAVObject.getmNativeADDataRef() == null){
            name.setText(mAVObject.getName());
            des.setText(mAVObject.getText().trim());
            time.setText(mAVObject.getCreate_time());

            profileImage.setImageURI(Uri.parse(mAVObject.getProfile_image()));

            if (mAVObject.getType().equals("10")) {
                list_item_img.setVisibility(View.VISIBLE);
                DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
                        .setAutoPlayAnimations(true)
                        .setUri(Uri.parse(mAVObject.getImage2()))
                        .build();
                list_item_img.setController(mDraweeController);
            } else {
                list_item_img.setVisibility(View.GONE);
            }

            if (mAVObject.getType().equals("41") || mAVObject.getType().equals("31")) {
                videoplayer.setVisibility(View.VISIBLE);
                videoplayer.setUp(mAVObject.getVideo_uri(), JCVideoPlayerStandard.SCREEN_LAYOUT_LIST,"");
                Glide.with(context)
                        .load(mAVObject.getVideo_uri())
                        .into(videoplayer.thumbImageView);
            }else{
                videoplayer.setVisibility(View.GONE);
            }

            list_item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClick(mAVObject);
                }
            });
        }else{
            final NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
            name.setText("VoiceAds广告");
            time.setText(TimeUtil.formatLongTimeForCustom(System.currentTimeMillis(),TimeUtil.DateFormat));
            des.setText(mNativeADDataRef.getTitle());
            list_item_img.setVisibility(View.VISIBLE);
            list_item_img.setImageURI(Uri.parse(mNativeADDataRef.getImage()));
            videoplayer.setVisibility(View.GONE);
            list_item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNativeADDataRef.onClicked(v);
                }
            });
        }
    }

    private void onItemClick(BDJContent mAVObject) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(KeyUtil.ActionbarTitle, mAVObject.getName());
        intent.putExtra(KeyUtil.URL, mAVObject.getImage3());
        context.startActivity(intent);
    }

}
