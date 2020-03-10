package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class GetShareUrlRequest extends Request {
	public GetShareUrlRequest() {
		this.msgType = MsgType.SHARE_获取推广链接;
	}
}