package com.messi.cantonese.study.dao;

import java.util.List;

public class ToutiaoNewsResult{
	
	private List<ToutiaoNewsItem> data;

	private String stat;

	public List<ToutiaoNewsItem> getData() {
		return data;
	}

	public void setData(List<ToutiaoNewsItem> data) {
		this.data = data;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
}
