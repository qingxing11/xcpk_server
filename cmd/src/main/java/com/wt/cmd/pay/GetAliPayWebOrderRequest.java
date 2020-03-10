package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetAliPayWebOrderRequest  extends Request
{
	public int payId;
	public GetAliPayWebOrderRequest()
	{
		msgType = MsgType.PAY_支付宝网页支付下单;
	}
	
	public GetAliPayWebOrderRequest(int payId)
	{
		msgType = MsgType.PAY_支付宝网页支付下单;
		this.payId = payId;
	}
}