package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetPayOrderResultRequest  extends Request
{
	public String out_trade_no;
	public GetPayOrderResultRequest()
	{
		msgType = MsgType.PAY_获取支付结果;
	}
	
	public GetPayOrderResultRequest(String out_trade_no)
	{
		msgType = MsgType.PAY_获取支付结果;
		this.out_trade_no = out_trade_no;
	}
}