package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ClassicGamePush_playerReady extends Response
{
	public int pos;
	public ClassicGamePush_playerReady()
	{
		msgType = MsgTypeEnum.classic_其他玩家准备.getType();
	}
	
	public ClassicGamePush_playerReady(int pos)
	{
		msgType = MsgTypeEnum.classic_其他玩家准备.getType();
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_playerReady [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
