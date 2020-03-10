package com.gjc.cmd.lottery;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskLotteryRequest extends Request
{
		public int userId;
		public int number;
		public int type;
		
		public AskLotteryRequest() 
		{
			this.msgType= MsgTypeEnum.Lottery_下注.getType();
		}
		
		public AskLotteryRequest(int userId, int number, int type) 
		{
			this.msgType= MsgTypeEnum.Lottery_下注.getType();
			this.userId = userId;
			this.number = number;
			this.type = type;
		}



		@Override
		public String toString() 
		{
			return "AskLotteryRequest [userId=" + userId + ", number=" + number + ", type=" + type + ", msgType="
					+ msgType + ", callBackId=" + callBackId + "]";
		}
		
		
		
}
