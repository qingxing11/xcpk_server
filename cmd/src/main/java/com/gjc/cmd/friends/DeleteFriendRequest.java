package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class DeleteFriendRequest extends Request
{
	public int userId;
	public DeleteFriendRequest() 
	{
		this.msgType=MsgTypeEnum.Delete_删除好友.getType(); 
	}
	public DeleteFriendRequest(int userId)
	{
		this.msgType=MsgTypeEnum.Delete_删除好友.getType(); 
		this.userId = userId;
	}
	
	@Override
	public String toString() 
	{
		return "DeleteFriendRequest [userId=" + userId + ", msgType=" + msgType + ", callBackId=" + callBackId + "]";
	}
	
}
