package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class Push_killRoomOtherPlayerApplicationBanker extends Response
{
	public PlayerSimpleData bankerRequest;
	public Push_killRoomOtherPlayerApplicationBanker()
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家上庄.getType();
	}
	
	public Push_killRoomOtherPlayerApplicationBanker(PlayerSimpleData bankerRequest)
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家上庄.getType();
		this.bankerRequest = bankerRequest;
	}
}
