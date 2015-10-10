package com.suntomor.bank.netpay.helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.netpay.model.BankType;

public class BocomElementHandler implements ElementHandler {

	private boolean success = false;
	private String rspCode = null;
	private String rspMessage = null;
	private List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
	
	private static final Logger logger = LoggerFactory.getLogger(BocomElementHandler.class);
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
	public static final String PATH_HEAD = "/Document/Head";
	/** 对账交易 **/
	public static final String PATH_BODY_SETTLEMENT_FILE = "/Document/Body/SettlementFile";
	/** 支付查询 **/
	public static final String PATH_BODY_ORDERLIST = "/Document/Body/OrderList";
	
	public BocomElementHandler() {
	}
	
	@Override
	public void onStart(ElementPath elementPath) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onEnd(ElementPath elementPath) {
		Element current = elementPath.getCurrent();
		
		if (current.getPath().equals(PATH_HEAD)){
			Element rspTypeEle = current.element("RspType");
			this.success = rspTypeEle.getText().equals("N");
			if (!this.success) {
				this.rspCode = current.element("RspCode").getTextTrim();
				this.rspMessage = current.element("RspMsg").getTextTrim();
			}
			return;
		}
		// 对账XML文件解析
		if (current.getPath().equals(PATH_BODY_SETTLEMENT_FILE)) {
			String fileContent = current.getText();
			String[] contents = fileContent.split("\r\n|\n");
			int index = -1;
			int startIndex = -1;
			int endIndex = -1;
			// 找出数据开始下标和结束下标
			for (String content : contents) {
				index++;
				if (content.trim().length() == 0) {
					continue;
				}
				if (content.equals("消费")) {
					startIndex = index + 4;
				}
				if (content.equals("退货")) {
					endIndex = index - 4;
				}
			}
			accountStatements.addAll(generateDetails(startIndex, endIndex, contents));
		}
		// 订单查询XML
		if (current.getPath().equals(PATH_BODY_ORDERLIST)) {
			List<Element> orderElements = current.elements("Order");
			for (Element orderElement : orderElements) {
				AccountStatement statement = new AccountStatement();
				statement.setBankType(BankType.BOCOM.getCode());
				statement.setOrderNumber(orderElement.elementText("OrderNo"));

				//TranState为交易状态 0处理中 1成功 2失败 3异常 9未处理
				String tranState = orderElement.elementText("TranState");
				if(!tranState.equals("1")){
					logger.warn("订单:{}处理结果:{}[0处理中 1成功 2失败 3异常 9未处理].", statement.getOrderNumber(), tranState);
					continue;
				}
				
				Double moneyAmount = 0.0;
				try {
					moneyAmount = Double.valueOf(orderElement.elementText("Amount"));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				statement.setTotalMoney(moneyAmount);
				
				String payTime = orderElement.elementText("TranDate") + "-" + orderElement.elementText("TranTime");
				try {
					statement.setPayTime(DATE_FORMAT.parse(payTime));
				} catch (Exception e) {
					logger.error(e.getMessage(), e);
				}
				
				accountStatements.add(statement);
			}
		}
	}
	
	private List<AccountStatement> generateDetails(int startIndex, int endIndex, String[] contents) {
		List<AccountStatement> accountStatements = new ArrayList<AccountStatement>();
		for (int i = startIndex; i < endIndex; i++) {
			String[] detailContent = contents[i].replaceAll("\\ +", "\\|").split("\\|");
			logger.info("记录：{}", Arrays.asList(detailContent));
			if (detailContent.length != 10) {
				continue;
			}
			AccountStatement statement = new AccountStatement();
			statement.setBankType(BankType.BOCOM.getCode());
			statement.setOrderNumber(detailContent[1]);
			try {
				statement.setPayTime(DATE_FORMAT.parse(detailContent[3]));
			} catch (ParseException e) {
				logger.error(e.getMessage(), e);
			}
			statement.setSerialNumber(detailContent[4]);
			statement.setTotalMoney(Double.valueOf(detailContent[8]));
			
			accountStatements.add(statement);
		}
		return accountStatements;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getRspCode() {
		return rspCode;
	}

	public String getRspMessage() {
		return rspMessage;
	}

	public List<AccountStatement> getAccountStatements() {
		return accountStatements;
	}

}
