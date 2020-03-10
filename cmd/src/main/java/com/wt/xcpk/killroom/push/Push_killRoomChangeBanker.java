package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class Push_killRoomChangeBanker extends Response
{
	public PlayerSimpleData banker;
	
	public Push_killRoomChangeBanker()
	{
		this.msgType = MsgTypeEnum.KillRoom_更换庄家.getType();
	}

	public Push_killRoomChangeBanker(PlayerSimpleData banker)
	{
		this.msgType = MsgTypeEnum.KillRoom_更换庄家.getType();
		this.banker = banker;
	}

	@Override
	public String toString()
	{
		return "Push_killRoomChangeBanker [banker=" + banker + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
