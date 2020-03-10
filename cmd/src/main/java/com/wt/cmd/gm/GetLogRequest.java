package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetLogRequest  extends Request
{
	public String logName;
	public GetLogRequest()
	{
		msgType = MsgType.GM_获取指定日志;
	}
	
	public GetLogRequest(String logName)
	{
		msgType = MsgType.GM_获取指定日志;
		
		this.logName = logName;
	}
}