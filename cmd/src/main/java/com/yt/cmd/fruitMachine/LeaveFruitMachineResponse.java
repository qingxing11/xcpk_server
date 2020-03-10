package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class LeaveFruitMachineResponse  extends Response
{
	public LeaveFruitMachineResponse()
	{
		msgType = MsgTypeEnum.LeaveFruitMechine.getType();
	}

	public LeaveFruitMachineResponse(int code)
	{
		this.code = code;
		msgType = MsgTypeEnum.LeaveFruitMechine.getType();
	}
}