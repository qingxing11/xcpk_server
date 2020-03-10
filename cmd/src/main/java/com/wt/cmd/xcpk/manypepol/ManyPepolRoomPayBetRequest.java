package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomPayBetRequest extends Request
{
	public int pos;
	public int betNum;
	public ManyPepolRoomPayBetRequest()
	{
		msgType = MsgTypeEnum.manypepol_闲家下注.getType();
	}
}
