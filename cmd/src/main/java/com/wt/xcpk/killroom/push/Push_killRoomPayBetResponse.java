package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomPayBetResponse extends Response
{
	public int userId;
	public int payPos;
	public int payNum;
	
	public Push_killRoomPayBetResponse()
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家下注.getType();
	}

	public Push_killRoomPayBetResponse(int code)
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家下注.getType();
		this.code = code;
	}
	
	public Push_killRoomPayBetResponse(int userId,int payPos,int payNum)
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家下注.getType();
		this.userId = userId;
		this.payPos = payPos;
		this.payNum = payNum;
	}

	@Override
	public String toString()
	{
		return "Push_killRoomPayBetResponse [userId=" + userId + ", payPos=" + payPos + ", payNum=" + payNum + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
