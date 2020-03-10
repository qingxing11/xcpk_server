package com.wt.cmd.user;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class UserAntiaddictionLogoutRequest extends Request
{
	public int userId;
	public UserAntiaddictionLogoutRequest()
	{
		msgType = MsgTypeEnum.COMMUNICATION_AUTH_玩家登出游戏服.getType();
	}
	
	public UserAntiaddictionLogoutRequest(int userId)
	{
		this.userId = userId;
		msgType = MsgTypeEnum.COMMUNICATION_AUTH_玩家登出游戏服.getType();
	}
}
