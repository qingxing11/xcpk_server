package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class JiangPoolResponse extends Response {

	public long jiangPoolValue;

	public JiangPoolResponse(int code) {
		this.code = code;
		this.msgType = MsgTypeEnum.Fruit_JiangPool.getType();
	}

	public JiangPoolResponse(int code, long jiangPoolValue) {
		this.code = code;
		this.jiangPoolValue = jiangPoolValue;
		this.msgType = MsgTypeEnum.Fruit_JiangPool.getType();
	}

	@Override
	public String toString() {
		return "JiangPoolResponse [jiangPoolValue=" + jiangPoolValue + ", msgType=" + msgType + ", code=" + code + "]";
	}
}
