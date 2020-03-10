package com.wt.cmd.notice;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class NoticePush_serverNotice extends Response
{
//	public int msgType;
	
	public String msg;
	public NoticePush_serverNotice() {
		this.msgType = MsgType.NOTICE_服务器公告;
	}

	/**
	 * @param code
	 */
	public NoticePush_serverNotice(String msg) {
		this.msgType = MsgType.NOTICE_服务器公告;
		this.msg = msg;
	}
}
