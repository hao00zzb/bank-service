package com.suntomor.bank.frame.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public final class PropertiesUtils {
	
	private static Logger logger = LogManager.getLogger(PropertiesUtils.class);
	
	private PropertiesUtils(){}
	
	public static Properties getProperties(String... fileNames) {
		Properties prop = new Properties();
		for (String fileName : fileNames) {
			InputStream is = null;
			try {
				is = PropertiesUtils.class.getClassLoader().getResourceAsStream(fileName);
				prop.load(is);
			} catch (IOException e) {
				logger.error("配置文件装载异常",e);
				throw new RuntimeException(e.getMessage(), e);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
		return prop;
	}
	
}
