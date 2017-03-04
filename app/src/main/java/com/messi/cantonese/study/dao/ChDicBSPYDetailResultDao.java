package com.messi.cantonese.study.dao;


public class ChDicBSPYDetailResultDao {
	
	private String reason;
	private ChDicBSPYDetailListResultDao result;
	private int error_code;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public ChDicBSPYDetailListResultDao getResult() {
		return result;
	}

	public void setResult(ChDicBSPYDetailListResultDao result) {
		this.result = result;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
}