package com.wt.cmd.backend;

import com.wt.cmd.Response;

public class PaySuccessResponse extends Response
{
	public static final int ERROR_没有这个玩家 = 0;
	
	public PaySuccessResponse()
	{
		msgType = BACKEND_购买金币成功;
	}
	
	public PaySuccessResponse(int code,int callBackId)
	{
		msgType = BACKEND_购买金币成功;
		this.code = code;
		this.callBackId = callBackId;
	}
}
