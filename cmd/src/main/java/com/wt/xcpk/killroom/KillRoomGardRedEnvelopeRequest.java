package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Request;

public class KillRoomGardRedEnvelopeRequest extends Request {

	public int redEnvelopeIndex;
	public long redEnvelopeValue;

	public KillRoomGardRedEnvelopeRequest() {
		this.msgType = MsgTypeEnum.KillRoom_抢红包.getType();
	}
	
	public KillRoomGardRedEnvelopeRequest(int redEnvelopeIndex,long redEnvelopeValue) {
		this.msgType = MsgTypeEnum.KillRoom_抢红包.getType();
		this.redEnvelopeIndex =redEnvelopeIndex;
		this.redEnvelopeValue=redEnvelopeValue;
	}

	@Override
	public String toString() {
		return "KillRoomGardRedEnvelopeRequest [redEnvelopeIndex=" + redEnvelopeIndex + ", redEnvelopeValue="
				+ redEnvelopeValue + ", msgType=" + msgType + "]";
	}
	
	
}
