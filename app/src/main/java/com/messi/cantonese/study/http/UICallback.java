package com.messi.cantonese.study.http;

import android.app.Activity;
import android.text.TextUtils;

import com.avos.avoscloud.okhttp.Callback;
import com.avos.avoscloud.okhttp.Request;
import com.avos.avoscloud.okhttp.Response;

import java.io.IOException;

public class UICallback implements Callback{
	
	private Activity context;
	private String responseString;
	
	public UICallback(Activity context){
		this.context = context;
	}

	@Override
	public void onFailure(Request arg0, IOException arg1) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				onFinished();
				onFailured();
			}
		});
	}

	@Override
	public void onResponse(final Response mResponse) throws IOException {
		if (mResponse != null && mResponse.isSuccessful()){
			responseString = mResponse.body().string();
		}
		if(context != null && !TextUtils.isEmpty(responseString)){
			context.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					onFinished();
					onResponsed(responseString);
				}
			});
		}
	}
	
	public void onFailured() {}
	public void onFinished() {}
	public void onResponsed(String responseString) {}
	
}
