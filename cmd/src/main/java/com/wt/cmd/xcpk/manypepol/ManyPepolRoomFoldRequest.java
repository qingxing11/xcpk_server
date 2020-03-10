package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomFoldRequest extends Request
{
	public ManyPepolRoomFoldRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家弃牌.getType();
	}
}
