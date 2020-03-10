package com.wt.cmd.loadtest;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class LoadTestConnectResponse extends Response
{
	public LoadTestConnectResponse() {
		this.msgType = MsgType.LOADTEST_连接;
	}

	public LoadTestConnectResponse(int code,int callbackId) {
		this.msgType = MsgType.LOADTEST_连接;
		this.code = code;
		
		this.callBackId = callbackId;
	}
}
