package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class EnterKillRoomRequest extends Request
{
	public EnterKillRoomRequest()
	{
		msgType = MsgTypeEnum.KillRoom_进入通杀场.getType();
	}
}
