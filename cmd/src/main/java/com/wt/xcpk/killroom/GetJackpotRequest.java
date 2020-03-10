package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class GetJackpotRequest extends Request
{
	public GetJackpotRequest()
	{
		msgType = MsgTypeEnum.KillRoom_获取奖池.getType();
	}
}
