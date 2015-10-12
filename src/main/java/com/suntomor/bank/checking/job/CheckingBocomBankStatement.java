package com.suntomor.bank.checking.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.netpay.helper.BocomBankHelper;

public class CheckingBocomBankStatement implements Callable<List<AccountStatement>> {
	
	private Date checkDate = null;
	
	public CheckingBocomBankStatement(Date checkDate) {
		this.checkDate = checkDate;
	}
	
	@Override
	public List<AccountStatement> call() throws Exception {
		final BocomBankHelper bocomHelper = BocomBankHelper.getInstance();
		
		List<AccountStatement> accountStatements = bocomHelper.downloadSettle(checkDate);
		
		return accountStatements;
	}

}
