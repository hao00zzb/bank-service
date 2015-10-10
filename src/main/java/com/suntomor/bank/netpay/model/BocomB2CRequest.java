package com.suntomor.bank.netpay.model;

import java.io.Serializable;

public class BocomB2CRequest implements Serializable {

	private static final long serialVersionUID = 362639161419222791L;

	private String merID = "";

	private String interfaceVersion = "1.0.0.0";

	private String orderUrl = "";

	private String orderNumber = "";

	private String amount = "";

	private String orderDate = "";

	private String orderTime = "";

	private String tranType = "0";

	private String curType = "CNY";

	private String orderContent = "";

	private String orderMono = "";

	private String phdFlag = "0";

	private String notifyType = "1";

	private String merURL = "";

	private String goodsURL = "";

	private String jumpSeconds = "3";

	private String payBatchNo = "";

	private String proxyMerName = "";

	private String proxyMerType = "";

	private String proxyMerCredentials = "";

	private String netType = "0";

	private String merSignMsg = "";
	
	private boolean wap;

	public String getMerID() {
		return merID;
	}

	public void setMerID(String merID) {
		this.merID = merID;
	}

	public String getInterfaceVersion() {
		return interfaceVersion;
	}

	public void setInterfaceVersion(String interfaceVersion) {
		this.interfaceVersion = interfaceVersion;
	}

	public String getOrderUrl() {
		return orderUrl;
	}

	public void setOrderUrl(String orderUrl) {
		this.orderUrl = orderUrl;
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

	public String getTranType() {
		return tranType;
	}

	public void setTranType(String tranType) {
		this.tranType = tranType;
	}

	public String getCurType() {
		return curType;
	}

	public void setCurType(String curType) {
		this.curType = curType;
	}

	public String getOrderContent() {
		return orderContent;
	}

	public void setOrderContent(String orderContent) {
		this.orderContent = orderContent;
	}

	public String getOrderMono() {
		return orderMono;
	}

	public void setOrderMono(String orderMono) {
		this.orderMono = orderMono;
	}

	public String getPhdFlag() {
		return phdFlag;
	}

	public void setPhdFlag(String phdFlag) {
		this.phdFlag = phdFlag;
	}

	public String getNotifyType() {
		return notifyType;
	}

	public void setNotifyType(String notifyType) {
		this.notifyType = notifyType;
	}

	public String getMerURL() {
		return merURL;
	}

	public void setMerURL(String merURL) {
		this.merURL = merURL;
	}

	public String getGoodsURL() {
		return goodsURL;
	}

	public void setGoodsURL(String goodsURL) {
		this.goodsURL = goodsURL;
	}

	public String getJumpSeconds() {
		return jumpSeconds;
	}

	public void setJumpSeconds(String jumpSeconds) {
		this.jumpSeconds = jumpSeconds;
	}

	public String getPayBatchNo() {
		return payBatchNo;
	}

	public void setPayBatchNo(String payBatchNo) {
		this.payBatchNo = payBatchNo;
	}

	public String getProxyMerName() {
		return proxyMerName;
	}

	public void setProxyMerName(String proxyMerName) {
		this.proxyMerName = proxyMerName;
	}

	public String getProxyMerType() {
		return proxyMerType;
	}

	public void setProxyMerType(String proxyMerType) {
		this.proxyMerType = proxyMerType;
	}

	public String getProxyMerCredentials() {
		return proxyMerCredentials;
	}

	public void setProxyMerCredentials(String proxyMerCredentials) {
		this.proxyMerCredentials = proxyMerCredentials;
	}

	public String getNetType() {
		return netType;
	}

	public void setNetType(String netType) {
		this.netType = netType;
	}

	public String getMerSignMsg() {
		return merSignMsg;
	}

	public void setMerSignMsg(String merSignMsg) {
		this.merSignMsg = merSignMsg;
	}

	public boolean isWap() {
		return wap;
	}

	public void setWap(boolean wap) {
		this.wap = wap;
	}

}
