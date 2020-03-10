package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetLogListRespone extends Response
{
	
	public String[] log_path;
	public GetLogListRespone() {
	}

	public GetLogListRespone(int code) {
		this.msgType = MsgType.GM_获取日志列表;
		this.code = code;
	}
	
	public GetLogListRespone(int code,String[] log_path) {
		this.msgType = MsgType.GM_获取日志列表;
		this.code = code;
		
		this.log_path = log_path;
	}
}
