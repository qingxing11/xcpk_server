package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class LookFriendsRequest extends Request
{
	public int userId;
	public LookFriendsRequest()
	{
		this.msgType= MsgTypeEnum.Look_查找好友.getType();
	}
	public LookFriendsRequest(int userId)
    {
        this.userId = userId;
        this.msgType =MsgTypeEnum.Look_查找好友.getType();
    }
	@Override
	public String toString()
	{
		return "LookFriendsRequest:查找好友";
	}
}
