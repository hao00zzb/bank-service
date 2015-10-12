package com.suntomor.bank.checking.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.checking.service.IAccountStatementService;

public class AccountStatementJobDetail {

	@Resource(name = "iAccountStatementService")
	private IAccountStatementService accountStatementService;
	
	private static final Logger logger = LoggerFactory.getLogger(AccountStatementJobDetail.class);
	
	public void execute() {
		ExecutorService executorService = Executors.newCachedThreadPool();
		List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
		try {
			Date checkDate = DateUtils.addDays(new Date(), -1);
			Future<List<AccountStatement>> abcFuture = executorService.submit(new CheckingABCBankStatement(checkDate));
			Future<List<AccountStatement>> bocomFuture = executorService.submit(new CheckingBocomBankStatement(checkDate));
			
			accountStatements.addAll(abcFuture.get());
			accountStatements.addAll(bocomFuture.get());
			
			this.accountStatementService.saveOrUpdate(accountStatements, checkDate);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			//关闭线程池
			executorService.shutdown();
		}
		
	}
	
}
