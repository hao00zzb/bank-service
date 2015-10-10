package com.suntomor.bank.checking.model;

import java.io.Serializable;
import java.util.Date;

public class AccountStatement implements Serializable {

	private static final long serialVersionUID = -6268692585626288716L;

	/**
     * t_account_statement.c_id 主键
     */
    private Integer id;

    /**
     * t_account_statement.c_bank_type 银行类型,abc:农行,bocom:建行
     */
    private String bankType;

    /**
     * t_account_statement.c_order_id 订单ID
     */
    private Integer orderId;

    /**
     * t_account_statement.c_order_number 订单编号
     */
    private String orderNumber;

    /**
     * t_account_statement.c_business_id 业务ID
     */
    private Integer businessId;

    /**
     * t_account_statement.c_payer_name 缴款人姓名
     */
    private String payerName;

    /**
     * t_account_statement.c_payer_account 缴款人账号
     */
    private String payerAccount;

    /**
     * t_account_statement.c_pay_time 付款时间
     */
    private Date payTime;

    /**
     * t_account_statement.c_reconciliation_time 对账时间
     */
    private Date reconciliationTime;

    /**
     * t_account_statement.c_total_money 总金额
     */
    private Double totalMoney;

    /**
     * t_account_statement.c_fund_source 款项来源
     */
    private String fundSource;

    /**
     * t_account_statement.c_serial_number 订单流水号
     */
    private String serialNumber;

    /**
     * t_account_statement.c_create_time 创建时间
     */
    private Date createTime;

    /**
     * @return t_account_statement.c_id 主键
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the value for t_account_statement.c_id 主键
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return t_account_statement.c_bank_type 银行类型,abc:农行,bocom:建行
     */
    public String getBankType() {
        return bankType;
    }

    /**
     * @param bankType the value for t_account_statement.c_bank_type 银行类型,abc:农行,bocom:建行
     */
    public void setBankType(String bankType) {
        this.bankType = bankType == null ? null : bankType.trim();
    }

    /**
     * @return t_account_statement.c_order_id 订单ID
     */
    public Integer getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the value for t_account_statement.c_order_id 订单ID
     */
    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    /**
     * @return t_account_statement.c_order_number 订单编号
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * @param orderNumber the value for t_account_statement.c_order_number 订单编号
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber == null ? null : orderNumber.trim();
    }

    /**
     * @return t_account_statement.c_business_id 业务ID
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * @param businessId the value for t_account_statement.c_business_id 业务ID
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * @return t_account_statement.c_payer_name 缴款人姓名
     */
    public String getPayerName() {
        return payerName;
    }

    /**
     * @param payerName the value for t_account_statement.c_payer_name 缴款人姓名
     */
    public void setPayerName(String payerName) {
        this.payerName = payerName == null ? null : payerName.trim();
    }

    /**
     * @return t_account_statement.c_payer_account 缴款人账号
     */
    public String getPayerAccount() {
        return payerAccount;
    }

    /**
     * @param payerAccount the value for t_account_statement.c_payer_account 缴款人账号
     */
    public void setPayerAccount(String payerAccount) {
        this.payerAccount = payerAccount == null ? null : payerAccount.trim();
    }

    /**
     * @return t_account_statement.c_pay_time 付款时间
     */
    public Date getPayTime() {
        return payTime;
    }

    /**
     * @param payTime the value for t_account_statement.c_pay_time 付款时间
     */
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    /**
     * @return t_account_statement.c_reconciliation_time 对账时间
     */
    public Date getReconciliationTime() {
        return reconciliationTime;
    }

    /**
     * @param reconciliationTime the value for t_account_statement.c_reconciliation_time 对账时间
     */
    public void setReconciliationTime(Date reconciliationTime) {
        this.reconciliationTime = reconciliationTime;
    }

    /**
     * @return t_account_statement.c_total_money 总金额
     */
    public Double getTotalMoney() {
        return totalMoney;
    }

    /**
     * @param totalMoney the value for t_account_statement.c_total_money 总金额
     */
    public void setTotalMoney(Double totalMoney) {
        this.totalMoney = totalMoney;
    }

    /**
     * @return t_account_statement.c_fund_source 款项来源
     */
    public String getFundSource() {
        return fundSource;
    }

    /**
     * @param fundSource the value for t_account_statement.c_fund_source 款项来源
     */
    public void setFundSource(String fundSource) {
        this.fundSource = fundSource == null ? null : fundSource.trim();
    }

    /**
     * @return t_account_statement.c_serial_number 订单流水号
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the value for t_account_statement.c_serial_number 订单流水号
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    /**
     * @return t_account_statement.c_create_time 创建时间
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime the value for t_account_statement.c_create_time 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}