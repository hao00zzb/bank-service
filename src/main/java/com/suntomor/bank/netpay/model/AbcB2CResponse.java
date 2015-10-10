package com.suntomor.bank.netpay.model;

import java.io.Serializable;

public class AbcB2CResponse implements Serializable {

	private static final long serialVersionUID = -1912005504695520999L;

	private String returnCode;
	
	private String errorMessage;
	
	private String orderNo;
	
	private Double amount;
	
	private boolean success;
	
	private String batchNo;
	
	private String voucherNo;

	private String hostDate;
	
	private String hostTime;
	
	private String irspRef;

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

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getHostDate() {
		return hostDate;
	}

	public void setHostDate(String hostDate) {
		this.hostDate = hostDate;
	}

	public String getHostTime() {
		return hostTime;
	}

	public void setHostTime(String hostTime) {
		this.hostTime = hostTime;
	}

	public String getIrspRef() {
		return irspRef;
	}

	public void setIrspRef(String irspRef) {
		this.irspRef = irspRef;
	}

}
