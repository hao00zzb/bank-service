package com.suntomor.bank.netpay.helper;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import com.bankcomm.netpay.Production;
import com.suntomor.bank.checking.model.AccountStatement;
import com.suntomor.bank.frame.common.GlobalConfigure;
import com.suntomor.bank.frame.common.ThreadLocalWrapper;
import com.suntomor.bank.frame.utils.PropertiesUtils;
import com.suntomor.bank.netpay.model.BocomB2CRequest;
import com.suntomor.bank.netpay.model.BocomB2CResponse;

public class BocomBankHelper {

	private static final Logger logger = LoggerFactory.getLogger(BocomBankHelper.class);
	private static String PFX_PASSWORD = null;
	private static String MER_ID = null;
	private static byte[] PFX_BYTE = null;
	
	private BocomBankHelper(){
		try {
			Properties properties = PropertiesUtils.getProperties("config/bankcomm.properties");
			PFX_PASSWORD = properties.getProperty("pfx.password");
			MER_ID = properties.getProperty("merID");
			
			InputStream is = new FileInputStream(properties.getProperty("pfx.path"));
			PFX_BYTE = IOUtils.toByteArray(is);
			logger.info("生产环境pfx商户证书读取成功!");
		} catch (Exception e) {
			logger.error("生产环境pfx商户证书读取失败!"+e.getMessage(), e);
		}
	}
	
	private static class SingletonHolder {
		private static final BocomBankHelper INSTANCE = new BocomBankHelper();
	}
	
	// 使用java实现模拟往https地址post报文 严禁在生产环境忽略https证书的错误
	@SuppressWarnings("restriction")
	public String post(String url, String param) {
		String response = "";
		try {
			HttpsURLConnection conn = (HttpsURLConnection) new URL(null, url, new sun.net.www.protocol.https.Handler()).openConnection();
			conn.setRequestMethod("POST");
			conn.setDoOutput(true);// 是否输入参数
			OutputStream outStream = conn.getOutputStream();
			outStream.write(param.getBytes("GBK"));// 输入参数
			outStream.close();
			InputStream inStream = conn.getInputStream();
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			int byteData = 0;
			while ((byteData = inStream.read()) != -1) {
				byteArray.write(byteData);
			}
			inStream.close();
			byte[] bytes = byteArray.toByteArray();
			byteArray.close();
			response = new String(bytes, "GBK");
		} catch (Exception e) {
			logger.error("生产环境后台POST报文失败:"+e.getMessage(), e);
		}
		return response;
	}


	public static final BocomBankHelper getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	public String detachedSign(String srcMsg) {
		return Production.detachedSign(srcMsg, PFX_BYTE, PFX_PASSWORD);
	}

	public boolean detachedVerify(String srcMsg, String signMsg) {
		return Production.detachedVerify(srcMsg, signMsg);
	}

	public String attachedSign(String srcMsg) {
		return Production.attachedSign(srcMsg, PFX_BYTE, PFX_PASSWORD);
	}

	public String attachedVerify(String signMsg) {
		return Production.attachedVerify(signMsg);
	}
	
	public String getPostURL() {
		return Production.getPostURL();
	}

	public String getApiURL() {
		return Production.getApiURL();
	}

	public String getOnekeyURL() {
		return Production.getOnekeyURL();
	}

	public String getMobileURL() {
		return Production.getMobileURL();
	}
	
	/**
	 * 初始化请求参数
	 * @param orderNumber
	 * @param amount
	 * @param description 
	 * @param isWap 
	 * @return {@code BocomB2CRequest}
	 */
	public BocomB2CRequest initRequest(String orderNumber, String amount, String description, boolean isWap) {
		String basePath = (String) ThreadLocalWrapper.get(GlobalConfigure.APPLICATION_CONTEXT_BASE_PATH);
		
		Date payTime = new Date();
		String orderDate = String.format("%1$tY%1$tm%1$td", payTime);
		String orderTime = String.format("%1$tH%1$tM%1$tS", payTime);

		BocomB2CRequest request = new BocomB2CRequest();
		request.setWap(isWap);
		request.setMerID(MER_ID);
		request.setOrderNumber(orderNumber);
		request.setOrderDate(orderDate);
		if (isWap) {
			boolean flag = makeMobileOrder(orderNumber, amount, description, payTime);
			if (flag) {
				request.setOrderUrl(Production.getMobileURL());
			}
			return request;
		}
		
		request.setOrderUrl(Production.getPostURL());
		request.setAmount(amount);
		request.setOrderTime(orderTime);
		request.setOrderMono("");
		request.setNotifyType("1");
		request.setMerURL(basePath+"/b2c/bocom/backnotify");
		request.setGoodsURL(basePath+"/b2c/bocom/frontnotify");
		request.setPayBatchNo(orderDate);
		request.setOrderContent(description);
		String sourceMsg = request.getInterfaceVersion() + "|" + request.getMerID() + "|" + orderNumber + "|" + request.getOrderDate()
				+ "|" + request.getOrderTime() + "|" + request.getTranType() + "|" + amount + "|" + request.getCurType() + "|" + request.getOrderContent() 
				+ "|" + request.getOrderMono() + "|" + request.getPhdFlag() + "|" + request.getNotifyType() + "|" + request.getMerURL() 
				+ "|" + request.getGoodsURL() + "|" + request.getJumpSeconds() + "|" + request.getPayBatchNo()
				+ "|" + request.getProxyMerName() + "|" + request.getProxyMerType() + "|" + request.getProxyMerCredentials() + "|" + request.getNetType();
		request.setMerSignMsg(detachedSign(sourceMsg));
		return request;
	}
	
