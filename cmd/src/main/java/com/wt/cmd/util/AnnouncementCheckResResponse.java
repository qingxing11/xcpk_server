package com.wt.cmd.util;

import java.util.ArrayList;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class AnnouncementCheckResResponse extends Response {
	public int msgType;
	public int code;
	
	public ArrayList<String> expiredRes=new ArrayList<String>();//需要下载的资源path
	
	public ArrayList<String> deleteRes=new ArrayList<String>();//需要删除的资源
	public AnnouncementCheckResResponse() {
	}

	public AnnouncementCheckResResponse(int code, ArrayList<String> expiredRes,ArrayList<String> deleteRes) {
 		this.msgType = MsgType.UTIL_CHECK_FILE;
 		this.code = code;
 		this.expiredRes=expiredRes;
 		this.deleteRes = deleteRes;
	}

}
