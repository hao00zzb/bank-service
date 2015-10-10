package com.suntomor.bank.netpay.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.suntomor.bank.netpay.constants.OrderListCons;
import com.suntomor.bank.netpay.dao.IOrderListDAO;
import com.suntomor.bank.netpay.model.OrderList;
import com.suntomor.bank.netpay.service.IOrderListService;

@Service("iOrderListService")
public class OrderListServiceImpl implements IOrderListService {

	@Resource(name = "iOrderListDAO")
	private IOrderListDAO orderListDAO;

	@Override
	public boolean saveOrUpdate(OrderList orderList) {
		Assert.notNull(orderList);
		
		if (orderList.getId() == null) {
			orderList.setCreateTime(new Date());
			orderList.setStatus(OrderListCons.STATUS_FAILURE);
		}
		orderList.setModifyTime(new Date());
		
		boolean flag = this.orderListDAO.saveOrUpdate(orderList) > 0;
		return flag;
	}

	@Override
	public OrderList query(String orderNumber) {
		Assert.notNull(orderNumber);
		
		Map<String, Object> parameter = new HashMap<String, Object>();
		parameter.put("orderNumber", orderNumber);
		
		return this.orderListDAO.queryByParameterOne(parameter);
	}

	@Override
	public boolean orderSuccess(String orderNumber, Double amount, Date payTime, String serialNumber) {
		Assert.notNull(orderNumber);
		Assert.notNull(amount);
		
		OrderList orderList = query(orderNumber);
		orderList.setTotalMoney(amount);
		orderList.setPayTime(payTime);
		orderList.setStatus(OrderListCons.STATUS_SUCCESS);
		orderList.setSerialNumber(serialNumber);
		orderList.setModifyTime(new Date());
		boolean flag = this.orderListDAO.saveOrUpdate(orderList) > 0;
		return flag;
	}
	
}
