package com.wt.xcpk.killroom;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class BankerListResponse extends Response
{
	public ArrayList<PlayerSimpleData> list_bankerList;
	public BankerListResponse()
	{
		msgType = MsgTypeEnum.KillRoom_庄家列表.getType();
	}
	
	public BankerListResponse(int code,ArrayList<PlayerSimpleData> list_bankerList)
	{
		msgType = MsgTypeEnum.KillRoom_庄家列表.getType();
		this.code = code;
		this.list_bankerList = list_bankerList;
	}
}	
