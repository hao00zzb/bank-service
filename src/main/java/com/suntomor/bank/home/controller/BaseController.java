package com.suntomor.bank.home.controller;

import java.util.Date;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import com.suntomor.bank.frame.web.CustomDateEditor;

public class BaseController {

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor());
	}
	
}


