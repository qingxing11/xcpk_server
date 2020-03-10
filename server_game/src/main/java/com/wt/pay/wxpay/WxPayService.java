package com.wt.pay.wxpay;

public interface WxPayService
{
	String getWxPayOrder(int userId,String cost,String info,String attach);
}
