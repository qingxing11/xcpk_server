package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 进入房间，返回房间信息
 * 房间状态
 * 当前庄家
 * 座位玩家
 * 上庄列表
 * @author WangTuo
 *
 */
public class KillRoomSitDownResponse extends Response
{
	public static final int ERROR_桌子有人 = 0;
	public static final int ERROR_桌子号错误 = 1;
	public static final int ERROR_已经坐下 = 2;
	public static final int ERROR_庄家不能坐下 = 3;
	
	public int pos;
	public KillRoomSitDownResponse()
	{
		msgType = MsgTypeEnum.KillRoom_选座坐下.getType();
	}
	
	public KillRoomSitDownResponse(int code)
	{
		msgType = MsgTypeEnum.KillRoom_选座坐下.getType();
		this.code = code;
	}
	
	public KillRoomSitDownResponse(int code,int pos)
	{
		msgType = MsgTypeEnum.KillRoom_选座坐下.getType();
		this.code = code;
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return "KillRoomSitDownResponse [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
