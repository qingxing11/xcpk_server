package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_FruitMachineStartXiaZhuResponse extends Response {
	
	public Push_FruitMachineStartXiaZhuResponse() {
		this.msgType = MsgTypeEnum.FruitMechine_开始下注.getType();
	}

	@Override
	public String toString()
	{
		return "Push_FruitMachineStartXiaZhuResponse [msgType=" + msgType + ", code=" + code + "]";
	}
	
}
