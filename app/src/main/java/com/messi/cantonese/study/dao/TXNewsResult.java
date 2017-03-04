package com.messi.cantonese.study.dao;

import java.util.List;

public class TXNewsResult {
	
	private List<TXNewsItem> newslist;

	private int code;

	private String msg;

	public List<TXNewsItem> getNewslist() {
		return newslist;
	}

	public void setNewslist(List<TXNewsItem> newslist) {
		this.newslist = newslist;
	}

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
}
