package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_dealPoker extends Response
{
	/**
	 * 庄家位置
	 */
	public int bankerPos;
	
	/**
	 * 扣底注数
	 */
	public int betNum;
	
	public int playPos;
	public ManyPepolPush_dealPoker()
	{
		msgType = MsgTypeEnum.manypepol_发牌.getType();
	}
	
	public ManyPepolPush_dealPoker(int pos,int betNum,int playPos)
	{
		msgType = MsgTypeEnum.manypepol_发牌.getType();
		this.bankerPos = pos;
		this.betNum = betNum;
		this.playPos = playPos;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_dealPoker [bankerPos=" + bankerPos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
