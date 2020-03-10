package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class IsContinueXiaZhuResponse extends Response {
	public IsContinueXiaZhuResponse() {
		this.msgType = MsgTypeEnum.Fruit_ContinueXiaZhu.getType();
	}
	public IsContinueXiaZhuResponse(int code) {
		this.msgType = MsgTypeEnum.Fruit_ContinueXiaZhu.getType();
		this.code=code;
	}
	@Override
	public String toString() {
		return "IsContinueXiaZhuResponse [msgType=" + msgType + "]";
	}
	
}
