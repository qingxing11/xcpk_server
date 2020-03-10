package com.wt.pay.main;

import javax.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@PropertySource({ "classpath:alipay.properties" })
@SpringBootApplication
public class PayServerMain 
{
//	@Value("${ali.app.id}")
//	private String wxAppId;
	
	public static void main(String[] args)
	{
		SpringApplication.run(PayServerMain.class, args);
	}
	
	@PostConstruct
	private void init()
	{
	}
}
