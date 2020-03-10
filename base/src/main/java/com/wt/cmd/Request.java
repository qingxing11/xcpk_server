package com.wt.cmd;

/**
 * 请求 父类
 * 
 * msgType表示协议号-表明要调用哪个模块
 * 
 */
public class Request implements MsgType
{
	public int msgType;

	public int callBackId;
	public Request()
	{}
}
