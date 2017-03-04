package com.messi.cantonese.study.dao;

import com.iflytek.voiceads.NativeADDataRef;

public class WechatJXResult {

	private String ctime;

	private String title;

	private String description;

	private String picUrl;

	private String url;

	private NativeADDataRef mNativeADDataRef;

	private boolean isHasShowAD;

	public boolean isHasShowAD() {
		return isHasShowAD;
	}

	public void setHasShowAD(boolean hasShowAD) {
		isHasShowAD = hasShowAD;
	}

	public String getCtime() {
		return ctime;
	}

	public void setCtime(String ctime) {
		this.ctime = ctime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public NativeADDataRef getmNativeADDataRef() {
		return mNativeADDataRef;
	}

	public void setmNativeADDataRef(NativeADDataRef mNativeADDataRef) {
		this.mNativeADDataRef = mNativeADDataRef;
	}


}
