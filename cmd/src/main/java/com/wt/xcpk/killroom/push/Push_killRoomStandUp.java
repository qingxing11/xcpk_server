package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomStandUp extends Response
{
	public int pos;
	
	public Push_killRoomStandUp()
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家站起.getType();
	}

	public Push_killRoomStandUp(int pos)
	{
		this.msgType = MsgTypeEnum.KillRoom_其他玩家站起.getType();
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "Push_killRoomStandUp [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
