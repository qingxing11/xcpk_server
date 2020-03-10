package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class LeaveClassicGameRequest extends Request
{
	public LeaveClassicGameRequest()
	{
		msgType = MsgTypeEnum.classic_玩家离开.getType();
	}
}
