package com.wt.cmd.user;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class UpdateHeadIconResponse extends Response
{
	public static final int ERROR_头像数据错误 = 0;
	
	public String headIconUrl;
	public UpdateHeadIconResponse()
	{
		this.msgType = MsgTypeEnum.USER_上传自定义头像.getType();
	}

	public UpdateHeadIconResponse(int code)
	{
		this.msgType = MsgTypeEnum.USER_上传自定义头像.getType();
		this.code = code;
	}
	public  UpdateHeadIconResponse(int code,String headIconUrl)
	{
		this.msgType = MsgTypeEnum.USER_上传自定义头像.getType();
		this.code = code;
		this.headIconUrl=headIconUrl;
	}
}
