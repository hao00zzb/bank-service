package com.suntomor.bank.netpay.controller;

import javax.annotation.Resource;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.frame.common.GlobalConfigure;
import com.suntomor.bank.frame.common.JSONResponse;
import com.suntomor.bank.netpay.constants.OrderListCons;
import com.suntomor.bank.netpay.helper.AbcBankHelper;
import com.suntomor.bank.netpay.helper.BocomBankHelper;
import com.suntomor.bank.netpay.model.AbcB2CRequest;
import com.suntomor.bank.netpay.model.BankType;
import com.suntomor.bank.netpay.model.BocomB2CRequest;
import com.suntomor.bank.netpay.model.OrderList;
import com.suntomor.bank.netpay.service.IOrderListService;

@Controller
public class BankPayController {

	private static final Logger logger = LoggerFactory.getLogger(BankPayController.class);
	
	@Resource(name = "iOrderListService")
	private IOrderListService orderListService;
	
	@RequestMapping(value = "/order/make")
	public @ResponseBody JSONResponse makeOrder(OrderList orderList, Integer iswap) {
		JSONResponse json = new JSONResponse();
		if (orderList == null) {
			json.setMsg("缺少参数!");
			return json;
		}
		
		orderList.setWap(iswap != null ? iswap.equals(1) : false);
		Integer orderType = orderList.getOrderType() == null ? -1 : orderList.getOrderType();
		String description = StringUtils.defaultIfBlank(OrderListCons.ORDER_TYPE_MAP.get(orderType), "其他费用");
		orderList.setOrderTypeName(description);
		
		try {
			boolean flag = this.orderListService.saveOrUpdate(orderList);
			json.setSuccess(flag);
			json.setMsg(flag?"订单生成成功!":"订单生成失败!");
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			json.setMsg(e.getMessage());
		}
		
		return json;
	}
	
	@RequestMapping(value = "/pay")
	public String pay(Model model, @RequestParam String bankType, 
				@RequestParam String orderNumber, @RequestParam("amount") String totalMoney, Integer iswap, 
				@RequestParam String sign) {
		
		if (StringUtils.isBlank(bankType) || StringUtils.isBlank(orderNumber) || StringUtils.isBlank(sign)) {
			model.addAttribute("errorMsg", "参数信息不完整!");
			logger.error("参数信息不完整,bankType:{}, orderNumber:{}, totalMoney:{}, sign:{}", bankType, orderNumber, totalMoney, sign);
			return "error";
		}
		// 验证MD5签名
		StringBuilder builder = new StringBuilder();
		builder.append(bankType).append(orderNumber).append(totalMoney).append(GlobalConfigure.SIGN_KEY);
		if (!DigestUtils.md5Hex(builder.toString()).equalsIgnoreCase(sign)) {
			model.addAttribute("errorMsg", "签名签证失败!");
			logger.error("签名签证失败!");
			return "error";
		}
		
		OrderList order = this.orderListService.query(orderNumber);
		if (order == null) {
			model.addAttribute("errorMsg", "订单不存在!");
			logger.error("订单不存在!");
			return "error";
		}
		if (!order.getStatus().equals(OrderListCons.STATUS_FAILURE)) {
			model.addAttribute("errorMsg", "订单已支付或已取消!");
			logger.error("订单不存在!");
			return "error";
		}
//		Integer orderType = order.getOrderType() == null ? -1 : order.getOrderType();
		String description = StringUtils.defaultIfBlank(order.getOrderTypeName(), "其他费用");
		
		boolean isWap = iswap != null ? iswap.equals(1) : false;
		String returnUrl = null;
		if (BankType.ABC.getCode().equals(bankType)) {
			AbcB2CRequest abcB2CRequest = AbcBankHelper.getInstance().initRequest(orderNumber, totalMoney, description, true, isWap);
			model.addAttribute("abcB2CRequest", abcB2CRequest);
			returnUrl = "b2c/abcPay";
		} else if (BankType.BOCOM.getCode().equals(bankType)) {
			BocomB2CRequest bocomB2CRequest = BocomBankHelper.getInstance().initRequest(orderNumber, totalMoney, description, isWap);
			model.addAttribute("bocomB2CRequest", bocomB2CRequest);
			returnUrl = "b2c/bocomPay";
		}
		
		return returnUrl;
	}
	
	@RequestMapping(value = "/query/order")
	public @ResponseBody JSONResponse queryOrder(String bankType, String orderNumber, String sign) {
		JSONResponse json = new JSONResponse();
		
		if (StringUtils.isBlank(bankType) || StringUtils.isBlank(orderNumber) || StringUtils.isBlank(sign)) {
			json.setMsg("参数信息不完整,bankType:{"+bankType+"}, orderNumber:{"+orderNumber+"}, sign:{"+sign+"}");
			return json;
		}
		// 验证MD5签名
		StringBuilder builder = new StringBuilder();
		builder.append(bankType).append(orderNumber).append(GlobalConfigure.SIGN_KEY);
		if (!DigestUtils.md5Hex(builder.toString()).equalsIgnoreCase(sign)) {
			json.setMsg("签名签证失败!");
			return json;
		}
		
		OrderList order = this.orderListService.query(orderNumber);
		if (order == null) {
			json.setMsg("订单不存在!");
			return json;
		}
		AccountStatement accountStatement = null;
		if (BankType.ABC.getCode().equals(bankType)) {
			accountStatement = AbcBankHelper.getInstance().queryOrder(order.getOrderNumber());
		} else if (BankType.BOCOM.getCode().equals(bankType)) {
			accountStatement = BocomBankHelper.getInstance().queryOrder(order.getOrderNumber());
		}

		json.setSuccess(accountStatement != null);
		json.setData(accountStatement);

		return json;
	}
	
}
