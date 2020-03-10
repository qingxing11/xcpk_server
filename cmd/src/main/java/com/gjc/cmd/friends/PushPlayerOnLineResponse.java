package com.gjc.cmd.friends;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class PushPlayerOnLineResponse extends Response
{
	public int userId;
	public boolean state;
	
	public PushPlayerOnLineResponse() {
		this.msgType=MsgTypeEnum.Push_好友玩家状态.getType();
	}

	public PushPlayerOnLineResponse(int userId, boolean state) {
		this.msgType=MsgTypeEnum.Push_好友玩家状态.getType();
		this.userId = userId;
		this.state = state;
		this.code=PushPlayerOnLineResponse.SUCCESS;
	}

	@Override
	public String toString() {
		return "PushPlayerOnLineResponse [userId=" + userId + ", state=" + state + ", msgType=" + msgType + ", data="
				+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	
	
}
