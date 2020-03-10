package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomOtherPlayerResignBanker extends Response
{
	public int userId;
	public Push_killRoomOtherPlayerResignBanker()
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家下庄.getType();
	}
	
	public Push_killRoomOtherPlayerResignBanker(int userId)
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家下庄.getType();
		this.userId = userId;
	}
}
