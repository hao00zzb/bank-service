package com.suntomor.bank.frame.interceptor;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.suntomor.bank.frame.common.GlobalConfigure;
import com.suntomor.bank.frame.common.ThreadLocalWrapper;
import com.suntomor.bank.frame.common.UserContext;
import com.suntomor.bank.frame.utils.CookieUtils;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String contextPath = request.getContextPath();
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+contextPath;
		
		request.setAttribute("path", contextPath);
		request.setAttribute("basePath", basePath);
		
		ThreadLocalWrapper.put(GlobalConfigure.APPLICATION_CONTEXT_BASE_PATH, basePath);
		Object applicationContext = ThreadLocalWrapper.get(GlobalConfigure.SPRING_APPLICATION_CONTEXT_KEY);
		if(applicationContext == null){
			ServletContext servletContext = request.getSession().getServletContext();
			ThreadLocalWrapper.put(GlobalConfigure.SPRING_APPLICATION_CONTEXT_KEY, 
						WebApplicationContextUtils.getWebApplicationContext(servletContext));
		}
		UserContext userContext = new UserContext();
		userContext.setIpAddress(CookieUtils.getCurrIpAddress(request));
		ThreadLocalWrapper.bind(userContext);
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		ThreadLocalWrapper.remove();
		super.afterCompletion(request, response, handler, ex);
	}

}
