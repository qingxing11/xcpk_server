package com.wt.cmd.user;

import com.wt.archive.UserData;
import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.util.security.token.TokenVO;

public class WxLoginResponse extends Response
{

	public static final int ERROR_服务器重启维护 = 0;
	public static final int ERROR_证书空 = 1;
	public static final int ERROR_token校验错误 = 2;
	public static final int ERROR_获取信息失败 = 3;

	public UserData bean;
	public TokenVO tokenVO;

	public boolean isReconnect;

	public WxLoginResponse()
	{
		msgType = USER_WX_LOGIN;
	}

	public WxLoginResponse(int code)
	{
		msgType = USER_WX_LOGIN;
		this.code = code;
	}

	public WxLoginResponse(int code, UserData bean, TokenVO tokenVO, boolean isReconnect)
	{
		this.msgType = MsgType.USER_WX_LOGIN;
		this.code = code;
		this.bean = bean;
		this.tokenVO = tokenVO;
		this.isReconnect = isReconnect;
	}

	public WxLoginResponse(int code, UserData bean)
	{

		this.msgType = MsgType.USER_WX_LOGIN;
		this.code = code;
		this.bean = bean;
	}

	@Override
	public String toString()
	{
		return "WxLoginResponse [bean=" + bean + ", tokenVO=" + tokenVO + ", isReconnect=" + isReconnect + "]";
	}

}