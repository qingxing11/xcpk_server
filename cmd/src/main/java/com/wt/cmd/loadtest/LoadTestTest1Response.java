package com.wt.cmd.loadtest;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class LoadTestTest1Response extends Response
{
	public int value;

	public LoadTestTest1Response()
	{
		this.msgType = MsgType.LOADTEST_测试1;
	}

	public LoadTestTest1Response(int code, int value, int callBackid)
	{
		this.msgType = MsgType.LOADTEST_测试1;
		this.code = code;

		this.value = value;

		this.callBackId = callBackid;
	}
}
