package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GetOtherPlayerDataResponse extends Response
{
	public static final int ERROR_玩家ID不存在 = 0;
	
	public int msgType;
	public int code;
	public GetOtherPlayerDataResponse() {
	}


	public GetOtherPlayerDataResponse(int code) {
		this.msgType = MsgType.GAME_获取指定玩家数据;
		this.code = code;
	}
}
