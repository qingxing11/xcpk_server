package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class EnterBeginnerRequest extends Request
{
	public int type;
	public EnterBeginnerRequest()
	{
		msgType = MsgTypeEnum.classic_进入新手场.getType();
	}
}
