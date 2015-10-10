package com.suntomor.bank.netpay.constants;

import java.util.HashMap;
import java.util.Map;

public class OrderListCons {

	/** 未支付 **/
	public static final Integer STATUS_FAILURE = 0;
	/** 已支付 **/
	public static final Integer STATUS_SUCCESS = 1;
	/** 已取消 **/
	public static final Integer STATUS_CANCEL = 2;
	
	/** 住院费 **/
	public static final Integer TYPE_HOSPITAL = 1;
	/** 体检费 **/
	public static final Integer TYPE_EXAMINATION = 2;
	
	public static final Map<Integer, String> ORDER_TYPE_MAP = new HashMap<Integer, String>();
	static {
		ORDER_TYPE_MAP.put(TYPE_HOSPITAL, "住院费");
		ORDER_TYPE_MAP.put(TYPE_EXAMINATION, "体检费");
		ORDER_TYPE_MAP.put(-1, "其他费用");
	}
	
}
