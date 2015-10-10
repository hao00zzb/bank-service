package com.suntomor.bank.netpay.model;

import java.io.Serializable;

public class AbcB2CRequest implements Serializable {

	private static final long serialVersionUID = -2984920625469739033L;

	private String returnCode;

	private String errorMessage;

	private String paymentUrl;

	public String getReturnCode() {
		return returnCode;
	}

	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getPaymentUrl() {
		return paymentUrl;
	}

	public void setPaymentUrl(String paymentUrl) {
		this.paymentUrl = paymentUrl;
	}

}
