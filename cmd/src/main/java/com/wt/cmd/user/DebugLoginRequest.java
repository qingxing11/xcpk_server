package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class DebugLoginRequest extends Request {

	public String userName;

	public DebugLoginRequest() {
		this.msgType = MsgType.USER_DEBUGLOGIN;
	}

	public DebugLoginRequest(String userName) {
		this.msgType = MsgType.USER_DEBUGLOGIN;
		this.userName = userName;
	}
}
