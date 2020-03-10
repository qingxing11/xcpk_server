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
public class FollowBetResponse extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_不在比赛中 = 1;
	public static final int ERROR_没轮到行动 = 2;
	public static final int ERROR_超过20回 = 3;
	public static final int ERROR_全压不能跟注 = 4;
	
	public int betNum;
	public FollowBetResponse()
	{
		msgType = MsgTypeEnum.classic_玩家跟注.getType();
	}
	
	public FollowBetResponse(int code)
	{
		msgType = MsgTypeEnum.classic_玩家跟注.getType();
		this.code = code;
	}
	
	public FollowBetResponse(int code,int betNum)
	{
		msgType = MsgTypeEnum.classic_玩家跟注.getType();
		this.code = code;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "FollowBetResponse [betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
