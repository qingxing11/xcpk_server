package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomResignBanker extends Response
{
	public Push_killRoomResignBanker()
	{
		this.msgType = MsgTypeEnum.KillRoom_自己下庄.getType();
	}
}
