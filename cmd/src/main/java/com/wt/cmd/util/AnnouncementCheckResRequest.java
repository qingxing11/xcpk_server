package com.wt.cmd.util;

import java.util.ArrayList;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class AnnouncementCheckResRequest extends Request {

	public ArrayList<String> path=new ArrayList<String>();//现有资源
	
	public ArrayList<String> md5=new ArrayList<String>();//现有资源md5
	
	public AnnouncementCheckResRequest() {
	}

	public AnnouncementCheckResRequest(ArrayList<String> path, ArrayList<String> md5) {
		this.msgType = MsgType.UTIL_CHECK_FILE;
		this.path = path;
		this.md5 = md5;
	}

}
