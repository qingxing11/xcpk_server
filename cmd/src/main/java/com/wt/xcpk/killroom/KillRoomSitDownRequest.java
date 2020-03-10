package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class KillRoomSitDownRequest extends Request
{
	public int pos;
	public KillRoomSitDownRequest()
	{
		msgType = MsgTypeEnum.KillRoom_选座坐下.getType();
	}
}
