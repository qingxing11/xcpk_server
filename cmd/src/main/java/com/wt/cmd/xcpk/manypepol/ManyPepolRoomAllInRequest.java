package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomAllInRequest extends Request
{
	public ManyPepolRoomAllInRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家全压.getType();
	}
}
