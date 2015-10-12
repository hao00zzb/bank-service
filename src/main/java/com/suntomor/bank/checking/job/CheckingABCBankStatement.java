package com.suntomor.bank.checking.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.netpay.helper.AbcBankHelper;

public class CheckingABCBankStatement implements Callable<List<AccountStatement>> {

	private Date checkDate = null;
	
	public CheckingABCBankStatement(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Override
	public List<AccountStatement> call() throws Exception {
		final AbcBankHelper abcHelper = AbcBankHelper.getInstance();
		
		List<AccountStatement> accountStatements = abcHelper.downloadSettle(checkDate);
		
		return accountStatements;
	}

}
