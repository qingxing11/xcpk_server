package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_playerAction extends Response
{
	public int pos;
	public int nowBet;
	public boolean isCheckPoker;
	public int round;
	/**0:不显示全压  1:显示全压  2:强制全压 3:第一回合*/
	public int allinState;
	public ManyPepolPush_playerAction()
	{
		msgType = MsgTypeEnum.manypepol_玩家行动.getType();
	}
	
	public ManyPepolPush_playerAction(int pos, int nowBet, boolean isCheckPoker,int round, int allinState)
	{
		msgType = MsgTypeEnum.manypepol_玩家行动.getType();
		this.pos = pos;
		this.nowBet = nowBet;
		this.isCheckPoker = isCheckPoker;
		this.round = round;
		this.allinState = allinState;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_playerAction [pos=" + pos + ", nowBet=" + nowBet + ", isCheckPoker=" + isCheckPoker + ", round=" + round + ", allinState=" + allinState + "]";
	}
	
	
}
