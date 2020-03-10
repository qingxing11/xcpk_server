package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomTableListRequest extends Request
{
	public ManyPepolRoomTableListRequest()
	{
		msgType = MsgTypeEnum.manypepol_上桌列表.getType();
	}
}
