package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetServerListRequest extends Request {

	public GetServerListRequest() {
		this.msgType = MsgType.UTIL_SERVER_LIST;
	}
}
