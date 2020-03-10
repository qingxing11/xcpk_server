package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.vo.JackpotData;

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
public class GetJackpotResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	
	public JackpotData jackpotData;
	
	public GetJackpotResponse()
	{
		msgType = MsgTypeEnum.KillRoom_获取奖池.getType();
	}
	
	public GetJackpotResponse(int code)
	{
		msgType = MsgTypeEnum.KillRoom_获取奖池.getType();
		this.code = code;
	}
	
	public GetJackpotResponse(int code,JackpotData jackpotData)
	{
		msgType = MsgTypeEnum.KillRoom_获取奖池.getType();
		this.code = code;
		this.jackpotData = jackpotData;
	}
	
	@Override
	public String toString()
	{
		return "GetJackpotResponse [jackpotData=" + jackpotData + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
