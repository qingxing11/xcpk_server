package com.yt.cmd.antiaddiction;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class RealNameAuthenticationResponse extends Response
{
	public static final int 验证失败  = 0; 
	public RealNameAuthenticationResponse(int code)
	{
		this.code=code;
		this.msgType=MsgTypeEnum.RealNameAuthentication.getType();
	}

	@Override
	public String toString()
	{
		return "RealNameAuthenticationResponse []";
	}
	
}
