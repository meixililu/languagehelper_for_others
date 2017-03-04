package com.messi.cantonese.study.dao;

public class TwistaItem {

	private String id;

	private String quest;

	private String result;

	private boolean isShowResult;

	public boolean isShowResult() {
		return isShowResult;
	}

	public void setShowResult(boolean showResult) {
		isShowResult = showResult;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getQuest() {
		return quest;
	}

	public void setQuest(String quest) {
		this.quest = quest;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
}
