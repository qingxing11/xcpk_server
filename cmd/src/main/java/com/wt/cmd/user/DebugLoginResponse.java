package com.wt.cmd.user;

import com.wt.archive.UserData;
import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

 
public class DebugLoginResponse extends Response {
	public static final int ERROR_NO_USER = 100;
	public static final int ERROR_服务器重启维护 = 101;
	
	public UserData bean;
	public String publicKey="";
	
	
	public DebugLoginResponse() {
	}

	public DebugLoginResponse(int code) {
		this.msgType = MsgType.USER_DEBUGLOGIN;
		this.code = code;
	}

	public DebugLoginResponse(int code, UserData bean, String publicKey) {
		publicKey = "";
		
		this.msgType = MsgType.USER_DEBUGLOGIN;
		this.code = code;
		this.bean = bean;
		this.publicKey=publicKey;
	}
}
