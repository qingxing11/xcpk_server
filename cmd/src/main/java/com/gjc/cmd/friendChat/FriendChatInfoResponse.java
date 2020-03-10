package com.gjc.cmd.friendChat;

import java.util.Arrays;

import com.wt.cmd.MsgTypeEnum;
import com.wt.cmd.Response;

public class FriendChatInfoResponse extends Response
{
	public int friendId;

	public FriendChatInfoResponse() {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天状态.getType();
	}

	public FriendChatInfoResponse(int friendId) {
		this.msgType = MsgTypeEnum.ChatInfo_好友聊天状态.getType();
		this.friendId = friendId;
		this.code=FriendChatInfoResponse.SUCCESS;
	}

	@Override
	public String toString() {
		return "FriendChatInfoResponse [friendId=" + friendId + ", msgType=" + msgType + ", data="
				+ Arrays.toString(data) + ", callBackId=" + callBackId + ", code=" + code + "]";
	}
	
	
}
