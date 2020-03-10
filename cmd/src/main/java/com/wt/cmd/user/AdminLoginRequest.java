package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class AdminLoginRequest extends Request {

	public String userName;

	public AdminLoginRequest() {
		this.msgType = MsgType.USER_ADMIN_LOGIN;
	}

	public AdminLoginRequest(String userName) {
		this.msgType = MsgType.USER_ADMIN_LOGIN;
		this.userName = userName;
	}
}
