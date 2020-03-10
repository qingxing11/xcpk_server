package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class DeleteInfoRequest extends Request
{
		public int userId;

		public DeleteInfoRequest() 
		{
			this.msgType= MsgTypeEnum.Delete_删除消息.getType();
		}
		public DeleteInfoRequest(int userId)
		{
			this.msgType= MsgTypeEnum.Delete_删除消息  .getType();
			this.userId = userId;
		}

		@Override
		public String toString() 
		{
			return "DeleteInfoRequest [userId=" + userId + "]";
		}
		
		
}
