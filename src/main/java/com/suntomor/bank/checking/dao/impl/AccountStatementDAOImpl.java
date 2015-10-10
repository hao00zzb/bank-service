package com.suntomor.bank.checking.dao.impl;

import org.springframework.stereotype.Repository;

import com.suntomor.bank.checking.dao.IAccountStatementDAO;
import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.frame.dao.BaseDAOImpl;

@Repository("iAccountStatementDAO")
public class AccountStatementDAOImpl extends BaseDAOImpl<AccountStatement, Integer> implements IAccountStatementDAO {

	public AccountStatementDAOImpl() {
		super(AccountStatement.class);
	}
	
}
