package com.suntomor.bank.netpay.dao.impl;

import org.springframework.stereotype.Repository;

import com.suntomor.bank.frame.dao.BaseDAOImpl;
import com.suntomor.bank.netpay.dao.IOrderListDAO;
import com.suntomor.bank.netpay.model.OrderList;

@Repository("iOrderListDAO")
public class OrderListDAOImpl extends BaseDAOImpl<OrderList, Integer> implements IOrderListDAO {

	public OrderListDAOImpl() {
		super(OrderList.class);
	}

	@Override
	public int saveOrUpdate(OrderList orderList) {
		if (orderList.getId() == null) {
			return super.insertByEntity(orderList);
		}

		return super.updateByEntity(orderList);
	}

}
