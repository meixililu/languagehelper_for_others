package com.messi.cantonese.study.dao;

import android.text.TextUtils;

public class ChDicBushouPinyinDetailDao {

	private String id;
	private String zi;
	private String py;
	private String wubi;
	private String pinyin;
	private String bushou;
	private String bihua;
	private String jijieResult;
	private String xiangjieResult;
	private String[] jijie;
	private String[] xiangjie;
	private boolean isShowXiangjie;

    public String getShareAndCopy(){
        return jijieResult + "\n" + xiangjieResult;
    }

	public void setData(){
		StringBuilder sb = new StringBuilder();
		StringBuilder sbxj = new StringBuilder();
		if(jijie != null){
			for(String item : jijie){
				if(!TextUtils.isEmpty(item.trim())){
					if(!item.contains("笔顺编号")){
						sb.append(item);
						sb.append("\n");
					}
				}
			}
		}
		jijieResult = sb.substring(0, sb.lastIndexOf("\n"));
		if(xiangjie != null && xiangjie.length > 1){
            sbxj.append("详细解释：");
            sbxj.append("\n");
			for(String item : xiangjie){
				if(!TextUtils.isEmpty(item.trim())){
					sbxj.append(item);
					sbxj.append("\n");
				}
			}
		}
		if(sbxj.length() > 1){
			xiangjieResult = sbxj.substring(0, sbxj.lastIndexOf("\n"));
		}else{
			xiangjieResult = sbxj.toString();
		}
	}

	public boolean isShowXiangjie() {
		return isShowXiangjie;
	}

	public void setShowXiangjie(boolean showXiangjie) {
		isShowXiangjie = showXiangjie;
	}

	public String getXiangjieResult() {
		return xiangjieResult;
	}

	public void setXiangjieResult(String xiangjieResult) {
		this.xiangjieResult = xiangjieResult;
	}

	public String getJijieResult() {
		return jijieResult;
	}

	public void setJijieResult(String jijieResult) {
		this.jijieResult = jijieResult;
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
}