package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class Push_BankerWiningResponse extends Response {

	public PlayerSimpleData playerBanker;
	public int winingValue;

	public Push_BankerWiningResponse(int code) {
		this.msgType = MsgTypeEnum.Fruit_bankerWining.getType();
		this.code = code;
	}

	public Push_BankerWiningResponse(int code, int winingValue,PlayerSimpleData playerBanker) {
		this.msgType = MsgTypeEnum.Fruit_bankerWining.getType();
		this.code = code;
		this.winingValue = winingValue;
		this.playerBanker=playerBanker;
	}

	@Override
	public String toString() {
		return "Push_BankerWiningResponse [playerBanker=" + playerBanker + ", winingValue=" + winingValue + ", msgType="
				+ msgType + ", code=" + code + "]";
	}

}
