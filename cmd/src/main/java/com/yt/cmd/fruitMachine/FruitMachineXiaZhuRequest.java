package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FruitMachineXiaZhuRequest extends Request {
	public int fruitMachineType;//押注类型
	public int fruitMachineValue;//押注值

	public FruitMachineXiaZhuRequest() {
		this.msgType=MsgTypeEnum.FruitMechine.getType();
	}
	
	public FruitMachineXiaZhuRequest(int fruitMachineType,int fruitMachineValue) {
		this.msgType=MsgTypeEnum.FruitMechine.getType();
		this.fruitMachineType=fruitMachineType;
		this.fruitMachineValue=fruitMachineValue;
	}

	@Override
	public String toString() {
		return "FruitMachineRequest [fruitMachineType=" + fruitMachineType + ", fruitMachineValue=" + fruitMachineValue
				+ ", msgType=" + msgType + "]";
	}	
}
