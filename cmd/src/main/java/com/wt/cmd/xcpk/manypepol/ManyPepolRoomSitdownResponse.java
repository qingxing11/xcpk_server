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
public class ManyPepolRoomSitdownResponse extends Response
{
	public static final int ERROR_金币不足 = 0;
	public static final int ERROR_已经坐下 = 1;
	public static final int ERROR_列表满 = 2;
	public static final int ERROR_已在列表 = 3;
	
	
	public ManyPepolRoomSitdownResponse()
	{
		msgType = MsgTypeEnum.manypepol_申请上桌.getType();
	}
	
	public ManyPepolRoomSitdownResponse(int code)
	{
		msgType = MsgTypeEnum.manypepol_申请上桌.getType();
		this.code = code;
	}

	@Override
	public String toString()
	{
		return "ManyPepolRoomSitdownResponse [msgType=" + msgType + ", code=" + code + "]";
	}
}	
