package com.wt.cmd.backend;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class ServerShutDownRequest  extends Request
{
	public int time;
	public ServerShutDownRequest()
	{
		msgType = MsgType.GM_服务器关机;
	}
	
	public ServerShutDownRequest(int time)
	{
		this.time = time;
		msgType = MsgType.GM_服务器关机;
	}
}