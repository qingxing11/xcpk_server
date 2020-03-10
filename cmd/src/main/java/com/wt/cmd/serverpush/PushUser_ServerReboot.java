package com.wt.cmd.serverpush;

import com.wt.cmd.Response;

public class PushUser_ServerReboot extends Response
{
	public PushUser_ServerReboot()
	{
		msgType = NOTICE_服务器准备重启;
	}
}
