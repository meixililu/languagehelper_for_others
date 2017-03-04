package com.messi.cantonese.study.dao;

public class ChDicBushouPinyinDao {

	private String id;
	private String pinyin_key;
	private String pinyin;
	private String type;
	private String bihua;
	private String bushou;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPinyin_key() {
		return pinyin_key;
	}

	public void setPinyin_key(String pinyin_key) {
		this.pinyin_key = pinyin_key;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getBihua() {
		return bihua;
	}

	public void setBihua(String bihua) {
		this.bihua = bihua;
	}

	public String getBushou() {
		return bushou;
	}

	public void setBushou(String bushou) {
		this.bushou = bushou;
	}
}