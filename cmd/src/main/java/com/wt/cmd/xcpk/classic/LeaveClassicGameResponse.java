package com.wt.cmd.xcpk.classic;

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
public class LeaveClassicGameResponse extends Response
{
	public static final int ERROR_不在游戏中 = 0;
	
	public LeaveClassicGameResponse()
	{
		msgType = MsgTypeEnum.classic_玩家离开.getType();
	}
	
	public LeaveClassicGameResponse(int code)
	{
		msgType = MsgTypeEnum.classic_玩家离开.getType();
		this.code = code;
	}
	

	@Override
	public String toString()
	{
		return "LeaveClassicGameResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}	
