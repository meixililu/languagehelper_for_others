package com.messi.cantonese.study.dao;

public class ChDicDao {
	
	private String reason;

	private ChDicContentDao result;

	private int error_code;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ChDicContentDao getResult() {
		return result;
	}

	public void setResult(ChDicContentDao result) {
		this.result = result;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
}