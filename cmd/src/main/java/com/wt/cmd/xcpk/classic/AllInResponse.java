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
public class AllInResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	public static final int ERROR_没轮到行动 = 1;
	public static final int ERROR_不在可全压人数 = 2;
	public static final int ERROR_超过20回 = 3;
	
	public long betNum;
	public AllInResponse()
	{
		msgType = MsgTypeEnum.classic_玩家全压.getType();
	}
	
	public AllInResponse(int code)
	{
		msgType = MsgTypeEnum.classic_玩家全压.getType();
		this.code = code;
	}
	
	public AllInResponse(int code,long betNum)
	{
		msgType = MsgTypeEnum.classic_玩家全压.getType(); 
		this.code = code;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "AllInResponse [betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
