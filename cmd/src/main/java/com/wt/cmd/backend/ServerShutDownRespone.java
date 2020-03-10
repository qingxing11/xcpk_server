package com.wt.cmd.backend;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class ServerShutDownRespone extends Response
{
	public int msgType;
	public int code;
	
	public ServerShutDownRespone() {
	}

	/**
	 * @param code 返回码
	 */
	public ServerShutDownRespone(int code) {
		this.msgType = MsgType.GM_服务器关机;
		this.code = code;
	}
}
