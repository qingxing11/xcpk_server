package com.gjc.cmd.lottery;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushRunALotteryTimeResponse extends Response
{
		public int time;

		public PushRunALotteryTimeResponse() 
		{
			this.msgType= MsgTypeEnum.Lottery_开奖时间.getType();
		}
		public PushRunALotteryTimeResponse(int time) 
		{
			this.msgType= MsgTypeEnum.Lottery_开奖时间.getType();
			this.code=PushRunALotteryTimeResponse.SUCCESS;
			this.time = time;
		}

		@Override
		public String toString() {
			return "PushRunALotteryTimeResponse [time=" + time + ", msgType=" + msgType + ", data="
					+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
		}
		
}
