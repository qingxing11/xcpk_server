package com.wt.cmd.xcpk.classic;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

/**
 * 经典场换桌：
 * 房间当前状态
 * 当前座位上玩家
 * 当前庄家
 * 
 * 
 * @author akwang
 *
 */
public class ChangeTableResponse extends Response
{
	public static final int ERROR_不在比赛中 = 0;
	public static final int ERROR_换桌错误 = 1;
	
	public ChangeTableResponse()
	{
		msgType = MsgTypeEnum.classic_换桌.getType();
	}
	
	public ChangeTableResponse(int code)
	{
		msgType = MsgTypeEnum.classic_换桌.getType();
		this.code = code;
	}
	
	@Override
	public String toString()
	{
		return "ChangeTableResponse [msgType=" + msgType + ", code=" + code + "]";
	}

}	
