package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class ReloadConfigRespone extends Response
{
	public ReloadConfigRespone()
	{}

	/**
	 * @param code
	 *                返回码
	 */
	public ReloadConfigRespone(int code)
	{
		this.msgType = MsgType.GM_重载配置;
		this.code = code;
	}
}
