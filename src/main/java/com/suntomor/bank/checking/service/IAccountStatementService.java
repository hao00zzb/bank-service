package com.suntomor.bank.checking.service;

import java.util.Date;
import java.util.List;

import com.suntomor.bank.checking.model.AccountStatement;

public interface IAccountStatementService {

	boolean saveOrUpdate(List<AccountStatement> accountStatements, Date checkDate);

	boolean saveOrUpdate(AccountStatement accountStatement);

	boolean saveOrUpdate(List<AccountStatement> allAccountStatements);

}
