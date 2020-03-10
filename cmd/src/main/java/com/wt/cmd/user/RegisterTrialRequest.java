package com.wt.cmd.user;


import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class RegisterTrialRequest extends Request
{
 	public long clientTime;
	public RegisterTrialRequest()
	{}

	public RegisterTrialRequest(long clientTime)
	{
		this.msgType = MsgType.USER_注册游客账号;
 		this.clientTime = clientTime;
	}
}
