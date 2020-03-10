package com.cmd.bag;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

/**
 * 回收道具
 */

public class BagRecyclingPropsResponse extends Response
{
	public static final int ERROR_玩家没有该道具 = 1;
	public static final int ERROR_使用数量不正确 = 2;

	public int num;// 要使用的 数量
	public int propId;// id

	public BagRecyclingPropsResponse()
	{
		msgType = MsgTypeEnum.GAME_BAGRECYCLINGPROP.getType();
	}

	public BagRecyclingPropsResponse(int code, int num, int propId)
	{
		msgType = MsgTypeEnum.GAME_BAGRECYCLINGPROP.getType();
		this.code = code;
		this.num = num;
		this.propId = propId;
	}

	public BagRecyclingPropsResponse(int code, int propId)
	{
		msgType = MsgTypeEnum.GAME_BAGRECYCLINGPROP.getType();
		this.code = code;
		this.propId = propId;
	}

	public String toString()
	{
		return "回应回收道具的信息num=" + num + ",propId=" + propId + ",code" + code;
	}
}
