package com.wt.cmd.user.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_otherDeviceLogin extends Response
{
	public Push_otherDeviceLogin()
	{
		msgType = MsgTypeEnum.PUSHUSER_另一个设备登录.getType();
	}
	

	@Override
	public String toString()
	{
		return "Push_otherDeviceLogin [msgType=" + msgType + ", code=" + code + "]";
	}
}
