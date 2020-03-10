package com.wt.cmd.pay;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class BuyGoldRespone extends Response
{
	public static final int ERROR_金币不足 = 0;
	
	public int reward;
	public BuyGoldRespone() {
		msgType = MsgTypeEnum.PAY_BUYGOLD.getType();
	}
	
	public BuyGoldRespone(int code) {
		msgType = MsgTypeEnum.PAY_BUYGOLD.getType();
		this.code = code;
	}

	/**
	 * @param code 返回码
	 */
	public BuyGoldRespone(int code,int reward) {
		msgType = MsgTypeEnum.PAY_BUYGOLD.getType();
		this.code = code;
		this.reward = reward;
	}
}