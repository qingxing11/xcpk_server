package com.yt.cmd.fruitMachine;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class FruitDownBankerResponse extends Response {
	
	public final static int 你不是当前庄家_不能申请下庄 = 0;

	public FruitDownBankerResponse(int code) {
		this.code=code;
		this.msgType=MsgTypeEnum.FruitDownBanker.getType();
	}
	@Override
	public String toString() {
		return "FruitDownBankerResponse [msgType=" + msgType + ", code=" + code
				+ "]";
	}
	
}
