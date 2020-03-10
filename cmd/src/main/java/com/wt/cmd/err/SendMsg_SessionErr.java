package com.wt.cmd.err;

import com.wt.cmd.Response;

public class SendMsg_SessionErr extends Response
{
	public SendMsg_SessionErr()
	{
		this.msgType = ERROR_SESSIONERR;
	}
}
