package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class FruitDownBankerRequest extends Request {
	public FruitDownBankerRequest() {
		this.msgType = MsgTypeEnum.FruitDownBanker.getType();
	}
}
