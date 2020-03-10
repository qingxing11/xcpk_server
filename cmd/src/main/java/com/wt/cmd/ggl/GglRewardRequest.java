package com.wt.cmd.ggl;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class GglRewardRequest  extends Request
{
	public GglRewardRequest()
	{
		msgType = MsgTypeEnum.GGL_兑奖.getType();
	}
}