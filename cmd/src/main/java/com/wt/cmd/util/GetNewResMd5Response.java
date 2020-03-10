package com.wt.cmd.util;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetNewResMd5Response extends Response {
//	public HashMap<String,String> map_fileMd5;
	public String newMd5Url;
	public GetNewResMd5Response() {
	}

	public GetNewResMd5Response(int code,String newMd5Url) {
		this.msgType = MsgType.UTIL_GET_NEW_MD5;
		this.code = code;
		this.newMd5Url = newMd5Url;
	}

}
