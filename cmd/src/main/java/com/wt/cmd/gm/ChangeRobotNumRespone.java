package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Response;

public class ChangeRobotNumRespone extends Response
{
	public int msgType;
	public int code;
	
	public ChangeRobotNumRespone() {
		this.msgType = MsgType.GM_设置机器人数量;
	}

	/**
	 * @param code 返回码
	 */
	public ChangeRobotNumRespone(int code) {
		this.msgType = MsgType.GM_设置机器人数量;
		this.code = code;
	}
}
