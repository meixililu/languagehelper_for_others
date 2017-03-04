package com.messi.cantonese.study.dao;

import android.text.TextUtils;

public class ChDicIdiomContentDao {

	private String bushou;
	private String head;
	private String pinyin;
	private String chengyujs;
	private String from_;
	private String example;
	private String yufa;
	private String ciyujs;
	private String result;
	private String yinzhengjs;
	private String[] tongyi;
	private String[] fanyi;

	public String getResultForShow(String word){
		return word+"\n"+result;
	}

	public void setResult(){
		StringBuilder sb = new StringBuilder();
		if (!TextUtils.isEmpty(pinyin)) {
			sb.append(pinyin);
			sb.append("\n");
		}
		if (!TextUtils.isEmpty(chengyujs)) {
			sb.append("成语解释:");
			sb.append("\n");
			sb.append(chengyujs);
			sb.append("\n");
			sb.append("\n");
		}
		if (!TextUtils.isEmpty(from_)) {
			sb.append("成语出处:");
			sb.append("\n");
			sb.append(from_);
			sb.append("\n");
			sb.append("\n");
		}
		if (!TextUtils.isEmpty(example)) {
			sb.append("举例:");
			sb.append("\n");
			sb.append(example);
			sb.append("\n");
			sb.append("\n");
		}
		if (!TextUtils.isEmpty(ciyujs)) {
			sb.append("词语解释:");
			sb.append("\n");
			sb.append(ciyujs);
			sb.append("\n");
			sb.append("\n");
		}
		if (!TextUtils.isEmpty(yufa)) {
			sb.append("语法:");
			sb.append("\n");
			sb.append(yufa);
			sb.append("\n");
			sb.append("\n");
		}
		if (!TextUtils.isEmpty(yinzhengjs)) {
			sb.append("引证解释:");
			sb.append("\n");
			sb.append(yinzhengjs);
			sb.append("\n");
			sb.append("\n");
		}

		if(tongyi != null){
			sb.append("同义词:");
			sb.append("\n");
			for(String item : tongyi){
				sb.append(item);
				sb.append("\n");
			}
			sb.append("\n");
		}
		if(fanyi != null){
			sb.append("反义词:");
			sb.append("\n");
			for(String item : fanyi){
				sb.append(item);
				sb.append("\n");
			}
		}
		sb.append("\n");
		sb.append("\n");
		result = sb.toString();
	}

	public String getBushou() {
		return bushou;
	}

	public void setBushou(String bushou) {
		this.bushou = bushou;
	}

	public String getHead() {
		return head;
	}

	public void setHead(String head) {
		this.head = head;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getChengyujs() {
		return chengyujs;
	}

	public void setChengyujs(String chengyujs) {
		this.chengyujs = chengyujs;
	}

	public String getFrom_() {
		return from_;
	}

	public void setFrom_(String from_) {
		this.from_ = from_;
	}

	public String getExample() {
		return example;
	}

	public void setExample(String example) {
		this.example = example;
	}

	public String getYufa() {
		return yufa;
	}

	public void setYufa(String yufa) {
		this.yufa = yufa;
	}

	public String getCiyujs() {
		return ciyujs;
	}

	public void setCiyujs(String ciyujs) {
		this.ciyujs = ciyujs;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getYinzhengjs() {
		return yinzhengjs;
	}

	public void setYinzhengjs(String yinzhengjs) {
		this.yinzhengjs = yinzhengjs;
	}

	public String[] getTongyi() {
		return tongyi;
	}

	public void setTongyi(String[] tongyi) {
		this.tongyi = tongyi;
	}

	public String[] getFanyi() {
		return fanyi;
	}

	public void setFanyi(String[] fanyi) {
		this.fanyi = fanyi;
	}
}