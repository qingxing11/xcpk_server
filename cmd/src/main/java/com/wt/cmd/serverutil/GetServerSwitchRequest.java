package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class GetServerSwitchRequest extends Request {
	public GetServerSwitchRequest() {
		this.msgType = MsgType.UTIL_服务器开关;
	}
	
	public GetServerSwitchRequest(int cb) {
		this.msgType = MsgType.UTIL_服务器开关;
		this.callBackId = cb;
	}
	
}