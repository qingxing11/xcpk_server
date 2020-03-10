package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomSitdownRequest extends Request
{
	public ManyPepolRoomSitdownRequest()
	{
		msgType = MsgTypeEnum.manypepol_申请上桌.getType();
	}
}
