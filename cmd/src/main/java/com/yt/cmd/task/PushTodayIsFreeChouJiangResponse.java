package com.yt.cmd.task;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushTodayIsFreeChouJiangResponse extends Response {

	public int freeChouJiang;/// 1 为今天未免费抽奖 2 为今天已经免费抽过奖

	public PushTodayIsFreeChouJiangResponse(int code, int freeChouJiang) {
		this.msgType = MsgTypeEnum.Push_FreeChouJiang.getType();
		this.code = code;
		this.freeChouJiang = freeChouJiang;
	}

	@Override
	public String toString() {
		return "PushTodayIsFreeChouJiangResponse [freeChouJiang=" + freeChouJiang + ", msgType=" + msgType + ", code="
				+ code + "]";
	}
}
