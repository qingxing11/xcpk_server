package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ComparerPokerRequest extends Request
{
	public int pos;
	public ComparerPokerRequest()
	{
		msgType = MsgTypeEnum.classic_玩家比牌.getType();
	}
}
