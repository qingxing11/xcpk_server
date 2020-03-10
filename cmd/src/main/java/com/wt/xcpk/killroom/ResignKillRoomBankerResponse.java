package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class ResignKillRoomBankerResponse extends Response
{
	public static final int ERROR_不是庄家 = 0;
	
	public ResignKillRoomBankerResponse()
	{
		msgType = MsgTypeEnum.KillRoom_通杀场下庄.getType();
	}
	
	public ResignKillRoomBankerResponse(int code)
	{
		msgType = MsgTypeEnum.KillRoom_通杀场下庄.getType();
		this.code = code;
	}
}	
