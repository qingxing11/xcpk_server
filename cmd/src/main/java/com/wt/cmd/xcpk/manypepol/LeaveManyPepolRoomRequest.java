package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class LeaveManyPepolRoomRequest extends Request
{
	public boolean isTV;
	public LeaveManyPepolRoomRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家离开.getType();
	}
}
