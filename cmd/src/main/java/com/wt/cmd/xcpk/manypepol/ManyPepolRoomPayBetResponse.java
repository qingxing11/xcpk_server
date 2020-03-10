package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 */
public class ManyPepolRoomPayBetResponse extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_不在下注时间 = 1;
	public static final int ERROR_下注位置错误 = 2;
	public static final int ERROR_不在比赛中 = 3;
	
	public int pos;
	public int betNum;
	public ManyPepolRoomPayBetResponse()
	{
		msgType = MsgTypeEnum.manypepol_闲家下注.getType();
	}
	
	public ManyPepolRoomPayBetResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_闲家下注.getType();
		this.code = code;
	}
	
	public ManyPepolRoomPayBetResponse(int code,int pos,int betNum)
	{
		msgType = MsgTypeEnum.manypepol_闲家下注.getType();
		this.code = code;
		this.pos = pos;
		this.betNum = betNum;
	}

	@Override
	public String toString()
	{
		return "ManyPepolRoomPayBetResponse [betNum=" + betNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
