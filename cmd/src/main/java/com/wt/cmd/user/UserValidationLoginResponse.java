package com.wt.cmd.user;

import com.wt.archive.GameData;
import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class UserValidationLoginResponse extends Response
{
	public static final int ERROR_登陆失败 = 0;
	public static final int ERROR_TOKEN空 = 1;
	public static final int ERROR_没有申请过登陆 = 2;
	public static final int ERROR_TOKEN验证失败 = 3;
	public static final int ERROR_已被封号 = 4;
	
	public GameData gameData;
	public UserValidationLoginResponse()
	{
		this.msgType =MsgType.USER_VALIDATION_TOKEN;
	}
	
	public UserValidationLoginResponse(int code)
	{
		this.msgType =MsgType.USER_VALIDATION_TOKEN;
		this.code = code;
	}
	
	public UserValidationLoginResponse(int code,GameData gameData)
	{
		this.msgType =MsgType.USER_VALIDATION_TOKEN;
		this.code = code;
		this.gameData = gameData;
	}

	@Override
	public String toString()
	{
		return "UserValidationLoginResponse [gameData=" + gameData + ", msgType=" + msgType + ", code=" + code + "]";
	}
	
	
}
