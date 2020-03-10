package com.wt.cmd.ggl;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class GglBuyOnceRequest  extends Request
{
	public int level;
	public GglBuyOnceRequest()
	{
		msgType = MsgTypeEnum.GGL_单次购买.getType();
	}
	
	public GglBuyOnceRequest(int level)
	{
		msgType = MsgTypeEnum.GGL_单次购买.getType();
		this.level = level;
	}
}