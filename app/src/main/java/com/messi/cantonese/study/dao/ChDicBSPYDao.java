package com.messi.cantonese.study.dao;

import java.util.List;

public class ChDicBSPYDao {
	
	private String reason;

	private List<ChDicBushouPinyinDao> result;

	private int error_code;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<ChDicBushouPinyinDao> getResult() {
		return result;
	}

	public void setResult(List<ChDicBushouPinyinDao> result) {
		this.result = result;
	}

	public int getError_code() {
		return error_code;
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}
}