package com.wt.cmd.user;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;
import com.wt.naval.dao.model.user.UserInfoModel;

public class GServerValidationLoginResponse extends Response
{
	public static final int ERROR_登陆失败 = 0;
	public static final int ERROR_TOKEN空 = 1;
	public static final int ERROR_没有申请过登陆 = 2;
	public static final int ERROR_TOKEN验证失败 = 3;
	
	public UserInfoModel userModel;
//	public UserAntiAddictionModel userAntiAddictionModel;
	
	public long lastLogoutTime;
	public long antiAddictionTime;
	public GServerValidationLoginResponse()
	{
		this.msgType =MsgType.USER_VALIDATION_TOKEN;
	}
	
	public GServerValidationLoginResponse(int code)
	{
		this.msgType =MsgType.USER_VALIDATION_TOKEN;
		this.code = code;
	}
	
	public GServerValidationLoginResponse(int code,UserInfoModel userModel,long antiAddictionTime,long lastLogoutTime)
	{
		this.msgType =MsgType.USER_VALIDATION_TOKEN;
		this.code = code;
		this.userModel = userModel;
		this.antiAddictionTime = antiAddictionTime;
		this.lastLogoutTime = lastLogoutTime;
	}

	@Override
	public String toString()
	{
		return "GServerValidationLoginResponse [userModel=" + userModel + "]";
	}
}
