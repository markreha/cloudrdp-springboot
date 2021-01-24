package com.gcu.controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@RequestMapping("/")
public class IndexController
{
	@RequestMapping(path="/", method=RequestMethod.GET)
	public String displayPage(HttpSession session)
	{
		if(session.getAttribute("token") == null)
		{
			return "login";
		}
		else
		{
			return "mainpage";
		}
	}
}
