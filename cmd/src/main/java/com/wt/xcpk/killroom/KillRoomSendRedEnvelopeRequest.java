package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class KillRoomSendRedEnvelopeRequest extends Request {
	
	public int userId;//发红包的玩家
	public long redEnvelopeValue;//发的红包金额
	public KillRoomSendRedEnvelopeRequest() {
		this.msgType = MsgTypeEnum.KillRoom_发红包.getType();
	}
	public KillRoomSendRedEnvelopeRequest(int  userId,long redEnvelopeValue) {
		this.msgType = MsgTypeEnum.KillRoom_发红包.getType();
		this.userId=userId;
		this.redEnvelopeValue=redEnvelopeValue;
	}
	
	@Override
	public String toString() {
		return "KillRoomSendRedEnvelopeRequest [userId=" + userId + ", redEnvelopeValue=" + redEnvelopeValue
				+ ", msgType=" + msgType + "]";
	}	
}
