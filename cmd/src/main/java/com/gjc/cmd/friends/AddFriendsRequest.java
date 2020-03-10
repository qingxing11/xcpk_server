package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AddFriendsRequest extends Request
{
	public int id;

	public AddFriendsRequest()
	{
		this.msgType= MsgTypeEnum.ADDFRIENDS.getType(); 
	}

	public AddFriendsRequest(int id)
	{
		this.msgType =MsgTypeEnum.ADDFRIENDS.getType(); 
		this.id=id;
		
	}

	@Override
	public String toString()
	{
		return "AddFriendsRequest [id=" + id + ", msgType=" + msgType + "]";
	}
}
