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

import com.abc.trustpay.client.TrxException;
import com.suntomor.bank.frame.common.GlobalConfigure;
import com.suntomor.bank.frame.common.JSONResponse;
import com.suntomor.bank.frame.utils.JsonUtils;
import com.suntomor.bank.netpay.helper.AbcBankHelper;
import com.suntomor.bank.netpay.model.AbcB2CResponse;
import com.suntomor.bank.netpay.service.IOrderListService;

@Controller
@RequestMapping(value = "/b2c/abc")
public class AbcB2CPayController {

	private static final Logger logger = LoggerFactory.getLogger(AbcB2CPayController.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	@Resource
	private IOrderListService orderListService;
	
	@RequestMapping(value = "/backnotify")
	public void backNotify(Model model, HttpServletResponse response, @RequestParam(value="MSG") String notifyMsg) {
		logger.info("农业银行支付通知结果：{}", notifyMsg);
		JSONResponse json = new JSONResponse();
		
		AbcB2CResponse abcB2CResponse = null;
		try {
			abcB2CResponse = AbcBankHelper.getInstance().initResponse(notifyMsg);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if (abcB2CResponse == null || !abcB2CResponse.isSuccess()) {
			return;
		}
		// 更新订单为已交款状态
		try {
			String payTime = abcB2CResponse.getHostDate() + " " + abcB2CResponse.getHostTime();
			Date _payTime = new Date();
			if (payTime != null && payTime.trim().length() > 0) {
				_payTime = DATE_FORMAT.parse(payTime);
			}
			boolean flag = this.orderListService.orderSuccess(abcB2CResponse.getOrderNo(), abcB2CResponse.getAmount(), 
					_payTime, abcB2CResponse.getIrspRef());
			json.setSuccess(flag);
			json.setData(abcB2CResponse);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.setMsg(e.getMessage());
		}
		
		if (!json.isSuccess()) {
			logger.warn(JsonUtils.objToString(json));
			return;
		}
		
		StringBuilder result = new StringBuilder();
		result.append(GlobalConfigure.ORDER_RESULT_SUCCESS);
		result.append("&ordersn=").append(abcB2CResponse.getOrderNo());
		result.append("&sign=").append(DigestUtils.md5Hex(GlobalConfigure.SIGN_KEY + abcB2CResponse.getOrderNo()));
		
		try {
			response.sendRedirect(result.toString());
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		
	}
	
	@RequestMapping(value = "/frontnotify")
	public @ResponseBody JSONResponse frontNotify(Model model, @RequestParam(value="MSG") String notifyMsg) {
		logger.info("农业银行支付通知结果：{}", notifyMsg);
		JSONResponse json = new JSONResponse();
		
		AbcB2CResponse abcB2CResponse = null;
		try {
			abcB2CResponse = AbcBankHelper.getInstance().initResponse(notifyMsg);
		} catch (TrxException e) {
			logger.error(e.getMessage(), e);
		}
		if (abcB2CResponse == null || !abcB2CResponse.isSuccess()) {
			return json;
		}
		
		json.setSuccess(true);
		return json;
	}
	
}
