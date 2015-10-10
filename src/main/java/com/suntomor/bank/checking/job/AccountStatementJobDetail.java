package com.suntomor.bank.checking.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.checking.service.IAccountStatementService;
import com.suntomor.bank.netpay.helper.AbcBankHelper;
import com.suntomor.bank.netpay.helper.BocomBankHelper;

public class AccountStatementJobDetail {

	@Resource(name = "iAccountStatementService")
	private IAccountStatementService accountStatementService;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountStatementJobDetail.class);
	
	public void execute() {
		try {
			ExecutorService pool = Executors.newCachedThreadPool();
			
			Date checkDate = DateUtils.addDays(new Date(), -1);
			Runnable abcThread = new CheckingABCBankStatement(checkDate);
			pool.execute(abcThread);
			
			Runnable bocomThread = new CheckingBocomBankStatement(checkDate);
			pool.execute(bocomThread);
			
			//关闭线程池
			pool.shutdown();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	private class CheckingABCBankStatement implements Runnable {

		private Date checkDate = null;
		
		public CheckingABCBankStatement(Date checkDate) {
			this.checkDate = checkDate;
		}

		@Override
		public void run() {
			final AbcBankHelper abcHelper = AbcBankHelper.getInstance();
			
			List<AccountStatement> accountStatements = abcHelper.downloadSettle(checkDate);
			if (accountStatements != null && !accountStatements.isEmpty()) {
				accountStatementService.saveOrUpdate(accountStatements, checkDate);
			}
		}
		
	}
	
	private class CheckingBocomBankStatement implements Runnable {

		private Date checkDate = null;
		
		public CheckingBocomBankStatement(Date checkDate) {
			this.checkDate = checkDate;
		}
		
		@Override
		public void run() {
			final BocomBankHelper bocomHelper = BocomBankHelper.getInstance();
			
			List<AccountStatement> accountStatements = bocomHelper.downloadSettle(checkDate);
			if (accountStatements != null && !accountStatements.isEmpty()) {
				accountStatementService.saveOrUpdate(accountStatements, checkDate);
			}
		}
		
	}
	
}
