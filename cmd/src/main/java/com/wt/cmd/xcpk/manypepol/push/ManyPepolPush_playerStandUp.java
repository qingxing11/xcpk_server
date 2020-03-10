package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_playerStandUp extends Response
{
	public int pos;
	public ManyPepolPush_playerStandUp()
	{
		msgType = MsgTypeEnum.manypepol_玩家下桌.getType();
	}

	public ManyPepolPush_playerStandUp(int pos)
	{
		this.pos = pos;
		msgType = MsgTypeEnum.manypepol_玩家下桌.getType();
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_playerStandUp [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
