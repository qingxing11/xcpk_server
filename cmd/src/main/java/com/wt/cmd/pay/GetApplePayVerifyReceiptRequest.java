package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetApplePayVerifyReceiptRequest  extends Request
{
	public String verifyReceipt;
	public GetApplePayVerifyReceiptRequest()
	{
		msgType = MsgType.PAY_获取苹果支付结果;
	}
	
	public GetApplePayVerifyReceiptRequest(String verifyReceipt)
	{
		msgType = MsgType.PAY_获取苹果支付结果;
		this.verifyReceipt = verifyReceipt;
	}
}