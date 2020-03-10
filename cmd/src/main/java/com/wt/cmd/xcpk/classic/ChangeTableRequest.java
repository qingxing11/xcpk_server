package com.wt.cmd.xcpk.classic;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ChangeTableRequest extends Request
{
	public ChangeTableRequest()
	{
		msgType = MsgTypeEnum.classic_换桌.getType();
	}
}
