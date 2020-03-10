package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomStandUpRequest extends Request
{
	public ManyPepolRoomStandUpRequest()
	{
		msgType = MsgTypeEnum.manypepol_申请下桌.getType();
	}
}
