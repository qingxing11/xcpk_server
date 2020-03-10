package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class CheckPokerRequest extends Request
{
	public CheckPokerRequest()
	{
		msgType = MsgTypeEnum.classic_玩家看牌.getType();
	}
}
