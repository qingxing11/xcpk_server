package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class KillRoomPayBetResponse extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_单边下注超标 = 1;
	public static final int ERROR_庄家不能下注 = 2;
	
	public int pos;
	public int chipNum;
	
	public KillRoomPayBetResponse()
	{
		msgType = MsgTypeEnum.KillRoom_通杀场下注.getType();
	}

	public KillRoomPayBetResponse(int code)
	{
		msgType = MsgTypeEnum.KillRoom_通杀场下注.getType();
		this.code = code;
	}
	
	public KillRoomPayBetResponse(int code,int pos,int chipNum)
	{
		msgType = MsgTypeEnum.KillRoom_通杀场下注.getType();
		this.code = code;
		this.pos = pos;
		this.chipNum = chipNum;
	}

	@Override
	public String toString()
	{
		return "KillRoomPayBetResponse [pos=" + pos + ", chipNum=" + chipNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
