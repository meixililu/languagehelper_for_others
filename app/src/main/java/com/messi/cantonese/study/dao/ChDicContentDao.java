package com.messi.cantonese.study.dao;

public class ChDicContentDao {

	private String id;
	private String zi;
	private String py;
	private String wubi;
	private String pinyin;
	private String bushou;
	private String bihua;
	private String[] jijie;
	private String result;
	private String[] xiangjie;

	public String getResultForShow(String question){
		return question + "\n" + result;
	}

	public void setResult(){
		StringBuilder sb = new StringBuilder();
		if(jijie != null){
			sb.append("简解:");
			sb.append("\n");
			for(String item : jijie){
				sb.append(item);
				sb.append("\n");
			}
		}
		if(xiangjie != null){
			sb.append("详解:");
			sb.append("\n");
			for(String item : xiangjie){
				sb.append(item);
				sb.append("\n");
			}
		}
		sb.append("\n");
		sb.append("\n");
		sb.append("\n");
		result = sb.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZi() {
		return zi;
	}

	public void setZi(String zi) {
		this.zi = zi;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public String getWubi() {
		return wubi;
	}

	public void setWubi(String wubi) {
		this.wubi = wubi;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getBushou() {
		return bushou;
	}

	public void setBushou(String bushou) {
		this.bushou = bushou;
	}

	public String getBihua() {
		return bihua;
	}

	public void setBihua(String bihua) {
		this.bihua = bihua;
	}

	public String[] getJijie() {
		return jijie;
	}

	public void setJijie(String[] jijie) {
		this.jijie = jijie;
	}

	public String[] getXiangjie() {
		return xiangjie;
	}

	public void setXiangjie(String[] xiangjie) {
		this.xiangjie = xiangjie;
	}
}