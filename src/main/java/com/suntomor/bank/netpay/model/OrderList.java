package com.suntomor.bank.netpay.model;

import java.io.Serializable;
import java.util.Date;

public class OrderList implements Serializable {

	private static final long serialVersionUID = -5139714920796834381L;

	/**
	 * t_order_list.C_ID 主键
	 */
	private Integer id;

	/**
	 * t_order_list.C_BANK_TYPE 银行类型,abc:农行,bocom:建行
	 */
	private String bankType;

	/**
	 * t_order_list.C_ORDER_NUMBER 订单编号
	 */
	private String orderNumber;

	/**
	 * t_order_list.C_TOTAL_MONEY 总金额
	 */
	private Double totalMoney;

	/**
	 * t_order_list.C_BUSINESS_ID 业务ID
	 */
	private Integer businessId;

	/**
	 * t_order_list.C_ORDER_TYPE 缴款类型,1:住院费，2:体检费
	 */
	private Integer orderType;
	
	/**
	 * t_order_list.PROJECT_NAME 缴款类型名称
	 */
	private String orderTypeName;

	/**
	 * t_order_list.C_PAYER_NAME 付款人姓名
	 */
	private String payerName;

	/**
	 * t_order_list.C_MOBILE_NO 手机号
	 */
	private String mobileNo;

	/**
	 * t_order_list.C_IDCARD_NUMBER 身份证号
	 */
	private String idcardNumber;

	/**
	 * t_order_list.C_PAY_TIME 付款时间
	 */
	private Date payTime;

	/**
	 * t_order_list.C_IP_ADDRESS 付款IP
	 */
	private String ipAddress;

	/**
	 * t_order_list.C_STATUS 订单状态,0:失败，1:成功，2:取消
	 */
	private Integer status;

	/**
	 * t_order_list.C_SERIAL_NUMBER 订单流水号
	 */
	private String serialNumber;

	/**
	 * t_order_list.c_is_wap 是否为手机支付
	 */
	private boolean wap;
	
	/**
	 * t_order_list.C_CREATE_TIME 创建时间
	 */
	private Date createTime;

	/**
	 * t_order_list.C_MODIFY_TIME 更新时间
	 */
	private Date modifyTime;

	/**
	 * @return t_order_list.C_ID 主键
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            the value for t_order_list.C_ID 主键
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return t_order_list.C_BANK_TYPE 银行类型,abc:农行,bocom:建行
	 */
	public String getBankType() {
		return bankType;
	}

	/**
	 * @param bankType
	 *            the value for t_order_list.C_BANK_TYPE 银行类型,abc:农行,bocom:建行
	 */
	public void setBankType(String bankType) {
		this.bankType = bankType == null ? null : bankType.trim();
	}

	/**
	 * @return t_order_list.C_ORDER_NUMBER 订单编号
	 */
	public String getOrderNumber() {
		return orderNumber;
	}

	/**
	 * @param orderNumber
	 *            the value for t_order_list.C_ORDER_NUMBER 订单编号
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber == null ? null : orderNumber.trim();
	}

	/**
	 * @return t_order_list.C_TOTAL_MONEY 总金额
	 */
	public Double getTotalMoney() {
		return totalMoney;
	}

	/**
	 * @param totalMoney
	 *            the value for t_order_list.C_TOTAL_MONEY 总金额
	 */
	public void setTotalMoney(Double totalMoney) {
		this.totalMoney = totalMoney;
	}

	/**
	 * @return t_order_list.C_BUSINESS_ID 业务ID
	 */
	public Integer getBusinessId() {
		return businessId;
	}

	/**
	 * @param businessId
	 *            the value for t_order_list.C_BUSINESS_ID 业务ID
	 */
	public void setBusinessId(Integer businessId) {
		this.businessId = businessId;
	}

	/**
	 * @return t_order_list.C_ORDER_TYPE 缴款类型,1:住院费，2:体检费
	 */
	public Integer getOrderType() {
		return orderType;
	}

	/**
	 * @param orderType
	 *            the value for t_order_list.C_ORDER_TYPE 缴款类型,1:住院费，2:体检费
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	/**
	 * @return t_order_list.PROJECT_NAME 缴款类型名称
	 */
	public String getOrderTypeName() {
		return orderTypeName;
	}

	/**
	 * @param orderTypeName
	 *            the value for t_order_list.PROJECT_NAME 缴款类型名称
	 */
	public void setOrderTypeName(String orderTypeName) {
		this.orderTypeName = orderTypeName;
	}

	/**
	 * @return t_order_list.C_PAYER_NAME 付款人姓名
	 */
	public String getPayerName() {
		return payerName;
	}

	/**
	 * @param payerName
	 *            the value for t_order_list.C_PAYER_NAME 付款人姓名
	 */
	public void setPayerName(String payerName) {
		this.payerName = payerName == null ? null : payerName.trim();
	}

	/**
	 * @return t_order_list.C_MOBILE_NO 手机号
	 */
	public String getMobileNo() {
		return mobileNo;
	}

	/**
	 * @param mobileNo
	 *            the value for t_order_list.C_MOBILE_NO 手机号
	 */
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo == null ? null : mobileNo.trim();
	}

	/**
	 * @return t_order_list.C_IDCARD_NUMBER 身份证号
	 */
	public String getIdcardNumber() {
		return idcardNumber;
	}

	/**
	 * @param idcardNumber
	 *            the value for t_order_list.C_IDCARD_NUMBER 身份证号
	 */
	public void setIdcardNumber(String idcardNumber) {
		this.idcardNumber = idcardNumber == null ? null : idcardNumber.trim();
	}

	/**
	 * @return t_order_list.C_PAY_TIME 付款时间
	 */
	public Date getPayTime() {
		return payTime;
	}

	/**
	 * @param payTime
	 *            the value for t_order_list.C_PAY_TIME 付款时间
	 */
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	/**
	 * @return t_order_list.C_IP_ADDRESS 付款IP
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * @param ipAddress
	 *            the value for t_order_list.C_IP_ADDRESS 付款IP
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress == null ? null : ipAddress.trim();
	}

	/**
	 * @return t_order_list.C_STATUS 订单状态,0:失败，1:成功，2:取消
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the value for t_order_list.C_STATUS 订单状态,0:失败，1:成功，2:取消
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return t_order_list.C_SERIAL_NUMBER 订单流水号
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *            the value for t_order_list.C_SERIAL_NUMBER 订单流水号
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber == null ? null : serialNumber.trim();
	}

	/**
	 * @return t_order_list.c_wap 是否为手机支付
	 */
	public boolean isWap() {
		return wap;
	}

	/**
	 * @param wap the value for t_order_list.c_wap 是否为手机支付
	 */
	public void setWap(boolean wap) {
		this.wap = wap;
	}

	/**
	 * @return t_order_list.C_CREATE_TIME 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the value for t_order_list.C_CREATE_TIME 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return t_order_list.C_MODIFY_TIME 更新时间
	 */
	public Date getModifyTime() {
		return modifyTime;
	}

	/**
	 * @param modifyTime
	 *            the value for t_order_list.C_MODIFY_TIME 更新时间
	 */
	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
}