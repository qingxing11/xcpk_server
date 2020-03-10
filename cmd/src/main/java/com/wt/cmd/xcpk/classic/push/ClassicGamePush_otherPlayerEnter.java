package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class ClassicGamePush_otherPlayerEnter extends Response
{
	public PlayerSimpleData playerSimpleData;
	public ClassicGamePush_otherPlayerEnter()
	{
		msgType = MsgTypeEnum.classic_其他玩家进入房间.getType();
	}
	
	public ClassicGamePush_otherPlayerEnter(PlayerSimpleData playerSimpleData)
	{
		msgType = MsgTypeEnum.classic_其他玩家进入房间.getType();
		this.playerSimpleData = playerSimpleData;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_otherPlayerEnter [playerSimpleData=" + playerSimpleData + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
