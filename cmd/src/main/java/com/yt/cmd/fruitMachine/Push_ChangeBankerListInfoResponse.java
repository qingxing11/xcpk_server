package com.yt.cmd.fruitMachine;
import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;
import com.wt.xcpk.PlayerSimpleData;

public class Push_ChangeBankerListInfoResponse extends Response {

	public PlayerSimpleData nextBanker;
	public ArrayList<PlayerSimpleData> bankers;

	public Push_ChangeBankerListInfoResponse(int code, ArrayList<PlayerSimpleData> bankers,PlayerSimpleData nextBanker) {

		this.msgType = MsgTypeEnum.Fruit_bankerListChange.getType();
		this.code = code;
		this.bankers = bankers;
		this.nextBanker=nextBanker;
	}

	@Override
	public String toString() {
		return "Push_ChangeBankerListInfoResponse [nextBanker=" + nextBanker + ", bankers=" + bankers + ", msgType="
				+ msgType + ", code=" + code + "]";
	}
}
