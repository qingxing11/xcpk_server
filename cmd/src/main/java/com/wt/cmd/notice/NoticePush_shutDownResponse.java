package com.wt.cmd.notice;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class NoticePush_shutDownResponse  extends Response
{
	public int msgType;
	
	public int timeLeft;
	public NoticePush_shutDownResponse() {
	}

	/**
	 * @param code
	 */
	public NoticePush_shutDownResponse(int timeLeft) {
		this.msgType = MsgType.NOTICE_服务器计时关机;
		this.timeLeft = timeLeft;
	}
}
