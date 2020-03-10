package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class LeaveKillRoomRequest extends Request
{
	public LeaveKillRoomRequest()
	{
		msgType = MsgTypeEnum.KillRoom_离开通杀场.getType();
	}

	@Override
	public String toString()
	{
		return "LeaveKillRoomRequest [msgType=" + msgType + "]";
	}
}
