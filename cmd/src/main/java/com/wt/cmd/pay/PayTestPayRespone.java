package com.wt.cmd.pay;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PayTestPayRespone extends Response
{
	public int reward;
	public PayTestPayRespone() {
		msgType = MsgTypeEnum.PAY_TestPay.getType();
	}

	/**
	 * @param code 返回码
	 */
	public PayTestPayRespone(int code,int reward) {
		msgType = MsgTypeEnum.PAY_TestPay.getType();
		this.code = code;
		this.reward = reward;
	}
}