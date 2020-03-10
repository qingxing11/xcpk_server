package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomCheckPokerRequest extends Request
{
	public ManyPepolRoomCheckPokerRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家看牌.getType();
	}
}
