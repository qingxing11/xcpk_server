package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetWxWebPayOrderRequest  extends Request
{
	public int payId;
	public GetWxWebPayOrderRequest()
	{
		msgType = MsgType.PAY_微信网页支付下单;
	}
	
	public GetWxWebPayOrderRequest(int payId)
	{
		msgType = MsgType.PAY_微信网页支付下单;
		this.payId = payId;
	}
}