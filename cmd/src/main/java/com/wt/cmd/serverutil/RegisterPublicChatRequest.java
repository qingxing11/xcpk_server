package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class RegisterPublicChatRequest extends Request {
	public RegisterPublicChatRequest() {
		this.msgType = MsgType.SERVER_PUSH_聊天服注册;
	}
}
