package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 */
public class ManyPepolRoomAllInResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	public static final int ERROR_没轮到行动 = 1;
	public static final int ERROR_不在可全压人数 = 2;
	public static final int ERROR_大于全压金币 = 3;
	public static final int ERROR_超过20回 = 4;
	
	public long betNum;
	public ManyPepolRoomAllInResponse()
	{
		msgType = MsgTypeEnum.manypepol_玩家全压.getType();
	}
	
	public ManyPepolRoomAllInResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_玩家全压.getType();
		this.code = code;
	}
	
	public ManyPepolRoomAllInResponse(int code,long betNum)
	{
		msgType = MsgTypeEnum.manypepol_玩家全压.getType(); 
		this.code = code;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "ManyPepolRoomAllInResponse [betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
