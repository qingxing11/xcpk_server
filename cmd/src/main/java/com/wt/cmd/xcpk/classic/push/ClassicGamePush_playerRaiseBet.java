package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ClassicGamePush_playerRaiseBet extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public int betNum;
	public ClassicGamePush_playerRaiseBet()
	{
		msgType = MsgTypeEnum.classic_有玩家加注.getType();
	}
	
	public ClassicGamePush_playerRaiseBet(int pos,int betNum)
	{
		msgType = MsgTypeEnum.classic_有玩家加注.getType();
		this.pos = pos;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_playerRaiseBet [pos=" + pos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
