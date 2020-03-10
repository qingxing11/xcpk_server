package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class LeaveKillRoomResponse extends Response
{
	public static final int ERROR_不在房间中 = 0;
	public LeaveKillRoomResponse()
	{
		msgType = MsgTypeEnum.KillRoom_离开通杀场.getType();
	}

	public LeaveKillRoomResponse(int code)
	{
		this.code = code;
		msgType = MsgTypeEnum.KillRoom_离开通杀场.getType();
	}
}
