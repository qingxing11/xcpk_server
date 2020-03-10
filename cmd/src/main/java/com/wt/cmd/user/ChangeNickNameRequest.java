package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class ChangeNickNameRequest extends Request
{
	public String nickName;
	public int gender;
	
	public ChangeNickNameRequest(String nickName,int gender)
	{
		this.msgType = MsgType.USER_CHANGE_NICK;
		this.nickName = nickName;
		this.gender = gender;
	}
}
