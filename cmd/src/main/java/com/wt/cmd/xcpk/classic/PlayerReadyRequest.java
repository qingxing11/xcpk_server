package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class PlayerReadyRequest extends Request
{
	public PlayerReadyRequest()
	{
		msgType = MsgTypeEnum.classic_玩家准备.getType();
	}
}
