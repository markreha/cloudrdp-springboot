package com.gcu.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.gcu"})
public class PortalApplication
{
	public static void main(String[] args)
	{
		SpringApplication.run(PortalApplication.class, args);
	}
}
