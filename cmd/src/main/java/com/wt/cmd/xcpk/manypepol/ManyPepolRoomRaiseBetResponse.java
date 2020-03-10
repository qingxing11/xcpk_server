package com.wt.cmd.xcpk.manypepol;

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
public class ManyPepolRoomRaiseBetResponse extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_不在比赛中 = 1;
	public static final int ERROR_没轮到行动 = 2;
	public static final int ERROR_加注不能低于底注 = 3;
	public static final int ERROR_超过20回 = 4;
	
	public int betNum;
	public ManyPepolRoomRaiseBetResponse()
	{
		msgType = MsgTypeEnum.manypepol_玩家加注.getType();
	}
	
	public ManyPepolRoomRaiseBetResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_玩家加注.getType();
		this.code = code;
	}
	
	public ManyPepolRoomRaiseBetResponse(int code,int betNum)
	{
		msgType = MsgTypeEnum.manypepol_玩家加注.getType();
		this.code = code;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "ManyPepolRoomRaiseBetResponse [betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
