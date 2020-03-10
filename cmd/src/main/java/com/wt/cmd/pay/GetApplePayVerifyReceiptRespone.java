package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetApplePayVerifyReceiptRespone extends Response
{
	public GetApplePayVerifyReceiptRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GetApplePayVerifyReceiptRespone(int code) {
		this.msgType = MsgType.PAY_获取苹果支付结果;
		this.code = code;
	}
}
