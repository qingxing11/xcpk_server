package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_playerRaiseBet extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public int betNum;
	public ManyPepolPush_playerRaiseBet()
	{
		msgType = MsgTypeEnum.manypepol_其他玩家加注.getType();
	}
	
	public ManyPepolPush_playerRaiseBet(int pos,int betNum)
	{
		msgType = MsgTypeEnum.manypepol_其他玩家加注.getType();
		this.pos = pos;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_playerRaiseBet [pos=" + pos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
