package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class ManyPepolRoomRaiseBetRequest extends Request
{
	public int betNum;
	public ManyPepolRoomRaiseBetRequest()
	{
		msgType = MsgTypeEnum.manypepol_玩家加注.getType();
	}
}
