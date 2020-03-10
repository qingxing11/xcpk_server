package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomComparerPokerRequest extends Request
{
	public int pos;
	public ManyPepolRoomComparerPokerRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家比牌.getType();
	}
}
