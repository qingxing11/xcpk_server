package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ClassicGamePush_playerLeave extends Response
{
	public int pos;
	public ClassicGamePush_playerLeave()
	{
		msgType = MsgTypeEnum.classic_其他玩家离开.getType();
	}
	
	public ClassicGamePush_playerLeave(int pos)
	{
		msgType = MsgTypeEnum.classic_其他玩家离开.getType();
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_playerLeave [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
