package com.yt.cmd.fruitMachine;


import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class FruitMachineXiaZhuResponse extends Response {
	
	public static final int CoinNotEnough = 0;// 金币不足
	public FruitMachineXiaZhuResponse()
	{
		this.msgType = MsgTypeEnum.FruitMechine.getType();
	}
	public FruitMachineXiaZhuResponse(int code) 
	{
		this.code = code;
		this.msgType = MsgTypeEnum.FruitMechine.getType();
	}
}
