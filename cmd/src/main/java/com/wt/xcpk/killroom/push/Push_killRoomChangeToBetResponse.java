package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomChangeToBetResponse extends Response {
	
	public Push_killRoomChangeToBetResponse() {
		this.msgType = MsgTypeEnum.KillRoom_通杀场下注时间.getType();
	}

	public Push_killRoomChangeToBetResponse(int code) {
		this.msgType = MsgTypeEnum.KillRoom_通杀场下注时间.getType();
		this.code = code;
	}

}
