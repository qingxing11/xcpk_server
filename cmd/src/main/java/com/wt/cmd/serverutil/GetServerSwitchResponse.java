package com.wt.cmd.serverutil;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

 
public class GetServerSwitchResponse extends Response {
	
	public int app_switch;
	public String ip;
	public String port;
	public int version;
	public int lastVersion;
	public int myVersion1;
	public int myVersion2;
	public GetServerSwitchResponse() {
		this.msgType = MsgType.UTIL_服务器开关;
	}

	public GetServerSwitchResponse(int code,int app_switch,String ip,String port,int version,int lastVersion,int myVersion1,int myVersion2) {
		this.msgType = MsgType.UTIL_服务器开关;
		this.code = code;
		this.app_switch = app_switch;
		this.ip = ip;
		this.port = port;
		this.version = version;
		this.lastVersion = lastVersion;
		this.myVersion1 = myVersion1;
		this.myVersion2 = myVersion2;
	}
}