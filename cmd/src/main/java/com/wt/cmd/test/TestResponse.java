package com.wt.cmd.test;

import com.wt.cmd.Response;

public class TestResponse extends Response
{
	public int code;
	public int num;
	public String name;
	public byte type;
	public TestResponse() 
	{}
	
	public TestResponse(int code,int num, String name, byte type)
	{
		this.msgType = TEST;
		this.code = code;
		this.num = num;
		this.name = name;
		this.type = type;
	}
	
	@Override
	public String toString()
	{
		return "TestResponse [code=" + code + ", num=" + num + ", name=" + name + ", type=" + type + "]";
	}
}
