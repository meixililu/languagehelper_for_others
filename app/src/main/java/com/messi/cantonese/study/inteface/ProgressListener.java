package com.messi.cantonese.study.inteface;

public interface ProgressListener {

	void update(long bytesRead, long contentLength, boolean done);
	
}
