package com.cmd.bag;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

/**
 * 回收道具
 */
public class BagRecyclingPropsRequest extends Request
{

	public int num;// 使用的 数量
	public int propId;// id

	public BagRecyclingPropsRequest()
	{
		msgType = MsgTypeEnum.GAME_BAGRECYCLINGPROP.getType();
	}

	public BagRecyclingPropsRequest(int num, int propId)
	{
		msgType = MsgTypeEnum.GAME_BAGRECYCLINGPROP.getType();
		this.num = num;
		this.propId = propId;
	}

	public String toString()
	{
		return "回收道具。num=" + num + ",propId=" + propId;
	}

}
