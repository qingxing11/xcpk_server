package com.wt.cmd.user.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_vipLevel extends Response
{
	public int vipLevel;
	public Push_vipLevel()
	{
		msgType = MsgTypeEnum.PUSHUSER_VIP.getType();
	}
	
	public Push_vipLevel(int vipLevel)
	{
		msgType = MsgTypeEnum.PUSHUSER_VIP.getType();
		this.vipLevel = vipLevel;
	}

	@Override
	public String toString()
	{
		return "Push_vipLevel [vipLevel=" + vipLevel + ", msgType=" + msgType + ", code=" + code + "]";
	}
}