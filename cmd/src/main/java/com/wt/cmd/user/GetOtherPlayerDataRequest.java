package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GetOtherPlayerDataRequest extends Request
{
	public int otherPlayerId;
	public GetOtherPlayerDataRequest()
	{}

	 /**
	  * @param userId 
	  * @param type
	  * @param num
	  */
	public GetOtherPlayerDataRequest(int otherPlayerId)
	{
		msgType = MsgType.GAME_获取指定玩家数据;
		this.otherPlayerId = otherPlayerId;
	}
}
