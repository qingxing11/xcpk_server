package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class KillRoomGardRedEnvelopeResponse extends Response {

	public static final int 您的手慢了_当前红包被抢走了 = 0;
	public static final int 您的手慢了_红包已被抢完 = 1;
	public static final int 请求错误_您当前抢的红包不存在 = 2;
	public int userId;
	public RedEnvelopeInfo redEnvelope;
	public KillRoomGardRedEnvelopeResponse(int code) {
		this.msgType = MsgTypeEnum.KillRoom_抢红包.getType();
		this.code=code;
	}

	
	public KillRoomGardRedEnvelopeResponse(int code,int userId,RedEnvelopeInfo redEnvelope) {
		this.msgType = MsgTypeEnum.KillRoom_抢红包.getType();
		this.code=code;
		this.userId=userId;
		this.redEnvelope=redEnvelope;
	}

	@Override
	public String toString() {
		return "KillRoomGardRedEnvelopeResponse [userId=" + userId + ", redEnvelope=" + redEnvelope
				+ ", msgType=" + msgType + ", code=" + code + "]";
	}

}
