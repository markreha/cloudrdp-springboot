package com.gcu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping({"/"})
public class sayHello
{

	@GetMapping({"/hello"})
	public String whyWontThisWork(Model model)
	{
		return "hello";
	}
	
	/*public ModelAndView printHelloView()
	{
		ModelAndView test = new ModelAndView();
		test.setViewName("hello");
		return test;
	}*/
}