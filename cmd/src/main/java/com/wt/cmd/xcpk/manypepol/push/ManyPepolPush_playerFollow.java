package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 *
 */
public class ManyPepolPush_playerFollow extends Response
{
	/**
	 * 玩家位置
	 */
	public int pos;
	
	public int betNum;
	public ManyPepolPush_playerFollow()
	{
		msgType = MsgTypeEnum.manypepol_其他玩家跟注.getType();
	}

	public ManyPepolPush_playerFollow(int pos,int num)
	{
		msgType = MsgTypeEnum.manypepol_其他玩家跟注.getType();
		this.pos = pos;
		this.betNum = num;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_playerFollow [pos=" + pos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
