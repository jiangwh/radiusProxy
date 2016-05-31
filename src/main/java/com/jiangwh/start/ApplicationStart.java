package com.jiangwh.start;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ApplicationStart {

	public static ApplicationContext loadConfig(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		return applicationContext;
	}
}
