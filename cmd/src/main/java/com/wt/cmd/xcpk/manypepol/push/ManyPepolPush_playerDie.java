package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_playerDie extends Response
{
	public int pos;
	
	public ManyPepolPush_playerDie()
	{
		msgType = MsgTypeEnum.manypepol_有玩家弃牌.getType();
	}
	
	public ManyPepolPush_playerDie(int pos)
	{
		msgType = MsgTypeEnum.manypepol_有玩家弃牌.getType();
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_playerDie [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
