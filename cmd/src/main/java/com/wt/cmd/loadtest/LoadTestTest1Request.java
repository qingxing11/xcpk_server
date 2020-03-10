package com.wt.cmd.loadtest;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class LoadTestTest1Request  extends Request
{
	public LoadTestTest1Request()
	{
		msgType = MsgType.LOADTEST_测试1;
	}
	
	public LoadTestTest1Request(int callBackId)
	{
		msgType = MsgType.LOADTEST_测试1;
		this.callBackId = callBackId;
	}
}