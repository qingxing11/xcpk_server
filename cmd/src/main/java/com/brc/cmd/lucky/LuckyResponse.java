package com.brc.cmd.lucky;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class LuckyResponse extends Response
{
	public static final int ERROR_当月已经抽取 = 0;
	
	public int luckyNum;
	
	public LuckyResponse(int code)
	{
		this.code=code;
		msgType=MsgTypeEnum.LUCKY.getType();
	}
	public LuckyResponse(int code,int luckyNum)
	{
		this.code=code;
		msgType=MsgTypeEnum.LUCKY.getType();
		this.luckyNum=luckyNum;
	}
}
