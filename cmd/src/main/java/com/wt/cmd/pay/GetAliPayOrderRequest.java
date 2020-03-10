package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetAliPayOrderRequest  extends Request
{
	public int payId;
	public GetAliPayOrderRequest()
	{
		msgType = MsgType.PAY_支付宝支付下单;
	}
	
	public GetAliPayOrderRequest(int payId)
	{
		msgType = MsgType.PAY_支付宝支付下单;
		this.payId = payId;
	}
}