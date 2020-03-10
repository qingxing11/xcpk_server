package com.wt.cmd.gm;

import com.wt.cmd.MsgType;
import com.wt.cmd.Request;

public class ChangeRobotNumRequest  extends Request
{
	public int num;
	public ChangeRobotNumRequest()
	{
		msgType = MsgType.GM_设置机器人数量;
	}
	
	public ChangeRobotNumRequest(int num)
	{
		this.num = num;
		msgType = MsgType.GM_设置机器人数量;
	}
}