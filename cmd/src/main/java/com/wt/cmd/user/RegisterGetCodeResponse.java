package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class RegisterGetCodeResponse extends Response {
	public static final int ERROR_用户名已存在 = 0;
	
	public int msgType;
	public int code;
	
	public RegisterGetCodeResponse() {
	}

	public RegisterGetCodeResponse(int code) {
		this.msgType = MsgType.USER_REGISTER_GETCODE;
		this.code = code;
	}
}
