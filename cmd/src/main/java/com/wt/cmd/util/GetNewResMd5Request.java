package com.wt.cmd.util;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class GetNewResMd5Request extends Request {
	public GetNewResMd5Request() {
		this.msgType = MsgType.UTIL_GET_NEW_MD5;
	}
}
