package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class GMUtilLoginRequest  extends Request
{
	public String phone;
	public GMUtilLoginRequest()
	{
		msgType = MsgType.GM_GM工具登录;
	}
	
	public GMUtilLoginRequest(String phone)
	{
		msgType = MsgType.GM_GM工具登录;
		
		this.phone = phone;
	}
}