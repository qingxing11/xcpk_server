package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class KillRoomPayBetRequest extends Request
{
	public int pos;
	public int chipNum;
	public KillRoomPayBetRequest()
	{
		msgType = MsgTypeEnum.KillRoom_通杀场下注.getType();
	}
}
