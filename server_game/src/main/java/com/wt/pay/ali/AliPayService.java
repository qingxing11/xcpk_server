package com.wt.pay.ali;

import com.wt.pay.AliPayOrderInfo;

public interface AliPayService
{
	AliPayOrderInfo getAliPayOrder(int userId, String cost, String subject, int payId);
	
	String getPayInfo(int payId);
	
	float getCost(int payId);
}
