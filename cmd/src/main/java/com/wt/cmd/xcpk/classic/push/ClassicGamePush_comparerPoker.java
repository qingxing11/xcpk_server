package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ClassicGamePush_comparerPoker extends Response
{
	/**发起方*/
	public int pos0;
	
	/**被比方*/
	public int pos1;
	
	/**输方*/
	public int lossPos;
	
	public int subCoins;
	public ClassicGamePush_comparerPoker()
	{
		msgType = MsgTypeEnum.classic_其他玩家比牌.getType();
	}
	
	public ClassicGamePush_comparerPoker(int pos0,int pos1,int lossPos,int subCoins)
	{
		msgType = MsgTypeEnum.classic_其他玩家比牌.getType();
		this.pos0 = pos0;
		this.pos1 = pos1;
		this.lossPos = lossPos;
		this.subCoins = subCoins;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_comparerPoker [pos0=" + pos0 + ", pos1=" + pos1 + ", lossPos=" + lossPos + ", subCoins=" + subCoins + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
