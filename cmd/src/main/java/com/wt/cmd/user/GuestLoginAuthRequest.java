package com.wt.cmd.user;


import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GuestLoginAuthRequest extends Request
{
 	public String device_code;
	public GuestLoginAuthRequest()
	{
		this.msgType = MsgType.USER_游客快速登录;
		
	}

	public GuestLoginAuthRequest(String device_code)
	{
		this.msgType = MsgType.USER_游客快速登录;
 		this.device_code = device_code;
	}

	@Override
	public String toString()
	{
		return "GuestLoingAuthRequest [device_code=" + device_code + "]";
	}
	
	
}
