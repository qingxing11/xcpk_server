package com.wt.cmd.heartbeat;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class HeartbeatRequest extends Request
{
	public HeartbeatRequest() {
		this.msgType = MsgType.HEARTBEAT;;
	}
}
