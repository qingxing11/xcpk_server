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
public class KillRoomStandUpResponse extends Response
{
	public int pos;
	public KillRoomStandUpResponse()
	{
		msgType = MsgTypeEnum.KillRoom_从座位站起.getType();
	}
	
	public KillRoomStandUpResponse(int code,int pos)
	{
		this.code = code;
		msgType = MsgTypeEnum.KillRoom_从座位站起.getType();
		this.pos = pos;
	}
	
	public KillRoomStandUpResponse(int code)
	{
		this.code = code;
		msgType = MsgTypeEnum.KillRoom_从座位站起.getType();
	}

	@Override
	public String toString()
	{
		return "KillRoomStandUpResponse [pos=" + pos + ", msgType=" + msgType + ", code=" + code + "]";
	}
}