package com.wt.cmd.heartbeat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class HeartbeatResponse extends Response
{
	public HeartbeatResponse()
	{
		this.msgType = MsgType.HEARTBEAT_RESPONSECLIENT;
	}

	@Override
	public String toString()
	{
		return "HeartbeatResponse []"+msgType;
	}
}
