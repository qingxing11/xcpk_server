package com.wt.cmd.xcpk.manypepol;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

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
public class ManyPepolRoomFoldResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	public static final int ERROR_没轮到行动 = 1;
	
	public ManyPepolRoomFoldResponse()
	{
		msgType = MsgTypeEnum.manypepol_玩家弃牌.getType();
	}
	
	public ManyPepolRoomFoldResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_玩家弃牌.getType();
		this.code = code;
	}
	

	@Override
	public String toString()
	{
		return "FoldResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}	
