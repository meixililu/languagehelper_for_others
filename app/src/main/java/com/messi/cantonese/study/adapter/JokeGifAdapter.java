package com.messi.cantonese.study.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.messi.cantonese.study.R;
import com.messi.cantonese.study.dao.JokeContent;

import java.util.List;

public class JokeGifAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private Context context;
	private List<JokeContent> avObjects;

	public JokeGifAdapter(Context mContext, List<JokeContent> avObjects) {
		context = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		this.avObjects = avObjects;
	}

	public int getCount() {
		return avObjects.size();
	}

	public Object getItem(int position) {
		return avObjects.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.joke_gif_list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.list_item_img = (SimpleDraweeView) convertView.findViewById(R.id.list_item_img);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		final JokeContent mAVObject = avObjects.get(position);
		holder.name.setText( mAVObject.getTitle() );

		DraweeController mDraweeController = Fresco.newDraweeControllerBuilder()
				.setAutoPlayAnimations(true)
				.setUri(Uri.parse(mAVObject.getImg()))
				.build();
		holder.list_item_img.setController(mDraweeController);

		return convertView;
	}



	static class ViewHolder {
		TextView name;
		SimpleDraweeView list_item_img;
	}



}
