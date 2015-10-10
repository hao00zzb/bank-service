package com.suntomor.bank.netpay.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.abc.trustpay.client.TrxException;
import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.frame.common.GlobalConfigure;
import com.suntomor.bank.frame.common.ThreadLocalWrapper;
import com.suntomor.bank.frame.common.UserContext;
import com.suntomor.bank.netpay.model.AbcB2CRequest;
import com.suntomor.bank.netpay.model.AbcB2CResponse;
import com.suntomor.bank.netpay.model.BankType;

public class AbcBankHelper {

	private static Logger logger = LoggerFactory.getLogger(AbcBankHelper.class);
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
	
	private AbcBankHelper(){
	}
	
	private static class SingletonHolder {
		private static final AbcBankHelper INSTANCE = new AbcBankHelper();
	}

	public static final AbcBankHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	@SuppressWarnings("unchecked")
	public AbcB2CRequest initRequest(String orderNumber, String amount, String description, boolean isB2C, boolean isWap) {
		
		UserContext userContext = ThreadLocalWrapper.getUserContext();
		String basePath = (String) ThreadLocalWrapper.get(GlobalConfigure.APPLICATION_CONTEXT_BASE_PATH);
		
		Date date = new Date();
		String orderDesc = description;
		String orderDate = String.format("%1$tY/%1$tm/%1$td", date);
		String orderTime = String.format("%1$tH:%1$tM:%1$tS", date);
		String reciveFront = "";
		String resultNotifyURL = basePath + "/b2c/abc/backnotify";
		String buyIP = userContext.getIpAddress();
		
		//1、生成订单对象
		com.abc.trustpay.client.ebus.PaymentRequest tPaymentRequest = new com.abc.trustpay.client.ebus.PaymentRequest();
		tPaymentRequest.dicOrder.put("PayTypeID", "ImmediatePay");//设定交易类型
		tPaymentRequest.dicOrder.put("OrderDate", orderDate);//设定订单日期 （必要信息 - YYYY/MM/DD）
		tPaymentRequest.dicOrder.put("OrderTime", orderTime);//设定订单时间 （必要信息 - HH:MM:SS）
		tPaymentRequest.dicOrder.put("OrderNo", orderNumber);//设定订单编号 （必要信息）
		tPaymentRequest.dicOrder.put("CurrencyCode", "156");//设定交易币种
		tPaymentRequest.dicOrder.put("OrderAmount", amount);//设定交易金额
		tPaymentRequest.dicOrder.put("OrderDesc", orderDesc);//设定订单说明
		tPaymentRequest.dicOrder.put("OrderURL", reciveFront);//设定订单地址
		tPaymentRequest.dicOrder.put("InstallmentMark", "0");//分期标识
		tPaymentRequest.dicOrder.put("CommodityType", "0203");//设置商品种类
		tPaymentRequest.dicOrder.put("BuyIP", buyIP);//IP
		tPaymentRequest.dicOrder.put("ExpiredDate", "");//设定订单保存时间
		tPaymentRequest.dicOrder.put("orderTimeoutDate", "");//设定订单有效期
		tPaymentRequest.dicOrder.put("Fee", "");//设定手续费金额
		tPaymentRequest.dicOrder.put("ReceiverAddress", "");//收货地址

		//2、订单明细
		Map<String, Object> orderitem = new LinkedHashMap<String, Object>();
		orderitem.put("ProductName", orderDesc);
		orderitem.put("ProductType", "0203");
		tPaymentRequest.orderitems.put(1, orderitem);
		
		//3、生成支付请求对象
		String paymentType = isB2C?"1":"7";//1：农行卡支付、2：国际卡支付、3：农行贷记卡支付、5： 基于第三方的跨行支付、6：银联跨行支付、7、对公户、A: 支付方式合并
		tPaymentRequest.dicRequest.put("PaymentType", paymentType);//设定支付类型
		String paymentLinkType  = isWap?"2":"1";//1：internet网络接入 2：手机网络接入 3:数字电视网络接入 4:智能客户端
		tPaymentRequest.dicRequest.put("PaymentLinkType", paymentLinkType);//设定支付接入方式
		tPaymentRequest.dicRequest.put("NotifyType", "1");//设定通知方式
		tPaymentRequest.dicRequest.put("ResultNotifyURL", resultNotifyURL);//设定通知URL地址
		tPaymentRequest.dicRequest.put("IsBreakAccount", "0");//设定交易是否分账

		tPaymentRequest.dicRequest.put("ReceiveAccount", "");//设定收款方账号
		tPaymentRequest.dicRequest.put("ReceiveAccName", "");//设定收款方户名
		tPaymentRequest.dicRequest.put("MerchantRemarks", "");//设定附言
		tPaymentRequest.dicRequest.put("SplitAccTemplate", "");//分账模版编号        

		com.abc.trustpay.client.JSON paymentResponse = tPaymentRequest.postRequest();
		AbcB2CRequest request = new AbcB2CRequest();
		String returnCode = paymentResponse.GetKeyValue("ReturnCode");
		String errorMessage = paymentResponse.GetKeyValue("ErrorMessage");
		request.setReturnCode(returnCode);
		request.setErrorMessage(errorMessage);
		if ("0000".equals(returnCode)) {
			String paymentUrl = paymentResponse.GetKeyValue("PaymentURL");
			request.setPaymentUrl(paymentUrl);
		}
		return request;
	}
	
