package com.lwj.cmd.sign;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class SignClickResponse extends Response {
	public static final int ALREADY_签到 = 0;
	
	public SignClickResponse() {
		msgType = MsgType.GAME_点击签到;
	}
	
	public SignClickResponse(int code)
	{
		msgType = MsgType.GAME_点击签到;
		this.code = code;
	}
	

}
