package com.suntomor.bank.frame.web;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

public class CustomDateEditor extends PropertyEditorSupport {
	
	private final static Logger logger = LoggerFactory.getLogger(CustomDateEditor.class);

	private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	private static final DateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final DateFormat GMT_DATE_FORMAT = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", new Locale("ENGLISH", "CHINA"));
	
	private DateFormat dateFormat;
	private boolean allowEmpty = true;

	public CustomDateEditor() {
	}

	public CustomDateEditor(DateFormat dateFormat) {
		this.dateFormat = dateFormat;
	}

	public CustomDateEditor(boolean allowEmpty) {
		this.allowEmpty = allowEmpty;
	}

	public CustomDateEditor(DateFormat dateFormat, boolean allowEmpty) {
		this.dateFormat = dateFormat;
		this.allowEmpty = allowEmpty;
	}

	/**
	 * Parse the Date from the given text, using the specified DateFormat.
	 */
	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (this.allowEmpty && !StringUtils.hasText(text)) {
			setValue(null);
		} else {
			Date parseDate = null;
			try {
				if (NumberUtils.isNumber(text)) {
					parseDate = new Date(Long.parseLong(text));
				} else {
					if (this.dateFormat != null)
						parseDate = this.dateFormat.parse(text);
					else {
						text = text.replaceAll("GMT\\+0800$", "GMT\\+08:00");
						try {
							parseDate = DATE_TIME_FORMAT.parse(text);
						} catch (ParseException ex) {
							try {
								parseDate = GMT_DATE_FORMAT.parse(text);
							} catch (ParseException e) {
								parseDate = DATE_FORMAT.parse(text);
							}
						}
					}
				}
				setValue(parseDate);
			} catch (ParseException ex) {
				logger.error("setAsText:parse异常", ex);
				throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
			}
		}
	}

	/**
	 * Format the Date as String, using the specified DateFormat.
	 */
	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		DateFormat dateFormat = this.dateFormat;
		if (dateFormat == null)
			dateFormat = DATE_TIME_FORMAT;
		return (value != null ? dateFormat.format(value) : "");
	}

}
