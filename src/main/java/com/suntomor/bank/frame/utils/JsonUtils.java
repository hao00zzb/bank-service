package com.suntomor.bank.frame.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	
	private static Logger logger = LoggerFactory.getLogger(JsonUtils.class);
	
	/**
	 * 将bean、List、Map、Array转成Json字符串
	 * @param obj bean、List、Map、Array
	 * @return json 字符串
	 */
	public static String objToString(Object obj){
		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(obj);
		} catch (Exception e) {
			logger.error("生成JSON字符串出错"+obj.getClass().getName(),e);
			json = "";
		}
		return json;
	}
	
	/**
	 * 将转成Json字符串转换成相应的Class对象
	 * @param json
	 * @param clazz
	 */
	public static <T> T stringToObj(String json, Class<T> clazz){
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = new JsonFactory(mapper);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		try {
			JsonParser jsonParser = factory.createParser(json);
			return jsonParser.readValueAs(clazz);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * 将转成Json字符串转换成相应的TypeReference对象
	 * @param json
	 * @param typeReference
	 * @return
	 */
	public static <T> T stringToObj(String json, TypeReference<T> typeReference){
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
		JsonFactory factory = new JsonFactory(mapper);
		try {
			JsonParser jsonParser = factory.createParser(json);
			return jsonParser.readValueAs(typeReference);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
	
}
