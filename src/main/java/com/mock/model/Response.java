package com.mock.model;

import java.util.HashMap;

public class Response {
	private int status;
	private HashMap<String, Object> headers;
	private int fixedDelayMilliseconds;
	private String bodyFileName;

	public String getBodyFileName() {
		return bodyFileName;
	}

	public void setBodyFileName(String bodyFileName) {
		this.bodyFileName = bodyFileName;
	}

	public int getFixedDelayMilliseconds() {
		return fixedDelayMilliseconds;
	}

	public void setFixedDelayMilliseconds(int fixedDelayMilliseconds) {
		this.fixedDelayMilliseconds = fixedDelayMilliseconds;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public HashMap<String, Object> getHeaders() {
		return headers;
	}

	public void setHeaders(HashMap<String, Object> headers) {
		this.headers = headers;
	}

}
