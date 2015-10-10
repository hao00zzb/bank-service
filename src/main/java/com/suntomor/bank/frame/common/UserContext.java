package com.suntomor.bank.frame.common;

import java.io.Serializable;

public class UserContext implements Serializable {

	private static final long serialVersionUID = -1260654722380762925L;

	private String ipAddress;
	
	public UserContext() {
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