	public AbcB2CResponse initResponse(String notifyMsg) throws TrxException {
		AbcB2CResponse response = new AbcB2CResponse();
		
		com.abc.trustpay.client.ebus.PaymentResult result = new com.abc.trustpay.client.ebus.PaymentResult(notifyMsg);
		response.setSuccess(result.isSuccess());
		response.setReturnCode(result.getReturnCode());
		response.setErrorMessage(result.getErrorMessage());
		response.setOrderNo(result.getValue("OrderNo"));
		response.setAmount(Double.valueOf(result.getValue("Amount")));
		response.setBatchNo(result.getValue("BatchNo"));
		response.setVoucherNo(result.getValue("VoucherNo"));
		response.setIrspRef(result.getValue("iRspRef"));
		response.setHostDate(result.getValue("HostDate"));
		response.setHostTime(result.getValue("HostTime"));
		
		return response;
	}
	
	/**
	 * 下载对账单
	 * @param settleDate
	 * @return {@code List<AccountStatement>}
	 */
	public List<AccountStatement> downloadSettle(Date settleDate) {
		Assert.notNull(settleDate);
		
        //1、取得商户对账单下载所需要的信息
		//对账日期YYYY/MM/DD （必要信息）
        String tSettleDate = dateFormat.format(settleDate);
        //1：压缩，0：不压缩
        String tZIP = "0";
        logger.info("农行B2C对账时间:{},数据是否压缩:{}", tSettleDate, tZIP);
            
        //2、生成商户对账单下载请求对象
        com.abc.trustpay.client.ebus.SettleRequest tRequest = new com.abc.trustpay.client.ebus. SettleRequest();
        tRequest.dicRequest.put("SettleDate",tSettleDate);  //对账日期YYYY/MM/DD （必要信息）
        tRequest.dicRequest.put("ZIP",tZIP);

        //3、传送商户对账单下载请求并取得对账单
        com.abc.trustpay.client.JSON json = tRequest.postRequest();
        
        //4、判断商户对账单下载结果状态，进行后续操作
        String returnCode = json.GetKeyValue("ReturnCode");
        String errorMessage = json.GetKeyValue("ErrorMessage");
        String detailRecords = json.GetKeyValue("DetailRecords");
        if (!returnCode.equals("0000")) {
            //6、商户账单下载失败
        	logger.warn("商户账单下载失败,ReturnCode=[{}], ErrorMessage=[{}]", returnCode, errorMessage);
        	return null;
        }
        //5、商户对账单下载成功，生成对账单对象
        logger.info("ReturnCode = [" + returnCode + "]");
        logger.info("ErrorMessage = [" + errorMessage + "]");
        
    	//6、取得对账单明细，每个字段以|分隔，每条记录以^^为分隔.
    	String[] records = detailRecords.split("\\^\\^");
    	int index = 0;
    	List<String> _records = new ArrayList<String>();
    	for (String record : records) {
    		++index;
    		logger.info("农行B2C对账信息:{}", record);
    		if (index == 1) {
    			continue;
    		}
    		_records.add(record);
        }
    	
    	List<AccountStatement> accountStatements = generateDetails(_records.toArray(new String[_records.size()]));
    	return accountStatements;
	}
	
