package com.yt.cmd.fruitMachine;

import java.util.ArrayList;
import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class FruitBankerListResponse extends Response {

	public ArrayList<PlayerSimpleData> list_bankerPlayers;

	public FruitBankerListResponse(int code) {
		this.msgType = MsgTypeEnum.Fruit_requestBankerList.getType();
		this.code = code;
	}

	public FruitBankerListResponse(int code, ArrayList<PlayerSimpleData> list_bankerPlayers) {
		this.msgType = MsgTypeEnum.Fruit_requestBankerList.getType();
		this.code = code;
		this.list_bankerPlayers = list_bankerPlayers;
	}

	@Override
	public String toString() {
		return "FruitBankerListResponse [list_bankerPlayers=" + list_bankerPlayers + ", msgType=" + msgType + ", code="
				+ code + "]";
	}
}
