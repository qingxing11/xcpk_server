package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ClassicGamePush_playerDie extends Response
{
	public int pos;
	
	public ClassicGamePush_playerDie()
	{
		msgType = MsgTypeEnum.classic_有玩家弃牌.getType();
	}
	
	public ClassicGamePush_playerDie(int pos)
	{
		msgType = MsgTypeEnum.classic_有玩家弃牌.getType();
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_playerDie [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
