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
public class ComparerPokerResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	public static final int ERROR_没轮到行动 = 1;
	public static final int ERROR_比牌位置错误  = 2;
	public static final int ERROR_比牌对象空  = 3;
	
	public ComparerPokerResponse()
	{
		msgType = MsgTypeEnum.classic_玩家比牌.getType();
	}
	
	public ComparerPokerResponse(int code)
	{
		msgType = MsgTypeEnum.classic_玩家比牌.getType();
		this.code = code;
	}
	
	@Override
	public String toString()
	{
		return "ComparerPokerResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}	
