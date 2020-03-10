package com.wt.cmd.loadtest;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class LoadTestConnectRequest  extends Request
{
	public String device_code;
	public LoadTestConnectRequest()
	{
		msgType = MsgType.LOADTEST_连接;
	}
}