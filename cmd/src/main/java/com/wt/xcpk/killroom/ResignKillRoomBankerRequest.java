package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ResignKillRoomBankerRequest extends Request
{
	public ResignKillRoomBankerRequest()
	{
		msgType = MsgTypeEnum.KillRoom_通杀场下庄.getType();
	}
}
