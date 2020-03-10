package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetWxPayOrderRequest  extends Request
{
	public int payId;
	public GetWxPayOrderRequest()
	{
		msgType = MsgType.PAY_微信支付下单;
	}
	
	public GetWxPayOrderRequest(int payId)
	{
		msgType = MsgType.PAY_微信支付下单;
		this.payId = payId;
	}
}