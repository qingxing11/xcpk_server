package com.lwj.cmd.sign;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class OpenSignRequest extends Request
{
	public OpenSignRequest()
	{
		msgType = MsgType.GAME_打开签到;
	}
	
}
