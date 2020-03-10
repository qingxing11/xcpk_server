package com.wt.cmd.xcpk.classic.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ClassicGamePush_waiStart extends Response
{
	public int waitPlayerTime;
	
	public ClassicGamePush_waiStart()
	{
		msgType = MsgTypeEnum.classic_等待比赛开始.getType();
	}

	@Override
	public String toString()
	{
		return "ClassicGamePush_waiStart [msgType=" + msgType + ", code=" + code + "]";
	}
}
