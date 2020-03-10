package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetLogRespone extends Response
{
	public static final int ERROR_未登录的号码 = 0;
	
	public int msgType;
	public int code;
	
	public StringBuffer stringBuffer;
	public GetLogRespone() {
	}

	public GetLogRespone(int code) {
		this.msgType = MsgType.GM_获取指定日志;
		this.code = code;
	}
	
	public GetLogRespone(int code,StringBuffer stringBuffer) {
		this.msgType = MsgType.GM_获取指定日志;
		this.code = code;
		this.stringBuffer = stringBuffer;
	}
}
