package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FoldRequest extends Request
{
	public FoldRequest()
	{
		msgType = MsgTypeEnum.classic_玩家弃牌.getType();
	}
}
