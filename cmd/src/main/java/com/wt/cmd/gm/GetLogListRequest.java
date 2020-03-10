package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetLogListRequest  extends Request
{
	public GetLogListRequest()
	{
		msgType = MsgType.GM_获取日志列表;
	}
}