package com.wt.cmd.pay;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetPayOrderResultRespone extends Response
{
	public static final int ERROR_不存在的订单号 = 0;
	
	public int msgType;
	public int code;
	
	public int gift;
	public GetPayOrderResultRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GetPayOrderResultRespone(int code) {
		this.msgType = MsgType.PAY_获取支付结果;
		this.code = code;
	}
	
	public GetPayOrderResultRespone(int code,int gift) {
		this.msgType = MsgType.PAY_获取支付结果;
		this.code = code;
		this.gift = gift;
	}
}
