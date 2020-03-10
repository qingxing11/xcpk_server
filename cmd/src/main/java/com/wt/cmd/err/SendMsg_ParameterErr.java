package com.wt.cmd.err;

import com.wt.cmd.Response;

public class SendMsg_ParameterErr extends Response
{
	public SendMsg_ParameterErr()
	{
		this.msgType = ERROR_PARAMETER;
	}
}
