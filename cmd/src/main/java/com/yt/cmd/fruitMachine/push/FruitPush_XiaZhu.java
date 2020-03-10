package com.yt.cmd.fruitMachine.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class FruitPush_XiaZhu extends Response
{
	public int userId;
	public int type;
	public int value;

	public FruitPush_XiaZhu(int userId, int type, int value)
	{
		this.msgType = MsgTypeEnum.Fruit_下注.getType();
		this.userId = userId;
		this.type = type;
		this.value = value;
	}

	public FruitPush_XiaZhu()
	{
		this.msgType = MsgTypeEnum.Fruit_下注.getType();
	}

	@Override
	public String toString()
	{
		return "FruitPush_XiaZhu [userId=" + userId + ", type=" + type + ", value=" + value + ", msgType=" + msgType + ", code=" + code + "]";
	}

}
