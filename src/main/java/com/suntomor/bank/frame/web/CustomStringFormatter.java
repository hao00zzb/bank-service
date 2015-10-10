package com.suntomor.bank.frame.web;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

public class CustomStringFormatter implements Formatter<String> {

	@Override
	public String print(String object, Locale locale) {
		return object;
	}

	@Override
	public String parse(String text, Locale locale) throws ParseException {
		if (text == null || text.length() == 0) {
			return "";
		}
		text = text.trim();
		return text;
	}

}
