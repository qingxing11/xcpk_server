package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolFollowBetRequest extends Request
{
	public ManyPepolFollowBetRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家跟注.getType();
	}
}
