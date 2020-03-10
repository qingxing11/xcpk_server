package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class ServerInfoRequest extends Request {
	public ServerInfoRequest() {
	}

	public ServerInfoRequest(int msgId) {
		this.msgType = MsgType.USER_SERVER_INFO;
	}

}
