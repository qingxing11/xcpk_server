package com.wt.cmd.xcpk.manypepol.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolPush_waiStart extends Response
{
	public ManyPepolPush_waiStart()
	{
		msgType = MsgTypeEnum.manypepol_等待比赛开始.getType();
	}

	@Override
	public String toString()
	{
		return "ManyPepolPush_waiStart [msgType=" + msgType + ", code=" + code + "]";
	}
}
