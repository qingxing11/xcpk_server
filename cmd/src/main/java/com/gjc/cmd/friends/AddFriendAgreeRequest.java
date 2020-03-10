package com.gjc.cmd.friends;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AddFriendAgreeRequest  extends Request
{
	public int id;//请求添加好友方
	public boolean result;//结果

	public AddFriendAgreeRequest()
	{
		this.msgType=MsgTypeEnum.Agree_同意添加好友.getType();  
	}

	public AddFriendAgreeRequest(int id,boolean result)
	{
		this.msgType =MsgTypeEnum.Agree_同意添加好友.getType();  
		this.id=id;
		this.result=result;
	}

	@Override
	public String toString()
	{
		return "AddFriendAgreeRequest  添加好友同意请求[好友id=" + id + "]";
	}
}
