package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ApplicationKillRoomBankerRequest extends Request
{
	public ApplicationKillRoomBankerRequest()
	{
		msgType = MsgTypeEnum.KillRoom_通杀场上庄.getType();
	}
}
