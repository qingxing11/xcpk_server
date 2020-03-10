package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class ChangeNickNameResponse extends Response
{
	public static final int ERROR_敏感词 = 0;
	public static final int ERROR_昵称重复 = 1;
	
	public int msgType;
	public int code;
	
	public ChangeNickNameResponse(int code)
	{
		this.msgType = MsgType.USER_CHANGE_NICK;
		this.code = code;
	}
}
