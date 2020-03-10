package com.wt.cmd.serverutil;

import java.util.ArrayList;
import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.naval.vo.ServerInfoVO;

public class GetServerListResponse extends Response {

 	public ArrayList<ServerInfoVO> list_serverInfo;// 服务器消息

	public GetServerListResponse() {
		this.msgType = MsgType.UTIL_SERVER_LIST;
	}

	public GetServerListResponse(int code) {
		this.msgType = MsgType.UTIL_SERVER_LIST;
	}
	
	public GetServerListResponse(int code, ArrayList<ServerInfoVO> list_serverInfo) {
		this.msgType = MsgType.UTIL_SERVER_LIST;
		this.code = code;
		this.list_serverInfo = list_serverInfo;
	}

	@Override
	public String toString()
	{
		return "GetServerListResponse [list_serverInfo=" + list_serverInfo + "]";
	}
}
