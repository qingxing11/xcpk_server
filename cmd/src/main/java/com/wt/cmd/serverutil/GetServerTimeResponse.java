package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetServerTimeResponse extends Response {
	public int msgType;
	public int code;
	
	public long serverTime;

	public GetServerTimeResponse() {
	}

	public GetServerTimeResponse(int code) {
		this.msgType = MsgType.UTIL_SERVER_TIME;
		this.code = code;
	}

	public GetServerTimeResponse(int code, long serverTime) {
		this.msgType = MsgType.UTIL_SERVER_TIME;
		this.code = code;
		this.serverTime = serverTime;
	}
}
