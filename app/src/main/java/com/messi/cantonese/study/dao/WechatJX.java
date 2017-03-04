package com.messi.cantonese.study.dao;

import java.util.List;

public class WechatJX {

	private int code;

	private List<WechatJXResult> newslist;

	private String msg;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<WechatJXResult> getNewslist() {
		return newslist;
	}

	public void setNewslist(List<WechatJXResult> newslist) {
		this.newslist = newslist;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
