package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_waiPayBet extends Response
{
	public ManyPepolPush_waiPayBet()
	{
		msgType = MsgTypeEnum.manypepol_开始下注.getType();
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_waiPayBet [msgType=" + msgType + ", code=" + code + "]";
	}
}
