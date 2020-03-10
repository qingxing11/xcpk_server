package com.wt.pay.httpcontroller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PayCallBackController
{
	@RequestMapping("/aliPayCallBack")
	public String aliPayCallBack()
	{
		System.out.println("aliPayCallBack..................");
		return "Alipay test";
	}
}
