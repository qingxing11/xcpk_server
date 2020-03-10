package com.wt.cmd.user;

import com.wt.archive.UserData;
import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

 
public class AdminLoginResponse extends Response {
	public static final int ERROR_NO_USER = 100;
	public static final int ERROR_服务器重启维护 = 101;
	
	public UserData bean;
	public String publicKey="";
	
	public AdminLoginResponse() {
	}

	public AdminLoginResponse(int code,int callbackId) {
		this.msgType = MsgType.USER_ADMIN_LOGIN;
		this.code = code;
		this.callBackId = callbackId;
	}

	public AdminLoginResponse(int code, UserData bean, String publicKey,int callbackId) {
		publicKey = "";
		
		this.msgType = MsgType.USER_ADMIN_LOGIN;
		this.code = code;
		this.bean = bean;
		this.publicKey=publicKey;
		
		
		this.callBackId = callbackId;
		
	}
}
