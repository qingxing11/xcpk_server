package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ManyPepolRoomStandUpResponse extends Response
{
	public static final int ERROR_还未坐下 = 0;
	public static final int ERROR_不在游戏中 = 1;
	public static final int ERROR_还没上庄 = 2;
	
	public ManyPepolRoomStandUpResponse()
	{
		msgType = MsgTypeEnum.manypepol_申请下桌.getType();
	}
	
	public ManyPepolRoomStandUpResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_申请下桌.getType();
		this.code = code;
	}

	@Override
	public String toString()
	{
		return "ManyPepolRoomStandUpResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}	
