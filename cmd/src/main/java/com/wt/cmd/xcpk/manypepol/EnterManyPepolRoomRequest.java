package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class EnterManyPepolRoomRequest extends Request
{
	public EnterManyPepolRoomRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家进入.getType();
	}

	@Override
	public String toString()
	{
		return "EnterManyPepolRoomRequest [msgType=" + msgType + "]";
	}
}
