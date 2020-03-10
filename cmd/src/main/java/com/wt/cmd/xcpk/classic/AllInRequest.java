package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class AllInRequest extends Request
{
	public AllInRequest()
	{
		msgType = MsgTypeEnum.classic_玩家全压.getType();
	}
}
