package com.suntomor.bank.checking.controller;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suntomor.bank.checking.job.CheckingABCBankStatement;
import com.suntomor.bank.checking.job.CheckingBocomBankStatement;
import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.checking.service.IAccountStatementService;
import com.suntomor.bank.frame.common.JSONResponse;
import com.suntomor.bank.frame.utils.JsonUtils;
import com.suntomor.bank.home.controller.BaseController;
import com.suntomor.bank.netpay.model.BankType;

@Controller
@RequestMapping(value = "/checking")
public class CheckController extends BaseController {

	private static final Logger logger = LoggerFactory.getLogger(CheckController.class);

	@Resource(name = "iAccountStatementService")
	private IAccountStatementService accountStatementService;
	
	@RequestMapping(value = "/download")
	public @ResponseBody JSONResponse download(@RequestParam String bankType, @RequestParam Date orderDate) {
		JSONResponse json = new JSONResponse();
		if (StringUtils.isBlank(bankType) || orderDate == null) {
			return json;
		}
		
		ExecutorService executorService = Executors.newCachedThreadPool();
		try {
			Future<List<AccountStatement>> future = null;
			if (BankType.ABC.getCode().equalsIgnoreCase(bankType)) {
				future = executorService.submit(new CheckingABCBankStatement(orderDate));
			} else if (BankType.BOCOM.getCode().equalsIgnoreCase(bankType)) {
				future = executorService.submit(new CheckingBocomBankStatement(orderDate));
			}
			
			List<AccountStatement> accountStatements = future.get();
			boolean flag = false;
			if (accountStatements != null && !accountStatements.isEmpty()) {
				flag = this.accountStatementService.saveOrUpdate(accountStatements, orderDate);
			}
			json.setData(JsonUtils.objToString(accountStatements));
			json.setMsg(flag?"对账单下载成功!":"对账单下载失败!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.setMsg(e.getMessage());
		} finally {
			executorService.shutdown();
		}
		
		return json;
	}
	
}
