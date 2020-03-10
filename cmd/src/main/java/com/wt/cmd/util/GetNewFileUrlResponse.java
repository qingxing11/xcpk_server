package com.wt.cmd.util;

import java.util.HashMap;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetNewFileUrlResponse extends Response {
	
	public HashMap<String,String> map_fileUrl;
	public GetNewFileUrlResponse() {
		this.msgType = MsgType.UTIL_GET_NEW_FILE_URL;
	}
	
	public GetNewFileUrlResponse(int code) {
		this.msgType = MsgType.UTIL_GET_NEW_FILE_URL;
		this.code = code;
	}

	public GetNewFileUrlResponse(int code,HashMap<String,String> map_fileUrl) {
		this.msgType = MsgType.UTIL_GET_NEW_FILE_URL;
		this.code = code;
		this.map_fileUrl = map_fileUrl;
	}

}
