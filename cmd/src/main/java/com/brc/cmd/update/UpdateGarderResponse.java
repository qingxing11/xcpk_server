package com.brc.cmd.update;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class UpdateGarderResponse extends Response
{
	public int gender;
	public UpdateGarderResponse(int code)
	{
		this.code=code;
		msgType=MsgTypeEnum.UPDATEGENDER.getType();
	}
	public UpdateGarderResponse(int code,int gender)
	{
		this.code=code;
		msgType=MsgTypeEnum.UPDATEGENDER.getType();
		this.gender=gender;
	}
}
