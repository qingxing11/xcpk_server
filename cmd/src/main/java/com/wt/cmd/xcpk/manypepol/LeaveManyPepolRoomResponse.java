package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 
 * @author akwang
 *
 */
public class LeaveManyPepolRoomResponse extends Response
{
	public static final int ERROR_不在游戏中 = 0;
	public static final int ERROR_坐下时不能离开 = 1;
	
	public LeaveManyPepolRoomResponse()
	{
		msgType = MsgTypeEnum.manypepol_玩家离开.getType();
	}
	
	public LeaveManyPepolRoomResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_玩家离开.getType();
		this.code = code;
	}
	

	@Override
	public String toString()
	{
		return "LeaveManyPepolRoomResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}	
