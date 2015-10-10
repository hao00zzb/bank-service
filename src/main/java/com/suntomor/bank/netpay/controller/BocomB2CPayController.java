package com.suntomor.bank.netpay.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suntomor.bank.frame.common.GlobalConfigure;
import com.suntomor.bank.frame.common.JSONResponse;
import com.suntomor.bank.frame.utils.JsonUtils;
import com.suntomor.bank.netpay.helper.BocomBankHelper;
import com.suntomor.bank.netpay.model.BocomB2CResponse;
import com.suntomor.bank.netpay.service.IOrderListService;

@Controller
@RequestMapping(value = "/b2c/bocom")
public class BocomB2CPayController {

	private static final Logger logger = LoggerFactory.getLogger(BocomB2CPayController.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Resource
	private IOrderListService orderListService;
	
	/**
	 * 接收交通银行支付通知
	 * @param model
	 * @param notifyMsg
	 * @return
	 */
	@RequestMapping(value = "/backnotify")
	public @ResponseBody JSONResponse backNotify(Model model, @RequestParam(value="notifyMsg") String notifyMsg) {
		logger.info("交通银行支付通知结果：{}", notifyMsg);
		//获取签名信息
		String signMsg = notifyMsg.substring( notifyMsg.lastIndexOf("|") + "|".length() , notifyMsg.length()); 
		String srcMsg = notifyMsg.substring(0,  notifyMsg.lastIndexOf("|") + "|".length() );
		
		JSONResponse json = new JSONResponse();
		json.setData(srcMsg);
		
		// 进行验签
		boolean flag = BocomBankHelper.getInstance().detachedVerify(srcMsg, signMsg);
		if(!flag) {
			logger.error("交通银行验签失败!");
			json.setMsg("交通银行验签失败!");
			return json;
		}
		json.setSuccess(flag);
		//商户号|订单号|订单金额|交易币种|银行批次号|商户批次号|交易日期|交易时间|交易流水号|交易状态 0处理中 1成功 2失败 3异常 9未处理|手续费金额|付款账号类型 0借记卡 2贷记卡 3他行银联卡|银行备注|本字段作废|付款方IP|付款页面来源|经过base64处理的商户备注|签名
		//301320580999500|1435570377327|0.01|CNY|20150629|20150629|20150629|175634|E9FDA765|1|0.00|0| | |106.39.255.109|42.121.12.97| |
		BocomB2CResponse response = BocomBankHelper.getInstance().initResponse(srcMsg, signMsg);
		if (!response.isSuccess()) {
			json.setMsg("交易失败，交易状态：" + response.getTradeStatus());
			return json;
		}
		json.setData(response);
		json.setSuccess(response.isSuccess());
		return json;
	}
	
	/**
	 * 接收交通银行支付通知
	 * @param model
	 * @param notifyMsg
	 * @return
	 */
	@RequestMapping(value = "/frontnotify")
	public void frontNotify(Model model, HttpServletResponse response, @RequestParam(value="notifyMsg") String notifyMsg) {
		logger.info("交通银行支付通知结果：{}", notifyMsg);
		//获取签名信息
		String signMsg = notifyMsg.substring( notifyMsg.lastIndexOf("|") + "|".length() , notifyMsg.length()); 
		String srcMsg = notifyMsg.substring(0,  notifyMsg.lastIndexOf("|") + "|".length() );
		
		JSONResponse json = new JSONResponse();
		json.setData(srcMsg);
		
		// 进行验签
		boolean flag = BocomBankHelper.getInstance().detachedVerify(srcMsg, signMsg);
		if(!flag) {
			logger.error("交通银行验签失败!");
			json.setMsg("交通银行验签失败!");
			return;
		}
		json.setSuccess(flag);
		//商户号|订单号|订单金额|交易币种|银行批次号|商户批次号|交易日期|交易时间|交易流水号|交易状态 0处理中 1成功 2失败 3异常 9未处理|手续费金额|付款账号类型 0借记卡 2贷记卡 3他行银联卡|银行备注|本字段作废|付款方IP|付款页面来源|经过base64处理的商户备注|签名
		//301320580999500|1435570377327|0.01|CNY|20150629|20150629|20150629|175634|E9FDA765|1|0.00|0| | |106.39.255.109|42.121.12.97| |
		BocomB2CResponse bocomB2CResponse = BocomBankHelper.getInstance().initResponse(srcMsg, signMsg);
		if (!bocomB2CResponse.isSuccess()) {
			json.setMsg("交易失败，交易状态：" + bocomB2CResponse.getTradeStatus());
			return;
		}
		try {
			Double amount = Double.valueOf(bocomB2CResponse.getAmount());
			String payTime = bocomB2CResponse.getOrderDate() + bocomB2CResponse.getOrderTime();
			Date _payTime = new Date();
			if (payTime != null) {
				_payTime = DATE_FORMAT.parse(payTime);
			}
			flag = this.orderListService.orderSuccess(bocomB2CResponse.getOrderNumber(), amount, _payTime, bocomB2CResponse.getSerialNumber());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		json.setData(bocomB2CResponse);
		json.setSuccess(bocomB2CResponse.isSuccess());
		
		if (!json.isSuccess()) {
			logger.warn(JsonUtils.objToString(json));
			return;
		}
		
		StringBuilder result = new StringBuilder();
		result.append(GlobalConfigure.ORDER_RESULT_SUCCESS);
		result.append("&ordersn=").append(bocomB2CResponse.getOrderNumber());
		result.append("&sign=").append(DigestUtils.md5Hex(GlobalConfigure.SIGN_KEY + bocomB2CResponse.getOrderNumber()));
		
		try {
			response.sendRedirect(result.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
		
	}
	
}
