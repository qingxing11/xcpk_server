package com.wt.cmd.game;

import com.wt.archive.GameData;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PlayerReConnectResponse extends Response
{
//	public static final int ERROR_不在比赛中 = 0;
//	public static final int ERROR_证书空 = 1;
//	public static final int ERROR_不存在的用户 = 2;
//	public static final int ERROR_证书不匹配 = 3;
//	public static final int ERROR_玩家不在游戏中 = 4;
	
	public static final int ERROR_登陆失败 = 0;
	public static final int ERROR_TOKEN空 = 1;
	public static final int ERROR_没有申请过登陆 = 2;
	public static final int ERROR_TOKEN验证失败 = 3;
	public static final int ERROR_不存在的用户 = 4;
	public static final int ERROR_不在任何房间中 = 5;
	public static final int ERROR_SESSION空 = 6;
	
	public GameData gameData;
	
	public boolean isPing;
	
	public PlayerReConnectResponse()
	{
		msgType = MsgTypeEnum.GAME_客户端要求断线重连.getType();
	}

	public PlayerReConnectResponse(int code,GameData gameData,boolean isPing)
	{
		msgType = MsgTypeEnum.GAME_客户端要求断线重连.getType();
		this.code = code;
		this.gameData = gameData;
		this.isPing = isPing;
	}
	
	public PlayerReConnectResponse(int code,GameData gameData)
	{
		msgType = MsgTypeEnum.GAME_客户端要求断线重连.getType();
		this.code = code;
		this.gameData = gameData;
	}
	
	public PlayerReConnectResponse(int code)
	{
		msgType = MsgTypeEnum.GAME_客户端要求断线重连.getType();
		this.code = code;
	}
	
	public PlayerReConnectResponse(int code,boolean isPing)
	{
		msgType = MsgTypeEnum.GAME_客户端要求断线重连.getType();
		this.code = code;
		this.isPing = isPing;
	}
 
	@Override
	public String toString()
	{
		return "PlayerReConnectResponse [gameData=" + gameData + ", isPing=" + isPing + ", msgType=" + msgType + ", code=" + code + "]";
	}
}