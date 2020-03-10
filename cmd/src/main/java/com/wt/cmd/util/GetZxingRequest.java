package com.wt.cmd.util;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetZxingRequest extends Request {
	
	public GetZxingRequest() {
		this.msgType = MsgType.UTIL_获取推广二维码;
	}
}
