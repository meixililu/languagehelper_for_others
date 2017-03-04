package com.messi.cantonese.study.dao;

public class ChDicIdiomDao {

    private String reason;

    private ChDicIdiomContentDao result;

    private int error_code;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public ChDicIdiomContentDao getResult() {
        return result;
    }

    public void setResult(ChDicIdiomContentDao result) {
        this.result = result;
    }

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }
}