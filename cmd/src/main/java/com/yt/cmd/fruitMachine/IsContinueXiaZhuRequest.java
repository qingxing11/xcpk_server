package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class IsContinueXiaZhuRequest extends Request {
	public IsContinueXiaZhuRequest() {
		this.msgType=MsgTypeEnum.Fruit_ContinueXiaZhu.getType();
	}

	@Override
	public String toString() {
		return "IsContinueXiaZhuRequest [msgType=" + msgType + "]";
	}
	
}
