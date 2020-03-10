package com.yt.cmd.fruitMachine;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class FruitUpBankerResponse extends Response {
	public final static int 未达到上装条件 = 0;
	public final static int 您已是当前房间庄家 = 1;
	public final static int 申请上庄失败 = 2;
	public final static int 申请人数已达上限 = 3;
	public PlayerSimpleData requestPlayer;
	public FruitUpBankerResponse() {
		msgType = MsgTypeEnum.FruitUpBanker.getType();
	}

	public FruitUpBankerResponse(int code) {
		this.code = code;
		msgType = MsgTypeEnum.FruitUpBanker.getType();
	}
	public FruitUpBankerResponse(int code,PlayerSimpleData requestPlayer) {
		this.code = code;
		msgType = MsgTypeEnum.FruitUpBanker.getType();
		this.requestPlayer=requestPlayer;
	}

	@Override
	public String toString() {
		return "FruitUpBankerResponse [requestPlayer=" + requestPlayer + ", msgType=" + msgType + ", code=" + code
				+ "]";
	}	
}
