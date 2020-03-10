package com.wt.cmd.user;


import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class RegisterTrialResponse extends Response {
	public static final int ERROR_账号已存在 = 0;
	public static final int ERROR_插入USER表失败 = 1;
	public static final int ERROR_插入USERDATA出错 = 2;
	
	public int msgType;
	public int code;

	public String name;
	public byte[] account;
	public RegisterTrialResponse() {
	}

	public RegisterTrialResponse(int code) {
		this.msgType = MsgType.USER_注册游客账号;
		this.code = code;
	}

	public RegisterTrialResponse(int code,byte[] account) {
		this.msgType = MsgType.USER_注册游客账号;
		this.code = code;
		
		this.account = account;
	}

	public RegisterTrialResponse(int code, String name, byte[] account,byte[] password)
	{
		this.msgType = MsgType.USER_注册游客账号;
		this.code = code;
		
		this.name = name;
		this.account = account;
	}
}
