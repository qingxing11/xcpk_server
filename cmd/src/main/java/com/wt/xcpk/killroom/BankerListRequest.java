package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class BankerListRequest extends Request
{
	public BankerListRequest()
	{
		msgType = MsgTypeEnum.KillRoom_庄家列表.getType();
	}
}
