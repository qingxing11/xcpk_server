package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FruitBankerListRequest extends Request {
	public FruitBankerListRequest() {
		this.msgType = MsgTypeEnum.Fruit_requestBankerList.getType();
	}

	@Override
	public String toString() {
		return "FruitBankerListRequest [msgType=" + msgType + "]";
	}	
}
