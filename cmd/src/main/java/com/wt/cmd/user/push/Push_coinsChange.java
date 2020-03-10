package com.wt.cmd.user.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_coinsChange extends Response
{
	public long changeNum; // 改变数量

	public Push_coinsChange()
	{
		this.msgType = MsgTypeEnum.PUSHUSER_金币变化.getType();
	}

	public Push_coinsChange(long changeNum)
	{
		this.msgType = MsgTypeEnum.PUSHUSER_金币变化.getType();
		this.changeNum = changeNum;
	}
}
