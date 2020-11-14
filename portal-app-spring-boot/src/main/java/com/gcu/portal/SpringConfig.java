package com.gcu.portal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcu.data.ContainerDAO;
import com.gcu.data.ContainerDAOInterface;
import com.gcu.data.ImageDAO;
import com.gcu.data.ImageDAOInterface;
import com.gcu.data.UserDAO;
import com.gcu.data.UserDAOInterface;
import com.gcu.service.ContainerService;
import com.gcu.service.ContainerServiceInterface;
import com.gcu.service.ImageService;
import com.gcu.service.ImageServiceInterface;
import com.gcu.service.UserService;
import com.gcu.service.UserServiceInterface;

@Configuration
public class SpringConfig
{
	@Bean(name="containerService")
	public ContainerServiceInterface getContainerService()
	{
		return new ContainerService();
	}
	
	@Bean(name="containerDAO")
	public ContainerDAOInterface getContainerDAO()
	{
		return new ContainerDAO();
	}
	
	@Bean(name="imageService")
	public ImageServiceInterface getImageService()
	{
		return new ImageService();
	}
	
	@Bean(name="imageDAO")
	public ImageDAOInterface getImageDAO()
	{
		return new ImageDAO();
	}
	
	@Bean(name="userService")
	public UserServiceInterface getUserService()
	{
		return new UserService();
	}
	
	@Bean(name="userDAO")
	public UserDAOInterface getUserDAO()
	{
		return new UserDAO();
	}
}
