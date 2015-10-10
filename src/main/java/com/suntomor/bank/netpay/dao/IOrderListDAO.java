package com.suntomor.bank.netpay.dao;

import com.suntomor.bank.frame.dao.IBaseDAO;
import com.suntomor.bank.netpay.model.OrderList;

public interface IOrderListDAO extends IBaseDAO<OrderList, Integer> {

	int saveOrUpdate(OrderList orderList);
	
}
