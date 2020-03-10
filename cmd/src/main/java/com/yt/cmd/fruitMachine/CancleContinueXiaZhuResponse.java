package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class CancleContinueXiaZhuResponse extends Response {
	public CancleContinueXiaZhuResponse() {
		this.msgType = MsgTypeEnum.Fruit_CancelContinueXiaZhu.getType();
	}

	public CancleContinueXiaZhuResponse(int code) {
		this.msgType = MsgTypeEnum.Fruit_CancelContinueXiaZhu.getType();
		this.code=code;
	}
	@Override
	public String toString() {
		return "CancleContinueXiaZhuResponse [msgType=" + msgType + ", code=" + code + "]";
	}
	
	
}
