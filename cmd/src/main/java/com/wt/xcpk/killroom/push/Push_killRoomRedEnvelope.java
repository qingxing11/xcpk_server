package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomRedEnvelope extends Response
{
	public String nickName;
	public long value;
	
	public Push_killRoomRedEnvelope()
	{
		this.msgType = MsgTypeEnum.KillRoom_红包公告.getType();
	}
	
	public Push_killRoomRedEnvelope(String nickName,long value)
	{
		this.nickName= nickName;
		this.value =value;
		this.msgType = MsgTypeEnum.KillRoom_红包公告.getType();
	}

	@Override
	public String toString()
	{
		return "Push_killRoomRedEnvelope [msgType=" + msgType + "]";
	}
}
