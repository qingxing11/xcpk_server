package com.gjc.cmd.safebox;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class DepositSafeBoxRequest extends Request
{
		public int userId;
		public long money;
		
		public DepositSafeBoxRequest() 
		{
			this.msgType= MsgTypeEnum.SafeBox_存入银行.getType();
		}
		
		public DepositSafeBoxRequest(int userId, long money) 
		{
			this.msgType= MsgTypeEnum.SafeBox_存入银行.getType();
			this.userId = userId;
			this.money = money;
		}



		@Override
		public String toString() 
		{
			return "DepositSafeBoxRequest [userId=" + userId + ", money=" + money + ", msgType=" + msgType+ ", callBackId=" + callBackId + "]";
		}
		
		
	
}
