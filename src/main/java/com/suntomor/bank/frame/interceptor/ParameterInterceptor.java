package com.suntomor.bank.frame.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.suntomor.bank.frame.utils.CookieUtils;

public class ParameterInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(ParameterInterceptor.class);
	private static final String dateFormat = "\t\t yyyy/MM/dd HH:mm:ss.SSS";
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String clientIpAddress = CookieUtils.getCurrIpAddress(request);
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
        logger.info("RequestParameterFilter begin log: " + clientIpAddress+sf.format(new Date()));
        logger.info("RequestServlet: " + request.getRequestURI());
        
		Map<String, String[]> parameterMaps = request.getParameterMap();
        for (Map.Entry<String, String[]> entry : parameterMaps.entrySet()) {
            logger.info("Key:" + entry.getKey());
            if (entry.getValue() != null) {
                for (String str : entry.getValue()) {
                    logger.info("Value:"+str);
                }
            }
        }
		
		return super.preHandle(request, response, handler);
	}
	
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
			Object handler, Exception ex) throws Exception {
		String clientIpAddress = CookieUtils.getCurrIpAddress(request);
		SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
		logger.info("RequestParameterFilter end log: "+clientIpAddress+sf.format(new Date()));
    }

}
