package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 进入经典场：
 * 房间当前状态
 * 当前座位上玩家
 * 当前庄家
 * 
 * 
 * @author akwang
 *
 */
public class PlayerReadyResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	public static final int ERROR_不在可准备状态 = 1;
	public static final int ERROR_已经准备 = 2;
	
	public PlayerReadyResponse()
	{
		msgType = MsgTypeEnum.classic_玩家准备.getType();
	}
	
	public PlayerReadyResponse(int code)
	{
		msgType = MsgTypeEnum.classic_玩家准备.getType();
		this.code = code;
	}

	@Override
	public String toString()
	{
		return "PlayerReadyResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}	
