package com.suntomor.bank.netpay.model;

public enum BankType {

	/** 农行 **/
	ABC("abc", 10),
	/** 交通银行 **/
	BOCOM("bocom", 11);
	
	private String code;
	private Integer value;
	
	BankType(String code, Integer value) {
		this.code = code;
		this.value = value;
	}

	public String getCode() {
		return code;
	}
	
	public Integer getValue() {
		return value;
	}

}
