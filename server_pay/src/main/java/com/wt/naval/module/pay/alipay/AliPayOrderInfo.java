package com.wt.naval.module.pay.alipay;

public class AliPayOrderInfo
{
	public String out_trade_no;
	public String total_fee;
	
	public String orderInfo;

	public void initOrderInfo(String out_trade_no,String total_fee,String orderInfo)
	{
		this.out_trade_no = out_trade_no;
		this.total_fee = total_fee;
		this.orderInfo = orderInfo;
	}
}
