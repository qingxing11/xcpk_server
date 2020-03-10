package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FollowBetRequest extends Request
{
	public FollowBetRequest()
	{
		msgType = MsgTypeEnum.classic_玩家跟注.getType();
	}
}
