package com.gjc.cmd.lottery;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskLotteryTimeRequest extends Request
{
	public AskLotteryTimeRequest() 
	{
		this.msgType= MsgTypeEnum.Lottery_同步时间.getType();	
	}

	@Override
	public String toString() {
		return "AskLotteryTimeRequest [msgType=" + msgType + ", callBackId=" + callBackId + "]";
	}
	

}
