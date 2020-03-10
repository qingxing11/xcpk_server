package com.brc.cmd.update;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class UpdateSignResponse extends Response
{
	public String sign;
	public UpdateSignResponse(int code)
	{
		msgType=MsgTypeEnum.UPDATESIGN.getType();
		this.code=code;
	}
	public UpdateSignResponse(int code,String sign)
	{
		msgType=MsgTypeEnum.UPDATESIGN.getType();
		this.code=code;
		this.sign=sign;
	}
}
