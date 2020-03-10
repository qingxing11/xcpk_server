package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class Push_killRoomSitDown extends Response
{
	public PlayerSimpleData player;
	
	public Push_killRoomSitDown()
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家坐下.getType();
	}

	public Push_killRoomSitDown(PlayerSimpleData player)
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家坐下.getType();
		this.player = player;
	}

	@Override
	public String toString()
	{
		return "Push_killRoomSitDown [player=" + player + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
