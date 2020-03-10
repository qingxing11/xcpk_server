package com.wt.cmd.test;

import com.wt.cmd.Request;

public class TestRequest extends Request
{
	public String testText;
	public TestRequest()
	{
		this.msgType = TEST;
	}
	@Override
	public String toString()
	{
		return "TestRequest [testText=" + testText + "]";
	}
}
