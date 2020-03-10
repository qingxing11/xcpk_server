package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ReadAddInfoRequest extends Request 
{
	public int userId;

	public ReadAddInfoRequest() 
	{
		this.msgType= MsgTypeEnum.Add_消息已读.getType();
	}

	public ReadAddInfoRequest(int userId) 
	{
		this.msgType= MsgTypeEnum.Add_消息已读.getType();
		this.userId = userId;
	}

	@Override
	public String toString() 
	{
		return "ReadAddInfoRequest [userId=" + userId + "]";
	}

}
