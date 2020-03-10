package com.wt.cmd.backend;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class ReloadConfigRequest  extends Request
{
	public ReloadConfigRequest()
	{
		msgType = MsgType.GM_重载配置;
	}
}