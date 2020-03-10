package com.wt.cmd.pay;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class PayTestPayRequest  extends Request
{
	public int payNum;
	public PayTestPayRequest()
	{
		msgType = MsgTypeEnum.PAY_TestPay.getType();
	}
	
}