	/**
	 * <p>
	 * 使用交易对账单对象的getDetailRecords()方法取得交易明细tRecords，每笔交易明细为字符串类型，使用逗号分隔不同的字段。字段信息如下： 
	 * </p>
	 * <p>商户号|交易类型|订单编号|交易时间|交易金额|商户账号|商户动账金额|客户账号|账户类型|商户回佣手续费|商户分期手续费|会计日期|主机流水号|9014流水号|原订单号</p> 
	 * 例如：{@code 103881072990001|Sale|1000072295|20150526113222|0.01|720101040217999|0.01|6228480428940601977|401|0.00|0.00|20150526|382197316|9015052611322302569|}
	 * @param records
	 * @return
	 */
	private List<AccountStatement> generateDetails(String[] records) {
		List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		for (String record : records) {
			if (StringUtils.isBlank(record)) {
				continue;
			}
			String[] details = record.split("\\|");
			// 交易类型
			String tradeType = details[1];
			if (!(tradeType.equalsIgnoreCase("Sale"))) {
				continue;
			}
			
			String orderNumber = null;
			Double moneyAmount = null;
			try {
				// 订单编号
				orderNumber = details[2];
				// 交易金额
				moneyAmount = Double.valueOf(details[4]);
			} catch (NumberFormatException e) {
				logger.error(e.getMessage(), e);
			}
			// 交易流水号
			// String bankSerailNumber = details[3]; 
			// 交易时间
			Date payTime = null;
			try {
				payTime = dateFormat.parse(details[3]);
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
			
			AccountStatement accountStatement = new AccountStatement();
			accountStatement.setBankType(BankType.ABC.getCode());
			accountStatement.setTotalMoney(moneyAmount);
			accountStatement.setOrderNumber(orderNumber);
			accountStatement.setPayTime(payTime);
			accountStatement.setSerialNumber(details[12]+":"+details[13]);
			accountStatements.add(accountStatement);
		}
		return accountStatements;
	}
	
	/**
	 * <p>商户查询B2C订单状态</p>
	 * @param orderNo 农行B2C订单号
	 * @return {@code AccountStatement}
	 */
	@SuppressWarnings("unchecked")
	public AccountStatement queryOrder(String orderNo) {
		if (orderNo == null) {
			return null;
		}
		
		//1、生成交易查询对象
		String payTypeID = "ImmediatePay";
		String queryTpye = "false";

		com.abc.trustpay.client.ebus.QueryOrderRequest tQueryRequest = new com.abc.trustpay.client.ebus.QueryOrderRequest();
		tQueryRequest.queryRequest.put("PayTypeID", payTypeID);//设定交易类型
		tQueryRequest.queryRequest.put("OrderNo", orderNo.toString());//设定订单编号 （必要信息）
		tQueryRequest.queryRequest.put("QueryDetail", queryTpye);//设定查询方式
		com.abc.trustpay.client.JSON json = tQueryRequest.postRequest();

		String returnCode = json.GetKeyValue("ReturnCode");
		String errorMessage = json.GetKeyValue("ErrorMessage");
		
		if (!returnCode.equals("0000")) {
			//6、商户订单查询失败
			logger.warn("商户查询账单失败,ReturnCode=[{}], ErrorMessage=[{}]", returnCode, errorMessage);
			return null;
		}
		//2、获取结果信息
		String orderInfo = json.GetKeyValue("Order");
        if (StringUtils.isBlank(orderInfo)) {
        	logger.warn("查询订单:{}结果为空!", orderNo);
        	return null;
        }
        
        //3、还原经过base64编码的信息 
        String orderDetail = new String(Base64.decodeBase64(orderInfo));
        json.setJsonString(orderDetail);
        logger.info("订单明细:" + orderDetail);

        String _orderNo = json.GetKeyValue("OrderNo");
		String orderDate = json.GetKeyValue("OrderDate");
		String orderTime = json.GetKeyValue("OrderTime");
		String orderAmount = json.GetKeyValue("OrderAmount");
		String orderStatus = json.GetKeyValue("Status");
        
        //6、判断订单是否支付成功
        //订单状态：01:未支付、02:无回应、03:已请款、04:成功、05:已退款、07:授权确认成功、00:授权已取消、99:失败
		if (!"04".equals(orderStatus)) {
			logger.info("订单:{},支付失败:{}.", orderNo, orderStatus);
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String payTime = orderDate + " " + orderTime;
        Date _payTime = null;
		
        try {
        	_payTime = dateFormat.parse(payTime);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
        
        Double _orderAmount = 0.0;
        try {
        	_orderAmount = Double.valueOf(orderAmount);
		} catch (Exception e) {
			logger.error("金额转换失败!"+e.getMessage(), e);
			return null;
		}
        
		AccountStatement accountStatement = new AccountStatement();
		accountStatement.setBankType(BankType.ABC.getCode());
		accountStatement.setTotalMoney(_orderAmount);
		accountStatement.setOrderNumber(_orderNo);
		accountStatement.setPayTime(_payTime);
        
		return accountStatement;
	}

}
