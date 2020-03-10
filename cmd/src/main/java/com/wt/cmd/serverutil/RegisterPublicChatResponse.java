package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

 
public class RegisterPublicChatResponse extends Response {
	public static final int ERROR_服务器重启维护 = 0;
	
	
	public RegisterPublicChatResponse() {
	}

	public RegisterPublicChatResponse(int code,int callBackId) {
		this.msgType = MsgType.SERVER_PUSH_聊天服注册;
		this.code = code;
		this.callBackId = callBackId;
	}

}
