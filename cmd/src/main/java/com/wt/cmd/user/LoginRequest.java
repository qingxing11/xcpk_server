package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;
 
public class LoginRequest extends Request {
	public String account;
	public String password;
	
	public LoginRequest()
	{
		this.msgType = MsgType.USER_LOGIN;
	}
}
