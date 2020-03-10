package com.gjc.cmd.safebox;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class SafeBoxTransferSureRequest extends Request
{
		public int otherId;
		
		public SafeBoxTransferSureRequest() 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账确认.getType();
		}
		
		public SafeBoxTransferSureRequest(int userId, int otherId, long money) 
		{
			this.msgType= MsgTypeEnum.SafeBox_游戏币转账确认.getType();
			this.otherId = otherId;
		}


		@Override
		public String toString() {
			return "SafeBoxTransferSureRequest [otherId=" + otherId + ", msgType=" + msgType + ", callBackId="
					+ callBackId + "]";
		}
}
