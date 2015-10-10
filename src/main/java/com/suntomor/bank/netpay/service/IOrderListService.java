package com.suntomor.bank.netpay.service;

import java.util.Date;

import com.suntomor.bank.netpay.model.OrderList;

public interface IOrderListService {

	/**
	 * 保存或更新订单
	 * @param orderList
	 * @return
	 */
	boolean saveOrUpdate(OrderList orderList);
	
	/**
	 * 根据订单号查询订单信息
	 * @param orderNumber 订单号
	 * @return {@code OrderList}
	 */
	OrderList query(String orderNumber);

	/**
	 * 处理订单缴款书成功
	 * @param orderNumber 订单号
	 * @param amount 金额
	 * @param payTime 付款时间 
	 * @param serialNumber 流水号
	 * @return true:成功，false:失败
	 */
	boolean orderSuccess(String orderNumber, Double amount, Date payTime, String serialNumber);

}
