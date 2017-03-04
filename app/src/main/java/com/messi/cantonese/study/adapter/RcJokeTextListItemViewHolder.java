package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.iflytek.voiceads.NativeADDataRef;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.dao.JokeContent;

/**
 * Created by luli on 10/23/16.
 */

public class RcJokeTextListItemViewHolder extends RecyclerView.ViewHolder {

    private final TextView title;
    private final TextView text;
    private final SimpleDraweeView list_item_img;
    private Context context;

    public RcJokeTextListItemViewHolder(View convertView) {
        super(convertView);
        this.context = convertView.getContext();
        title = (TextView) convertView.findViewById(R.id.title);
        text = (TextView) convertView.findViewById(R.id.text);
        list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
    }

    public void render(final JokeContent mAVObject) {
        if(mAVObject.getmNativeADDataRef() == null){
            list_item_img.setVisibility(View.GONE);
            text.setVisibility(View.VISIBLE);
            title.setText( mAVObject.getTitle() );
            text.setText(Html.fromHtml(mAVObject.getText()) );
        }else{
            list_item_img.setVisibility(View.VISIBLE);
            text.setVisibility(View.GONE);
            final NativeADDataRef mNativeADDataRef = mAVObject.getmNativeADDataRef();
            title.setText(mNativeADDataRef.getTitle() + "  (VoiceAds广告)");
            list_item_img.setImageURI(Uri.parse(mNativeADDataRef.getImage()));
            list_item_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mNativeADDataRef.onClicked(v);
                }
            });
        }
    }


}
