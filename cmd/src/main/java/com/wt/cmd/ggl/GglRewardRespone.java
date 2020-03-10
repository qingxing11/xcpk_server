package com.wt.cmd.ggl;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class GglRewardRespone extends Response
{
	public int money;
	public GglRewardRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public GglRewardRespone(int code) {
		this.msgType = MsgTypeEnum.GGL_兑奖.getType();
		this.code = code;
	}
	
	public GglRewardRespone(int code,int money) {
		this.msgType = MsgTypeEnum.GGL_兑奖.getType();
		this.code = code;
		this.money = money;
	}
}
