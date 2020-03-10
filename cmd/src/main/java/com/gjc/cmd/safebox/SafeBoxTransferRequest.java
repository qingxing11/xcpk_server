package com.gjc.cmd.safebox;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class SafeBoxTransferRequest extends Request
{
		public int otherId;
		public int userId;
		public long money;
		
		public SafeBoxTransferRequest()
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账.getType();
		}
		public SafeBoxTransferRequest(int otherId, int userId, long money) 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账.getType();
			this.otherId = otherId;
			this.userId = userId;
			this.money = money;
		}

		@Override
		public String toString() 
		{
			return "SafeBoxTransferRequest [otherId=" + otherId + ", userId=" + userId + ", money=" + money+ ", msgType=" + msgType + ", callBackId=" + callBackId + "]";
		}
	
}
