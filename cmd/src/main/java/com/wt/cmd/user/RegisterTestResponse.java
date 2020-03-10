package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class RegisterTestResponse extends Response {
	public static final int ERROR_账号存在 = 0;
	public String name = "";

	public RegisterTestResponse() {
	}

	public RegisterTestResponse(int code,int callbackId) {
		this.msgType = MsgType.USER_REGISTER_TESTACCOUNT;
		this.code = code;
		
		this.callBackId = callbackId;
	}

	public RegisterTestResponse(int code, String name,int callbackId) {
		this.msgType = MsgType.USER_REGISTER_TESTACCOUNT;
		this.code = code;
		this.name = name;
		this.callBackId = callbackId;
	}
}
