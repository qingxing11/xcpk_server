package com.wt.xcpk.killroom;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class KillRoomSendRedEnvelopeOverResponse extends Response {
	
	public KillRoomSendRedEnvelopeOverResponse() {
		this.msgType=MsgTypeEnum.KillRoom_抢红包结束.getType();
	}
	@Override
	public String toString() {
		return "KillRoomSendRedEnvelopeOverResponse [msgType=" + msgType + ", code=" + code + "]";
	}	
}
