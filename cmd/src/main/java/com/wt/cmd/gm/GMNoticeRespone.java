package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class GMNoticeRespone extends Response
{
	public int msgType;
	public int code;
	
	public GMNoticeRespone()
    {
		this.msgType = MsgType.GM_发送公告;
	}

	/**
	 * @param code 返回码
	 */
	public GMNoticeRespone(int code) {
		this.msgType = MsgType.GM_发送公告;
		this.code = code;
	}
}
