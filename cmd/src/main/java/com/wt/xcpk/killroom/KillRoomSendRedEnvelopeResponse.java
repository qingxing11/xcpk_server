package com.wt.xcpk.killroom;

import java.util.ArrayList;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class KillRoomSendRedEnvelopeResponse extends Response {

	public static final int 玩家金币不足 = 0;
	public static final int 您不是当前庄家_不能发红包 = 1;
	public static final int 本轮红包未结束_不能发红包 = 2;
	public ArrayList<RedEnvelopeInfo> redEnvelopeInfo;// 返回具体红包信息
	public int redEnvelopeState;
	public int userId;

	public KillRoomSendRedEnvelopeResponse(int code) {
		this.msgType = MsgTypeEnum.KillRoom_发红包.getType();
		this.code = code;
	}

	public KillRoomSendRedEnvelopeResponse(int code, ArrayList<RedEnvelopeInfo> redEnvelopeInfo, int redEnvelopeState,
			int userId) {
		this.msgType = MsgTypeEnum.KillRoom_发红包.getType();
		this.code = code;
		this.redEnvelopeInfo = redEnvelopeInfo;
		this.redEnvelopeState = redEnvelopeState;
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "KillRoomSendRedEnvelopeResponse [redEnvelopeInfo=" + redEnvelopeInfo + ", redEnvelopeState="
				+ redEnvelopeState + ", userId=" + userId + ", msgType=" + msgType + ", code=" + code + "]";
	}

}
