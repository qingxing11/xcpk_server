package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class RaiseBetRequest extends Request
{
	public int betNum;
	public RaiseBetRequest()
	{
		msgType = MsgTypeEnum.classic_玩家加注.getType();
	}
}
