package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class LeaveFruitMachineRequest extends Request
{
	public LeaveFruitMachineRequest()
	{
		msgType = MsgTypeEnum.LeaveFruitMechine.getType();
	}

	@Override
	public String toString()
	{
		return "LeaveFruitMachineRequest [msgType=" + msgType + "]";
	}
}
