package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class CancleContinueXiaZhuRequest extends Request {
	public CancleContinueXiaZhuRequest() {
		this.msgType = MsgTypeEnum.Fruit_CancelContinueXiaZhu.getType();
	}

	@Override
	public String toString() {
		return "CancleContinueXiaZhuRequest [msgType=" + msgType + "]";
	}
	
	
}
