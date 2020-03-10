package com.lwj.cmd.sign;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class SignClickRequest extends Request 
{
	public SignClickRequest() {
		msgType = MsgType.GAME_点击签到;
	}
	

}
