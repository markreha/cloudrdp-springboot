package com.gcu.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.gcu.exception.ContainerFoundException;
import com.gcu.exception.ContainerNotFoundException;
import com.gcu.exception.ImageNotFoundException;
import com.gcu.model.Container;
import com.gcu.model.User;
import com.gcu.service.ContainerServiceInterface;
import com.gcu.service.ImageServiceInterface;

@Controller
@SessionAttributes("token")
@RequestMapping("/container")
public class ContainerController 
{
	@Autowired
	private ContainerServiceInterface containerService;
	
	@Autowired
	private ImageServiceInterface imageService;
	
	

	@GetMapping("/catalog")
	public ModelAndView productCat(ModelMap model) 
	{
		System.out.println("position 1 in the containerController.productCat method");
		try 
		{
			// Call Container Service to assemble all the containers made by the user
			System.out.println("position 2 in the containerController.productCat method");
			List<Container> containers = containerService.getAllContainers((User) model.get("token"));
			System.out.println("position 3 in the containerController.productCat method");
			// Return containerList as homepage
			ModelAndView mv = new ModelAndView("containerList");
			mv.addObject("containerList", containers);
			return mv;

		}
		// Continue to containerList. JSP states the list is empty
		catch(ContainerNotFoundException e)
		{
			System.out.println("position 4 in the containerController.productCat method");
			ModelAndView mv = new ModelAndView("containerList");
			return mv;
		}
	}
	@GetMapping("/containerForm")
	public ModelAndView addContainerForm(@Valid @ModelAttribute ("container") Container container)
	{
		return new ModelAndView("createContainer", "container", new Container());
	}
	@PostMapping("/createContainer")
	public ModelAndView createContainer(@Valid @ModelAttribute("container") Container container, 
			BindingResult validate, ModelMap model)
	{
		try 
		{
			if(validate.hasErrors())
			{
				return new ModelAndView("productCat");
			}
			
			// Call the container service to create the container for the user
			containerService.createContainer(container, (User) model.get("token"));
			
			// Redirect the user to the container catalog
			return new ModelAndView("redirect:/container/catalog");
		} 
		catch(ImageNotFoundException e)
		{
			ModelAndView mv = new ModelAndView("productCat");
			mv.addObject("message", "The Docker Image reference was not found.");
			return mv;
		}
		catch(ContainerFoundException e)
		{
			ModelAndView mv = new ModelAndView("productCat");
			mv.addObject("message", "The container you are trying to make already exists.");
			return mv;
		}
	}
}
