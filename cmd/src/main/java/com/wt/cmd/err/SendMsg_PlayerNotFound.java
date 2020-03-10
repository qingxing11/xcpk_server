package com.wt.cmd.err;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class SendMsg_PlayerNotFound extends Response
{
	public SendMsg_PlayerNotFound()
	{
		this.msgType = MsgTypeEnum.ERROR_SESSIONERR.getType();
	}
}
