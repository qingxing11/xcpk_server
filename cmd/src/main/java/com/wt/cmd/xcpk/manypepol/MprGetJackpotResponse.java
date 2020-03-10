package com.wt.cmd.xcpk.manypepol;

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
public class MprGetJackpotResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	
	public JackpotData jackpotData;
	public MprGetJackpotResponse()
	{
		msgType = MsgTypeEnum.manypepol_获取奖池.getType();
	}
	
	public MprGetJackpotResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_获取奖池.getType();
		this.code = code;
	}
	
	public MprGetJackpotResponse(int code,JackpotData jackpotData)
	{
		msgType = MsgTypeEnum.manypepol_获取奖池.getType();
		this.code = code;
		this.jackpotData = jackpotData;
	}
	
	@Override
	public String toString()
	{
		return "GetJackpotResponse [jackpotData=" + jackpotData + ", msgType=" + msgType + ", code=" + code + "]";
	}
}	
