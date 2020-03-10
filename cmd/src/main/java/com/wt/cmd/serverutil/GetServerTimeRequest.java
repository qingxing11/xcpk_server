package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class GetServerTimeRequest extends Request {

	public GetServerTimeRequest() {
	}

	public GetServerTimeRequest(int msgId) {
		this.msgType = MsgType.UTIL_SERVER_TIME;
	}
}