	private boolean makeMobileOrder(String orderNumber, String amount, String orderContent, Date payTime) {
		String orderDate = String.format("%1$tY%1$tm%1$td", payTime);
		String orderTime = String.format("%1$tH%1$tM%1$tS", payTime);
		
		// 订单有效期为10分钟
		final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
		String period = dateFormat.format(DateUtils.addMonths(payTime, 10));
		String basePath = (String) ThreadLocalWrapper.get(GlobalConfigure.APPLICATION_CONTEXT_BASE_PATH);

		StringBuilder reqSrc = new StringBuilder("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		reqSrc.append("<Document>");
		reqSrc.append("<Head>");
		reqSrc.append("<TranCode>NPAY1001</TranCode>");
		reqSrc.append("<MerchNo>").append(MER_ID).append("</MerchNo>");
		reqSrc.append("</Head>");
		reqSrc.append("<Body>");
		reqSrc.append("<InterfaceVersion>1.0.0.0</InterfaceVersion>");
		reqSrc.append("<OrderNo>").append(orderNumber).append("</OrderNo>");
		reqSrc.append("<OrderDate>").append(orderDate).append("</OrderDate>");
		reqSrc.append("<OrderTime>").append(orderTime).append("</OrderTime>");
		reqSrc.append("<TranType>0</TranType>");
		reqSrc.append("<Amount>").append(amount).append("</Amount>");
		reqSrc.append("<CurType>CNY</CurType>");
		reqSrc.append("<OrderContent>").append(orderContent).append("</OrderContent>");
		reqSrc.append("<OrderMono>").append("</OrderMono>");
		reqSrc.append("<Period>").append(period).append("</Period>");
		reqSrc.append("<PhdFlag></PhdFlag>");
		reqSrc.append("<NotifyType>1</NotifyType>");
		reqSrc.append("<MerURL>").append(basePath + "/b2c/bocom/backnotify").append("</MerURL>");
		reqSrc.append("<GoodsURL>").append(basePath + "/b2c/bocom/frontnotify").append("</GoodsURL>");
		reqSrc.append("<JumpSeconds></JumpSeconds>");
		reqSrc.append("<PayBatchNo>").append(orderDate).append("</PayBatchNo>");
		reqSrc.append("<SndMerName>").append("").append("</SndMerName>");
		reqSrc.append("<SndMerType>").append("").append("</SndMerType>");
		reqSrc.append("<SndMerNo>").append("").append("</SndMerNo>");
		reqSrc.append("<NetType>0</NetType>");
		reqSrc.append("</Body>");
		reqSrc.append("</Document>");
		String reqSign = attachedSign(reqSrc.toString());
		String resSign = null;
		String xmlResponse = null;
		try {
			resSign = post(getApiURL(), "reqData=" + java.net.URLEncoder.encode(reqSign, "GBK"));
			xmlResponse = attachedVerify(resSign);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		logger.info(xmlResponse);
		if (StringUtils.isBlank(xmlResponse)) {
			return false;
		}
		
		SAXReader domReader = new SAXReader();
		BocomElementHandler elementHandler = new BocomElementHandler();
		Reader reader = null;
		try {
			reader = new StringReader(xmlResponse);
			domReader.addHandler(BocomElementHandler.PATH_HEAD, elementHandler);
			domReader.read(reader);
			reader.close();
			
			if (!elementHandler.isSuccess()) {
				logger.warn("生成手机订单错误,错误编码:{},错误信息:{}", elementHandler.getRspCode(), elementHandler.getRspMessage());
				return false;
			}
			return elementHandler.isSuccess();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return false;
	}
	
	
	/**
	 * 根据银行返回的结果初始化响应
	 * @param srcMsg
	 * @param signMsg
	 * @return {@code BocomB2CResponse}
	 */
	public BocomB2CResponse initResponse(String srcMsg, String signMsg) {
		Assert.notNull(srcMsg);
		
		String[] strs = srcMsg.split("\\|");
		if (strs.length != 17) {
			return null;
		}
		
		BocomB2CResponse response = new BocomB2CResponse();
		
		response.setMerID(strs[0]);
		response.setOrderNumber(strs[1]);
		response.setAmount(strs[2]);
		response.setCurType(strs[3]);
		response.setBankBatchNo(strs[4]);
		response.setMerBatchNo(strs[5]);
		response.setOrderDate(strs[6]);
		response.setOrderTime(strs[7]);
		response.setSerialNumber(strs[8]);
		response.setPaymentAccountType(strs[9]);
		response.setFeeAmount(strs[10]);
		response.setTradeStatus(strs[11]);
		response.setClientIp(strs[14]);
		
		return response;
	}
	
	/**
	 * 下载交通银行对账单
	 * @param settleDate 对账日期
	 * @return {@code List<AccountStatement>}
	 */
	public List<AccountStatement> downloadSettle(Date settleDate) {
		Assert.notNull(settleDate);
		
		StringBuilder reqSrc= new StringBuilder("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		reqSrc.append("<Document>");
		reqSrc.append("<Head>");
		reqSrc.append("<TranCode>NPAY1006</TranCode>");
		reqSrc.append("<MerchNo>"+MER_ID+"</MerchNo>");
		reqSrc.append("</Head>");
		reqSrc.append("<Body>");
		reqSrc.append("<SettleDate>").append(String.format("%1$tY%1$tm%1$td", settleDate)).append("</SettleDate>");
		reqSrc.append("</Body>");
		reqSrc.append("</Document>");
		
		String reqSign = attachedSign(reqSrc.toString());
		String resSign = null;
		try {
			resSign = post(getApiURL(),"reqData="+java.net.URLEncoder.encode(reqSign, "GBK"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
			
		}
		String xmlResponse = attachedVerify(resSign);
		logger.info(xmlResponse);
		//B2C对账单下载 响应的xml报文原文如下所示
		//（RspType为响应状态 N为正常 E为错误，RspDate为响应日期，RspTime为响应时间，TotalSum为作废字段，TotalCount为作废字段，TotalFee为作废字段，SettlementFile为对账单内容）：
		SAXReader domReader = new SAXReader();
		BocomElementHandler elementHandler = new BocomElementHandler();
		Reader reader = null;
		try {
			reader = new StringReader(xmlResponse);
			domReader.addHandler(BocomElementHandler.PATH_HEAD, elementHandler);
			domReader.addHandler(BocomElementHandler.PATH_BODY_SETTLEMENT_FILE, elementHandler);
			domReader.read(reader);
			reader.close();
			
			if (!elementHandler.isSuccess()) {
				logger.warn("下载对账单错误,错误编码:{},错误信息:{}", elementHandler.getRspCode(), elementHandler.getRspMessage());
				return null;
			}
			
			return elementHandler.getAccountStatements();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		
		return null;
	}
	
	/**
	 * <p>商户查询B2C订单状态</p>
	 * @param orderNo 交行B2C订单号
	 * @return {@code AccountStatement}
	 */
	public AccountStatement queryOrder(String orderNo) {
		Assert.notNull(orderNo);
		
		List<String> orderNos = new ArrayList<String>();
		orderNos.add(orderNo);
		
		List<AccountStatement> statements = queryOrder(orderNos);
		if (statements == null || statements.isEmpty()) {
			return null;
		}
		return statements.get(0);
	}
	
	/**
	 * <p>商户查询B2C订单状态</p>
	 * @param orderNo 交行B2C订单号
	 * @return {@code List<AccountStatement>}
	 */
	public List<AccountStatement> queryOrder(List<String> orderNos) {
		Assert.notEmpty(orderNos);
		
		StringBuilder reqSrc= new StringBuilder("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		reqSrc.append("<Document>");
		reqSrc.append("<Head>");
		reqSrc.append("<TranCode>NPAY1011</TranCode>");
		reqSrc.append("<MerchNo>").append(MER_ID).append("</MerchNo>");
		reqSrc.append("</Head>");
		reqSrc.append("<Body>");
		reqSrc.append("<OrderNos>").append(StringUtils.join(orderNos, ",")).append("</OrderNos>");
		reqSrc.append("</Body>");
		reqSrc.append("</Document>");
		
		String reqSign = attachedSign(reqSrc.toString());
		String resSign = null;
		try {
			resSign = post(getApiURL(), "reqData=" + java.net.URLEncoder.encode(reqSign, "GBK"));
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
		
		//B2C查询支付 响应的xml报文原文如下所示（RspType为响应状态 N为正常 E为错误，RspDate为响应日期，RspTime为响应时间，SerialNo为流水号，MerchNo为商户号，
		//OrderNo为订单号，Amount为交易金额，TranDate为交易日期，TranTime为交易时间，TranState为交易状态 0处理中 1成功 2失败 3异常 9未处理）
		String xmlResponse = attachedVerify(resSign);
		logger.info(xmlResponse);
		
		SAXReader domReader = new SAXReader();
		BocomElementHandler elementHandler = new BocomElementHandler();
		Reader reader = null;
		try {
			reader = new StringReader(xmlResponse);
			domReader.addHandler(BocomElementHandler.PATH_HEAD, elementHandler);
			domReader.addHandler(BocomElementHandler.PATH_BODY_ORDERLIST, elementHandler);
			domReader.read(reader);
			reader.close();
			
			if (!elementHandler.isSuccess()) {
				logger.warn("下载对账单错误,错误编码:{},错误信息:{}", elementHandler.getRspCode(), elementHandler.getRspMessage());
				return null;
			}
			return elementHandler.getAccountStatements();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(reader);
		}
		
		return null;
	}
}
