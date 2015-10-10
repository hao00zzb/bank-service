package com.suntomor.bank.checking.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.suntomor.bank.checking.dao.IAccountStatementDAO;
import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.checking.service.IAccountStatementService;
import com.suntomor.bank.netpay.model.OrderList;
import com.suntomor.bank.netpay.service.IOrderListService;

@Service("iAccountStatementService")
public class AccountStatementServiceImpl implements IAccountStatementService {

	@Resource(name = "iAccountStatementDAO")
	private IAccountStatementDAO accountStatementDAO;
	@Resource(name = "iOrderListService")
	private IOrderListService orderListService;
	
	@Override
	public boolean saveOrUpdate(List<AccountStatement> allAccountStatements, Date checkDate) {
		if (allAccountStatements == null || allAccountStatements.isEmpty()) {
			return false;
		}
		
		boolean flag = true;
		for (AccountStatement accountStatement : allAccountStatements) {
			flag &= this.saveOrUpdate(accountStatement);
		}
		return flag;
	}
	
	@Override
	public boolean saveOrUpdate(AccountStatement accountStatement) {
		Assert.notNull(accountStatement);
		
		String orderNumber = accountStatement.getOrderNumber();
		if (orderNumber != null) {
			//orderNumber是否数据库中已经存在,
			Map<String, Object> parameter = new HashMap<String,Object>();
			parameter.put("orderNumber", orderNumber);
			AccountStatement _accountStatement = accountStatementDAO.queryByParameterOne(parameter);
			if (_accountStatement != null) {
				return true;
			}
		}
		
		OrderList orderList = this.orderListService.query(orderNumber);
		if (orderList != null) {
			accountStatement.setOrderId(orderList.getId());
		}
		accountStatement.setCreateTime(new Date());
		return this.accountStatementDAO.insertByEntity(accountStatement) > 0;
	}

	@Override
	public boolean saveOrUpdate(List<AccountStatement> allAccountStatements) {
		if (allAccountStatements == null || allAccountStatements.isEmpty()) {
			return false;
		}
		boolean flag = true;
		for (AccountStatement accountStatement : allAccountStatements) {
			flag &= this.saveOrUpdate(accountStatement);
		}
		return flag;
	}

}
