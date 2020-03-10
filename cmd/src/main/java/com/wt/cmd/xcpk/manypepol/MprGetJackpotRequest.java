package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class MprGetJackpotRequest extends Request
{
	public MprGetJackpotRequest()
	{
		msgType = MsgTypeEnum.manypepol_获取奖池.getType();
	}
}
