package com.suntomor.bank.frame.common;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.StandardServletEnvironment;

import com.suntomor.bank.frame.utils.PropertiesUtils;

@Component
public class GlobalConfigure {
	
	public static final String SPRING_APPLICATION_CONTEXT_KEY = GlobalConfigure.class.getName() + "_SPRING_APPLICATION_CONTEXT_KEY";
	public static final String APPLICATION_CONTEXT_BASE_PATH = GlobalConfigure.class.getName() + "_APPLICATION_CONTEXT_BASE_PATH";

	public static String FILE_SERVER_LOCAL_PATH = null;
	public static String HTTP_FILE_SERVER_PATH = null;
	
	private static final String SPRING_ACTIVE_PROFILE_DEVELOPMENT = "development";
	private static final String SPRING_ACTIVE_PROFILE_PRODUCTION = "production";
	
	public static final String SIGN_KEY = "csey0512";
	public static final String ORDER_RESULT_SUCCESS = "http://www.csey.net/wx/index.php?mod=model&act=orderinfo";
	
	@Autowired
	private Environment environment;
	
	@PostConstruct
	private void init() {
		String profile = environment.getProperty(StandardServletEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, 
				"development");
		Properties application = null;
		if (SPRING_ACTIVE_PROFILE_DEVELOPMENT.equals(profile)) {
			application = PropertiesUtils.getProperties("config/dev/application.properties");
		} else if (SPRING_ACTIVE_PROFILE_PRODUCTION.equals(profile)) {
			application = PropertiesUtils.getProperties("config/pro/application.properties");
		}
		
		String os = System.getProperties().getProperty("os.name");
		HTTP_FILE_SERVER_PATH = application.getProperty("http.file.server");
		
		if(StringUtils.startsWithIgnoreCase(os, "win")){
			FILE_SERVER_LOCAL_PATH = application.getProperty("windows.file.server.local.path");
		} else {
			FILE_SERVER_LOCAL_PATH = application.getProperty("linux.file.server.local.path");
		}
	}
	
	@PreDestroy
	private void destroy() {
		GlobalConfigure.FILE_SERVER_LOCAL_PATH = null;
		GlobalConfigure.HTTP_FILE_SERVER_PATH = null;
	}
}
