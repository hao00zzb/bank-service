package com.suntomor.bank.frame.common;

import java.io.Serializable;

public class JSONResponse implements Serializable {

	private static final long serialVersionUID = 2866352044513698346L;
	
	private boolean success = false;
	
	private String msg;
	
	private Object data;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
