package com.wt.cmd.game;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class GClientReconnectValidationResponse extends Response
{
	public static final int ERROR_登陆失败 = 0;
	public static final int ERROR_TOKEN空 = 1;
	public static final int ERROR_没有申请过登陆 = 2;
	public static final int ERROR_TOKEN验证失败 = 3;
	
	public int userId;
	public long lastLogoutTime;
	public GClientReconnectValidationResponse()
	{
		msgType = MsgTypeEnum.GAME_游戏服断线重连验证.getType();
	}
	
	public GClientReconnectValidationResponse(int code)
	{
		msgType = MsgTypeEnum.GAME_游戏服断线重连验证.getType();
		this.code = code;
	}
	
	public GClientReconnectValidationResponse(int code,int userId,long lastLogoutTime)
	{
		msgType = MsgTypeEnum.GAME_游戏服断线重连验证.getType();
		this.code = code;
		this.lastLogoutTime = lastLogoutTime;
		this.userId = userId;
	}

	@Override
	public String toString()
	{
		return "GClientReconnectValidationResponse [lastLogoutTime=" + lastLogoutTime + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
