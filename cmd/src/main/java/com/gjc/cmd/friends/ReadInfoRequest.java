package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ReadInfoRequest extends Request
{
		public int userId;
		
		public ReadInfoRequest() 
		{
			this.msgType= MsgTypeEnum.Info_消息已读.getType();
		}
		
		public ReadInfoRequest(int userId) 
		{
			this.msgType= MsgTypeEnum.Info_消息已读.getType();
			this.userId = userId;
		}
		
		@Override
		public String toString() {
			return "ReadInfoRequest [userId=" + userId + "]";
		}
}
