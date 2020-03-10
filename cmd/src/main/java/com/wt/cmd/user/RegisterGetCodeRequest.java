package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class RegisterGetCodeRequest extends Request
{
	public String phoneNumber = "";
	
	public RegisterGetCodeRequest()
	{}

	public RegisterGetCodeRequest(String phoneNumber)
	{
		this.msgType = MsgType.USER_REGISTER_GETCODE;
		this.phoneNumber = phoneNumber;
	}
}
