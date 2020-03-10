package com.wt.naval.module.pay.alipay;

public interface AliPayService
{
	AliPayOrderInfo getAliPayOrder(int userId,String cost,String subject,String body);
}
