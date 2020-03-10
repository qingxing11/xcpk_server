package com.gjc.cmd.lottery;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskAutoLotteryRequest extends Request
{
		public int userId;
		public int num;

		public AskAutoLotteryRequest() 
		{
			this.msgType= MsgTypeEnum.Lottery_自动下注.getType();
		}
		
		public AskAutoLotteryRequest(int userId,int num) 
		{
			this.msgType= MsgTypeEnum.Lottery_自动下注.getType();
			this.userId = userId;
			this.num=num;
		}



		@Override
		public String toString() {
			return "AskAutoLotteryRequest [userId=" + userId + ", msgType=" + msgType + ", callBackId=" + callBackId+ "]";
		}
		
}
