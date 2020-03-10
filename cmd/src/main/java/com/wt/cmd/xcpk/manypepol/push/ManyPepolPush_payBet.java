package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_payBet extends Response
{
	public int pos;
	public int betNum;
	public ManyPepolPush_payBet()
	{
		msgType = MsgTypeEnum.manypepol_有闲家下注.getType();
	}

	public ManyPepolPush_payBet(int pos,int betNum)
	{
		this.betNum = betNum;
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_payBet [pos=" + pos + ", betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
