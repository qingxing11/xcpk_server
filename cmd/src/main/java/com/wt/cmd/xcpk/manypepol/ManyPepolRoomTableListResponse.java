package com.wt.cmd.xcpk.manypepol;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

/**
 * 进入经典场：
 * 房间当前状态
 * 当前座位上玩家
 * 当前庄家
 * 
 * 
 * @author akwang
 *
 */
public class ManyPepolRoomTableListResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	
	public ArrayList<PlayerSimpleData> list_players;
	public ManyPepolRoomTableListResponse()
	{
		msgType = MsgTypeEnum.manypepol_上桌列表.getType();
	}
	
	public ManyPepolRoomTableListResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_上桌列表.getType();
		this.code = code;
	}
	
	public ManyPepolRoomTableListResponse(int code,ArrayList<PlayerSimpleData> list_players)
	{
		msgType = MsgTypeEnum.manypepol_上桌列表.getType();
		this.code = code;
		this.list_players = list_players;
	}

	@Override
	public String toString()
	{
		return "ManyPepolRoomTableListResponse [list_players=" + list_players + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
