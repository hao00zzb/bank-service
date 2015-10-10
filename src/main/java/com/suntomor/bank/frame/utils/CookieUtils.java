package com.suntomor.bank.frame.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CookieUtils {

	private static final Logger logger = LoggerFactory.getLogger(CookieUtils.class);
	
	/**
	 * 调用Cookie对象的构造函数可以创建Cookie。Cookie对象的构造函数有两个字符串参数：Cookie名字和Cookie值。
	 * 名字和值都不能包含空白字符以及下列字符： []、()、=、,、"、/、?、@、、:、;
	 */
	public static void setLocaleCookie(HttpServletRequest request, HttpServletResponse response, String cookieName, String cookieValue) {
		final int expiry = 60 * 60 * 24 * 30;
		try {
			Cookie cookie = new Cookie(cookieName, URLEncoder.encode(cookieValue, "UTF-8"));
			cookie.setMaxAge(expiry);
			cookie.setPath(request.getContextPath());
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public static String getCookieValue(HttpServletRequest request, HttpServletResponse response, String cookieName){
		Cookie[] cookies = request.getCookies();
		if(cookies == null || cookies.length == 0){
			return "";
		}
		String result = "";
		try {
			for(Cookie cookie : cookies){
				if(cookieName.equals(cookie.getName())){
		               result = URLDecoder.decode(cookie.getValue(), "UTF-8");
					break;
				}
			}
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage(), e);
        }
		return result;
	}
	
	public static String getCurrIpAddress(HttpServletRequest request){
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        if(ipAddress.indexOf(",") != -1){
            ipAddress = ipAddress.split(",")[0];
        }
        return ipAddress;
    }
	
}
