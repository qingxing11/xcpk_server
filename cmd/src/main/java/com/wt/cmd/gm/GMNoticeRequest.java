package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GMNoticeRequest  extends Request
{
	public String txt;
	public GMNoticeRequest()
	{
		msgType = MsgType.GM_发送公告;
	}
	
	public GMNoticeRequest(String txt)
	{
		msgType = MsgType.GM_发送公告;
		this.txt = txt;
	}
}