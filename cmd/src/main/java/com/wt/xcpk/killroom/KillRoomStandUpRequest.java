package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class KillRoomStandUpRequest extends Request
{
	public KillRoomStandUpRequest()
	{
		msgType = MsgTypeEnum.KillRoom_从座位站起.getType();
	}
}
