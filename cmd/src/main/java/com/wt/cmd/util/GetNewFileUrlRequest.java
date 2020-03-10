package com.wt.cmd.util;

import java.util.ArrayList;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

 
public class GetNewFileUrlRequest extends Request {
	public ArrayList<String> fileName;
	
	public GetNewFileUrlRequest() {
		this.msgType = MsgType.UTIL_GET_NEW_FILE_URL;
	}
	
	
	public GetNewFileUrlRequest(ArrayList<String> fileName) {
		this.msgType = MsgType.UTIL_GET_NEW_FILE_URL;
		this.fileName = fileName;
	}
}
