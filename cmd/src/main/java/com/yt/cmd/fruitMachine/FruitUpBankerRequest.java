package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FruitUpBankerRequest extends Request {	
	public FruitUpBankerRequest() {
		this.msgType = MsgTypeEnum.FruitUpBanker.getType();
	}
}
