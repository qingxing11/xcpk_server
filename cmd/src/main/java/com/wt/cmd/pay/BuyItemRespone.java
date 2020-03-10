package com.wt.cmd.pay;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class BuyItemRespone extends Response
{
	public static final int ERROR_金币不足 = 0;
	
	public int payIndex;
	public BuyItemRespone() {
		msgType = MsgTypeEnum.PAY_BUYITEM.getType();
	}
	
	public BuyItemRespone(int code) {
		msgType = MsgTypeEnum.PAY_BUYITEM.getType();
		this.code = code;
	}

	/**
	 * @param code 返回码
	 */
	public BuyItemRespone(int code,int payIndex) {
		msgType = MsgTypeEnum.PAY_BUYITEM.getType();
		this.code = code;
		this.payIndex = payIndex;
	}
}