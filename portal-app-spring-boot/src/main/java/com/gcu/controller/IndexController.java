package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/")
public class IndexController
{
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String displayPage(SessionStatus session)
	{
		if(session.equals(null))
		{
			return "login";
		}
		else
		{
			return "mainpage";
		}
	}
}
