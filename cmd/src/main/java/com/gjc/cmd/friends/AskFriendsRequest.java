package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AskFriendsRequest extends Request
{
	public AskFriendsRequest()
	{
		this.msgType=MsgTypeEnum.ASKFRIENDS.getType(); 
	}
	
	@Override
	public String toString()
	{
		return "AskFriendsRequest:请求好友列表 ";
	}
}
