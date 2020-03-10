package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetServerStatusRequest  extends Request
{
	public GetServerStatusRequest()
	{
		msgType = MsgType.GM_服务器状态信息;
	}
}