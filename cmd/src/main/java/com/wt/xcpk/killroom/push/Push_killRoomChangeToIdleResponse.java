package com.wt.xcpk.killroom.push;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class Push_killRoomChangeToIdleResponse extends Response {
	
	public Push_killRoomChangeToIdleResponse() {
		this.msgType = MsgTypeEnum.KillRoom_休息时间.getType();
	}

	public Push_killRoomChangeToIdleResponse(int code) {
		this.msgType = MsgTypeEnum.KillRoom_休息时间.getType();
		this.code = code;
	}

}
