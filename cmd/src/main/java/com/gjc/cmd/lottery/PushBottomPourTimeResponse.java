package com.gjc.cmd.lottery;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushBottomPourTimeResponse extends Response
{
	public int time;

	public PushBottomPourTimeResponse() 
	{
		this.msgType= MsgTypeEnum.Lottery_下注时间.getType();
	}
	public PushBottomPourTimeResponse(int time) 
	{
		this.msgType= MsgTypeEnum.Lottery_下注时间.getType();
		this.code=PushRunALotteryTimeResponse.SUCCESS;
		this.time = time;
	}

	@Override
	public String toString() {
		return "PushBottomPourTimeResponse [time=" + time + ", msgType=" + msgType + ", data="
				+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
}
