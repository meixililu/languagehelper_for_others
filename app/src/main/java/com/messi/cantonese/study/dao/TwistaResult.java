package com.messi.cantonese.study.dao;

import java.util.List;

public class TwistaResult {

	private int code;

	private String msg;

	private List<TwistaItem> newslist;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<TwistaItem> getNewslist() {
		return newslist;
	}

	public void setNewslist(List<TwistaItem> newslist) {
		this.newslist = newslist;
	}
}
