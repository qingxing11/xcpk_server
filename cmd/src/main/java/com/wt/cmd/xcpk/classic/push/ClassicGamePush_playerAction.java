package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ClassicGamePush_playerAction extends Response
{
	public int pos;
	
	public int nowBet;
	
	/**
	 * 是否看过牌
	 */
	public boolean isCheckPoker;
	
	public int round;
	
	/**0:不显示全压  1:显示全压  2:强制全压*/
	public int allInState;
	
	public ClassicGamePush_playerAction()
	{
		msgType = MsgTypeEnum.classic_玩家行动.getType();
	}
	
	public ClassicGamePush_playerAction(int pos,int nowBet,boolean isCheckPoker,int round,int allInState)
	{
		msgType = MsgTypeEnum.classic_玩家行动.getType();
		this.pos = pos;
		this.nowBet = nowBet;
		this.isCheckPoker = isCheckPoker;
		this.round = round;
		this.allInState = allInState;
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_playerAction [pos=" + pos + ", nowBet=" + nowBet + ", isCheckPoker=" + isCheckPoker + ", round=" + round + ", allInState=" + allInState + "]";
	}

}
