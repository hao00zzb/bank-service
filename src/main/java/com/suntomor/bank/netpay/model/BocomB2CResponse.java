package com.suntomor.bank.netpay.model;

import java.io.Serializable;

public class BocomB2CResponse implements Serializable {

	private static final long serialVersionUID = 362639161419222791L;
	/** 商户号 **/
	private String merID = "";
	/** 订单号 **/
	private String orderNumber = "";
	/** 订单金额|||| **/
	private String amount = "";
	/** 交易币种 **/
	private String curType = "CNY";
	/** 银行批次号 **/
	private String bankBatchNo;
	/** 商户批次号 **/
	private String merBatchNo;
	/** 交易日期 **/
	private String orderDate = "";
	/** 交易时间 **/
	private String orderTime = "";
	/** 交易流水号 **/
	private String serialNumber;
	/** 交易状态 0处理中 1成功 2失败 3异常 9未处理 **/
	private String tradeStatus;
	/** 手续费金额 **/
	private String feeAmount;
	/** 付款账号类型 0借记卡 2贷记卡 3他行银联卡 **/
	private String paymentAccountType;
	/** 银行备注 **/
	private String bankRemark;
	/** 付款方IP **/
	private String clientIp;
	
	public String getMerID() {
		return merID;
	}

	public void setMerID(String merID) {
		this.merID = merID;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	public String getBankBatchNo() {
		return bankBatchNo;
	}

	public void setBankBatchNo(String bankBatchNo) {
		this.bankBatchNo = bankBatchNo;
	}

	public String getMerBatchNo() {
		return merBatchNo;
	}

	public void setMerBatchNo(String merBatchNo) {
		this.merBatchNo = merBatchNo;
	}

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getPaymentAccountType() {
		return paymentAccountType;
	}

	public void setPaymentAccountType(String paymentAccountType) {
		this.paymentAccountType = paymentAccountType;
	}

	public String getBankRemark() {
		return bankRemark;
	}

	public void setBankRemark(String bankRemark) {
		this.bankRemark = bankRemark;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public boolean isSuccess() {
		return this.tradeStatus.equals("0") ? true : false;
	}
	
}
