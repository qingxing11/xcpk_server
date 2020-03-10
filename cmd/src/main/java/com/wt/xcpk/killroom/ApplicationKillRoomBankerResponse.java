package com.wt.xcpk.killroom;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class ApplicationKillRoomBankerResponse extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_上庄人数太多 = 1;
	public static final int ERROR_申请上庄时已是庄家 = 2;
	public static final int ERROR_申请上庄时已在列表 = 3;
	
	public ArrayList<PlayerSimpleData> list_bankerList;
	public ApplicationKillRoomBankerResponse()
	{
		msgType = MsgTypeEnum.KillRoom_通杀场上庄.getType();
	}
	
	public ApplicationKillRoomBankerResponse(int code)
	{
		msgType = MsgTypeEnum.KillRoom_通杀场上庄.getType();
		this.code = code;
	}
	
	public ApplicationKillRoomBankerResponse(int code,ArrayList<PlayerSimpleData> list_bankerList)
	{
		msgType = MsgTypeEnum.KillRoom_通杀场上庄.getType();
		this.code = code;
		this.list_bankerList = list_bankerList;
	}
}	
