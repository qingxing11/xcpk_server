package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_comparerPoker extends Response
{
	/**发起方*/
	public int pos0;
	
	/**被比方*/
	public int pos1;
	
	/**输方*/
	public int lossPos;
	
	public int subCoins;
	public ManyPepolPush_comparerPoker()
	{
		msgType = MsgTypeEnum.manypepol_其他玩家比牌.getType();
	}
	
	public ManyPepolPush_comparerPoker(int pos0,int pos1,int lossPos,int subCoins)
	{
		msgType = MsgTypeEnum.manypepol_其他玩家比牌.getType();
		this.pos0 = pos0;
		this.pos1 = pos1;
		this.lossPos = lossPos;
		this.subCoins = subCoins;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_comparerPoker [pos0=" + pos0 + ", pos1=" + pos1 + ", lossPos=" + lossPos + ", subCoins=" + subCoins + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
